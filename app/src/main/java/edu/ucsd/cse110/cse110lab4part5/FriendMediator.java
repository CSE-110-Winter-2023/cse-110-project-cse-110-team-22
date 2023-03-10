package edu.ucsd.cse110.cse110lab4part5;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FriendMediator {
    Map<String, Friend> uuidToFriendMap = new HashMap<>();
    private static FriendMediator instance = null;
    private CompassActivity compassActivity;
    private MainActivity mainActivity;
    private ServerAPI serverAPI = ServerAPI.getInstance();

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private UserLocationService userLocationService;
    private UserOrientationService orientationService;
    private double userOrientation;
    private Location userLocation;

    private boolean GPSSignalGood;
    private String GPSStatusStr;


    String publicUUID;
    String privateUUID;
    String name;

    Location location;

    public void updateGPSStatus(boolean GPSSignalGood, String GPSStatusStr) {
        this.GPSSignalGood = GPSSignalGood;
        this.GPSStatusStr = GPSStatusStr;
    }

    public void setCompassActivity(CompassActivity compassActivity) {
        Log.d("CompassActivity", "Set");
        this.compassActivity = compassActivity;
        for (String uuid: uuidToFriendMap.keySet()) {
            Friend f = uuidToFriendMap.get(uuid);
            compassActivity.addFriendToCompass(Integer.parseInt(f.getUuid()), f.getName());
        }
    }

    public static FriendMediator getInstance() {
        if (instance == null) {
            instance = new FriendMediator();
        }
        return instance;
    }

    public void init(MainActivity context){
        this.mainActivity = context;
        userLocation = UserLocation.singleton(0, 0, "You");
        userOrientation = 0.0;
        userLocationService = UserLocationService.singleton(context);
        orientationService = UserOrientationService.singleton(context);
        userLocationService.getLocation().observe(context, loc -> {
            userLocation = UserLocation.singleton(loc.first, loc.second, "You");
        });
        orientationService.getOrientation().observe(context, orient -> {
            userOrientation = Math.toDegrees((double) orient);
        });

        List<String> friendUUIDS = SharedPrefUtils.getAllID(context);

        for(String uuid: friendUUIDS){
            uuidToFriendMap.put(uuid, new Friend("", uuid));
        }


        // If no existing uuids, generate them
        if(!SharedPrefUtils.hasPubUUID(context)){
            String publicUUID = serverAPI.getNewUUID();
            String privateUUID = serverAPI.getNewUUID();
            SharedPrefUtils.setPubUUID(context, Integer.valueOf(publicUUID));
            SharedPrefUtils.setPrivUUID(context, Integer.valueOf(privateUUID));
        }

        // get uuids
        publicUUID = String.valueOf(SharedPrefUtils.getPubUUID(context));
        privateUUID = String.valueOf(SharedPrefUtils.getPrivUUID(context));



        name = SharedPrefUtils.getName(context);

        executor.scheduleAtFixedRate(() -> {
            try{
                Log.d("FriendMediator", "Started task");
                for(String uuid: uuidToFriendMap.keySet()){
                    Future<Friend> friend = serverAPI.getFriendAsync(uuid);
                    try {
                        uuidToFriendMap.put(uuid, friend.get());
                        // NOTE: FRIEND IS UPDATED
                    } catch (ExecutionException e) {
                        Log.e("Mediator", e.toString());
                    } catch (InterruptedException e) {
                        Log.e("Mediator", e.toString());
                    }
                }
                Log.d("Mediator", "Finished Updating round");
                // All friends updated, notify UI
                updateUI();
            } catch(Exception e){
                Log.d("Mediator Error", e.toString());
            }
            }, 0, 1, TimeUnit.SECONDS);
    }

    /*
    Try adding a new friend with the uuid provided. If uuid invalid, do nothing.
    If uuid is valid, add the friend to the HashMap, get their info from the server, then
    update compassUI. Always update CompassUI after checking with the server for the latest info.
    We also update GPS info, so the entire UI is updated after a new friend is added.
     */
    public void addFriend(Context context, String uuid) {
        //Friend friend = new Friend(null, uuid);
        boolean friendIsValid;// = updateNewFriendStatus(friend);
        Future<Friend> future = serverAPI.getFriendAsync(uuid);
        Friend friend = null;
        try {
            friend = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        friendIsValid = (friend != null);


        if (friendIsValid) {
            uuidToFriendMap.put(uuid, friend);
            SharedPrefUtils.writeID(context, uuid);
            //TODO: Fix
            if (compassActivity != null) {
                compassActivity.addFriendToCompass(Integer.parseInt(uuid), friend.getName()); // new
            }
            updateUI();
        } else {
            // TODO something like a warning "invalid uuid"
            Utilities.showAlert((Activity)context, "invalid uuid");
        }
    }

//    private boolean updateNewFriendStatus(Friend friend) {
//        // TODO communicate with the server
//        // TODO check the return value from the server, check if the friend uid is valid
//        // TODO if valid, update friend with setName, setLocation
//        // TODO if notValid, simply return false
//        Future<Boolean> future = serverAPI.uuidExistsAsync(friend.getUuid());
//        boolean exists;
//        try {
//            exists = future.get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//        return false; // Put here for compilation to work
//    }


    private void updateServer() {
        // TODO update server on own info/location (US4)
    }

    private void updateUserForUI() {
        compassActivity.updateUser(userLocation, userOrientation);
    }

    private void updateGPSUI() {
        compassActivity.updateGPSStatus(GPSSignalGood, GPSStatusStr);
    }

    private void updateCompassUI() {
        compassActivity.updateFriendsMap(uuidToFriendMap);
    }

    public void setName(Context context, String name){
        if(!SharedPrefUtils.hasName(context)){
            this.name = name;
            SharedPrefUtils.writeName(context, name);
        }

        // TODO: Update this for future stories to use actual services
        // Do initial upsert of Self
        Future<String> response = serverAPI.upsertUserAsync(publicUUID, serverAPI.formatUpsertJSON(privateUUID
                , name
                , 0.0
                , 0.0));

        try {
            response.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getOrGenerateUUID(Context context){
        return Integer.valueOf(publicUUID);
    }

    public void updateUI() {
        if(compassActivity == null){
            return;
        }
        Log.d("Mediator", "updateUI called");
        updateUserForUI();
        updateGPSUI();
        updateCompassUI();
        compassActivity.display();
    }
}

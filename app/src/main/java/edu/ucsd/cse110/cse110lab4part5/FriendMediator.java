package edu.ucsd.cse110.cse110lab4part5;

import static edu.ucsd.cse110.cse110lab4part5.UserUUID.String_toUUID;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.internal.Util;

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
    private Location userLocation = UserLocation.singleton(0,0,"you");

    private boolean GPSSignalGood;
    private String GPSStatusStr;


    String publicUUID = "Waiting on Server to return a new UUID";
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
        userLocationService = UserLocationService.singleton(compassActivity);
        orientationService = UserOrientationService.singleton(compassActivity);
        userLocationService.getLocation().observe(compassActivity, loc -> {
            userLocation = UserLocation.singleton(loc.first, loc.second, "You");
            // upsert new location data to server
            serverAPI.upsertUserAsync(this.publicUUID, serverAPI.formatUpsertJSON(this.privateUUID, this.name, userLocation.getLatitude(), userLocation.getLongitude()));
            Log.d("LocationService", String.valueOf(userLocation.getLatitude()) + " " + String.valueOf(userLocation.getLongitude()));
        });
        orientationService.getOrientation().observe(compassActivity, orient -> {
            userOrientation = Math.toDegrees((double) orient);
            Log.d("OrientationService", String.valueOf(userOrientation));
        });
        for (String uuid: uuidToFriendMap.keySet()) {
            Friend f = uuidToFriendMap.get(uuid);
            compassActivity.addFriendToCompass(String_toUUID(f.getUuid()), f.getName());
        }
    }

    public static FriendMediator getInstance() {
        if (instance == null) {
            instance = new FriendMediator();
        }
        return instance;
    }

    public boolean init(MainActivity context){
        this.mainActivity = context;


        List<String> friendUUIDS = SharedPrefUtils.getAllID(context);

        for(String uuid: friendUUIDS){
            Friend blankFriend = new Friend("", uuid);
            blankFriend.setLocation(new LandmarkLocation(0, 0, "default Friend Location"));
            uuidToFriendMap.put(uuid, blankFriend);
        }
        // If no existing uuids, generate them
        if(!SharedPrefUtils.hasPubUUID(context)){
            String publicUUID = serverAPI.getNewUUIDTimeout(9);
            String privateUUID = serverAPI.getNewUUIDTimeout(9);
            //if(true){
            if(publicUUID == null || privateUUID == null){
                Utilities.closeAppServerError(context);
                return false;
            }
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
                        Friend constructedFriend = friend.get(9, TimeUnit.SECONDS);
                        if(constructedFriend != null) {
                            uuidToFriendMap.put(uuid, constructedFriend);
                        } else{
                            Log.d("Server Error", "Server unresponsive to update friend in mapping");
                        }
                        // NOTE: FRIEND IS UPDATED
                    } catch (ExecutionException e) {
                        Log.e("Mediator", e.toString());
                    } catch (InterruptedException e) {
                        Log.e("Mediator", e.toString());
                    } catch (TimeoutException e){
                        Log.d("Server Error", "Server took too long to respond to update request for " + uuid);
                    }
                }
                Log.d("Mediator", "Finished Updating round");
                // All friends updated, notify UI by calling the main thread
//                GPSStatus gpsStatus = new GPSStatus(compassActivity);
                if(compassActivity != null) {
                    compassActivity.runOnUiThread(this::updateUI);
                }
                //updateUI();
            } catch(Exception e){
                Log.d("Mediator Error", e.toString());
            }
            }, 0, 1, TimeUnit.SECONDS);
        return true;
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
        boolean crash = false;
        try {
            friend = future.get(9, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            Utilities.closeAppServerError(context);
            crash = true;
        }
        friendIsValid = (friend != null);


        if (friendIsValid) {
            uuidToFriendMap.put(uuid, friend);
            SharedPrefUtils.writeID(context, uuid);
            if (compassActivity != null) {
                compassActivity.addFriendToCompass(String_toUUID(uuid), friend.getName()); // new
            }
            updateUI();
        } else {
            if(!crash) {
                Utilities.showAlert((Activity) context, "invalid uuid");
            }
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
        // Do initial upsert of self to get name in database with default long/lat values
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

    public String getOrGenerateUUID(Context context){
        return publicUUID;
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

    /**
     * Testing method which mocks a location update from the LocationService to the Mediator
     * @param location update
     */
    @VisibleForTesting
    public void mockLocationChange(Location location){
        if(userLocationService != null){
            userLocationService.setMockLocationSource(new MutableLiveData<>(new Pair<>(location.getLatitude(), location.getLongitude())));
        }
        this.userLocation = UserLocation.singleton(location.getLatitude(), location.getLongitude(), location.getLabel());

        try {
            serverAPI.upsertUserAsync(this.publicUUID, serverAPI.formatUpsertJSON(
                    this.privateUUID
                    , this.name
                    , userLocation.getLatitude()
                    , userLocation.getLongitude()))
                    .get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        updateUI();
    }

    /**
     * Testing method which mocks an orientation update from the OrientationService to the Mediator
     * @param degree update
     */
    @VisibleForTesting
    public void mockOrientationChange(Float degree){
        if(orientationService != null){
            orientationService.setMockOrientationSource(new MutableLiveData<>(degree));
        }
        this.userOrientation = degree;
        updateUI();
    }
}

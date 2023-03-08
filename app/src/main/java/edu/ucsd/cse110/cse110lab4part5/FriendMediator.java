package edu.ucsd.cse110.cse110lab4part5;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
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
    private ServerAPI serverAPI = ServerAPI.getInstance();

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();



    String publicUUID;
    String privateUUID;
    String name;

    Location location;

    // TODO server stuff

    public void setCompassActivity(CompassActivity compassActivity) {
        this.compassActivity = compassActivity;
    }

    public static FriendMediator getInstance() {
        if (instance == null) {
            instance = new FriendMediator();
        }
        return instance;
    }

    public void init(Context context){
        java.util.List<String> friendUUIDS = SharedPrefUtils.getAllID(context);
        for(String uuid: friendUUIDS){
            uuidToFriendMap.put(uuid, new Friend("", uuid));
        }
        publicUUID = String.valueOf(SharedPrefUtils.getPubUUID(context));
        privateUUID = String.valueOf(SharedPrefUtils.getPrivUUID(context));
        name = SharedPrefUtils.getName(context);

        executor.scheduleAtFixedRate(() -> {
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
            updateGPSUI();
            updateCompassUI(uuidToFriendMap);
        } else {
            // TODO something like a warning "invalid uuid"
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

    private void updateGPSUI() {
        compassActivity.updateGPSStatus();
        // TODO update CompassActivity. Will this work?
    }

    private void updateCompassUI(Map<String, Friend> uuidToFriendMap) {
        compassActivity.updateFriendsMap(uuidToFriendMap);
        // TODO update CompassActivity. Will this work?
    }

    public void setName(Context context, String name){
        if(!SharedPrefUtils.hasName(context)){
            this.name = name;
            SharedPrefUtils.writeName(context, name);
        }
    }

    public int getOrGenerateUUID(Context context){
        if(SharedPrefUtils.hasPubUUID(context)){
            return SharedPrefUtils.getPubUUID(context);
        } else{
            String publicUUID = serverAPI.getNewUUID();
            String privateUUID = serverAPI.getNewUUID();
            SharedPrefUtils.setPubUUID(context, Integer.valueOf(publicUUID));
            SharedPrefUtils.setPrivUUID(context, Integer.valueOf(privateUUID));
            return Integer.valueOf(publicUUID);
        }
    }

    // TODO get updates from server every few seconds

}

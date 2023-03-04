package edu.ucsd.cse110.cse110lab4part5;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FriendMediator {
    Map<String, Friend> uuidToFriendMap = new HashMap<>();
    private static FriendMediator instance = null;
    private CompassActivity compassActivity;
    private ServerAPI serverAPI = ServerAPI.getInstance();

    // TODO server stuff

    public void setCompassActivity(CompassActivity compassActivity) {
        this.compassActivity = compassActivity;
    }

    public FriendMediator getInstance() {
        if (instance == null) {
            instance = new FriendMediator();
        }
        return instance;
    }

    /*
    Try adding a new friend with the uuid provided. If uuid invalid, do nothing.
    If uuid is valid, add the friend to the HashMap, get their info from the server, then
    update compassUI. Always update CompassUI after checking with the server for the latest info.
    We also update GPS info, so the entire UI is updated after a new friend is added.
     */
    public void addFriend(String uuid) {
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
            getAllServerUpdates();
            updateGPSUI();
            updateCompassUI();
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

    private void getAllServerUpdates() {
        // TODO uuidToFriendMap now have all valid friends. Get their current locations
        // TODO and update this uuidToFriendMap
    }

    private void updateServer() {
        // TODO when new users get their uid and set their name, notify the server
    }

    private void updateGPSUI() {
        compassActivity.updateGPSStatus();
        // TODO update CompassActivity. Will this work?
    }

    private void updateCompassUI() {
        compassActivity.updateFriendsMap();
        // TODO update CompassActivity. Will this work?
    }

    // TODO get updates from server every few seconds

}

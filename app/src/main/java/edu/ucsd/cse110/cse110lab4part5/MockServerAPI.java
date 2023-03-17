package edu.ucsd.cse110.cse110lab4part5;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.VisibleForTesting;
import androidx.annotation.WorkerThread;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.Request;

public class MockServerAPI extends ServerAPI{
    private HashMap<String,String> MockDB=new HashMap<>();
    private volatile static ServerAPI instance = null;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    public static ServerAPI getInstance() {
        if (instance == null) {
            instance = new MockServerAPI();
        }
        return instance;
    }

    @WorkerThread
    private Friend getFriend(String uuid){
        String body =MockDB.get(uuid);
       if(body!=null){
           return Friend.fromJSON(body);
       }
       return null;
    }
    @AnyThread
    public Future<Friend> getFriendAsync(String uuid) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Friend> future = executor.submit(() -> getFriend(uuid));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }
    @WorkerThread
    private String upsertUser(String uuid, String json){
        MockDB.put(uuid,json);
        return json;
    }
    @AnyThread
    public Future<String> upsertUserAsync(String uuid, String json) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() ->upsertUser(uuid,json));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }

    @WorkerThread
    public Boolean uuidExists(String uuid) {
        if(MockDB.get(uuid)==null) return false;
        return true;
    }
    @AnyThread
    public Future<Boolean> uuidExistsAsync(String uuid) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> uuidExists(uuid));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }
    @WorkerThread
    public String deleteFriend(String uuid, String privateCode) {
       MockDB.remove(uuid);
       return uuid;
    }
    @VisibleForTesting
    public Future<String> deleteFriendAsync(String uuid, String privateCode) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> upsertUser(uuid, privateCode));
        return future;
    }

    public String getNewUUID(){
        String uuid;
        while(true){
            uuid = String.valueOf(UserUUID.generate_own_uid());
            boolean exists = true;
            try {
                exists = uuidExistsAsync(uuid).get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(uuid == null){
                Log.d("BadUUIDCall", "Server returned null on get new UUID");
            }
            if(exists == false && uuid != null){
                break;
            }
        }
        Log.d("ServerAPIUUID", uuid);
        return uuid;
    }

}

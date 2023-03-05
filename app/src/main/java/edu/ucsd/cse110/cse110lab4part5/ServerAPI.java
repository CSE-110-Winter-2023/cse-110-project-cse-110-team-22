package edu.ucsd.cse110.cse110lab4part5;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.WorkerThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServerAPI {

    private volatile static ServerAPI instance = null;

    private OkHttpClient client;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");


    public ServerAPI() {
        this.client = new OkHttpClient();
    }

    /**
     * Singleton getinstance
     * @return instance of the ServerAPI
     */
    public static ServerAPI getInstance() {
        if (instance == null) {
            instance = new ServerAPI();
        }
        return instance;
    }

    /**
     * Get a friend from the server
     * @param uuid of the friend
     * @return a friend from the server
     */
    @WorkerThread
    public Friend getFriend(String uuid){
        // URLs cannot contain spaces, so we replace them with %20.
        uuid = uuid.replace(" ", "%20");


        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + uuid)
                .method("GET", null)
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String body = response.body().string();
            if(body.contains("Location not found")){
                Log.i("getFriend", "Friend " + uuid + " not found in database: ");
                return null;
            }
            Log.i("getFriend", "recieved response: " + body);
            return Friend.fromJSON(body); // TODO: from JSON here
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * Async call the server to get a friend's info.
     * @param uuid of the friend
     * @return a future which will contain the friend object upon thread completion
     */
    @AnyThread
    public Future<Friend> getFriendAsync(String uuid) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Friend> future = executor.submit(() -> getFriend(uuid));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }

    /**
     * Upsert a friend from the server
     * @param uuid of the friend
     * @return the http response body
     */
    @WorkerThread
    public String upsertFriend(String uuid, String json){
        // URLs cannot contain spaces, so we replace them with %20.
        uuid = uuid.replace(" ", "%20");
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + uuid)
                .put(body)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String responseBody = response.body().string();
            Log.i("UpsertFriend", "Upserted friend with response: " + responseBody);
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Async call the server to upsert user info
     * @param uuid of the user
     * @return a future which will contain the response body from the server upon thread completion
     */
    @AnyThread
    public Future<String> upsertFriendAsync(String uuid, String json) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> upsertFriend(uuid, json));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }

    /**
     * Check if a uuid exists on the server already
     * @param uuid to check
     * @return boolean true/false for existance, true if no server connection since this is used
     * to determine if a UUID is taken when making a new one, so err on side of caution.
     */
    @WorkerThread
    public boolean uuidTaken(String uuid){
        // URLs cannot contain spaces, so we replace them with %20.
        uuid = uuid.replace(" ", "%20");


        Request request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + uuid)
                .method("GET", null)
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String body = response.body().string();
            if(body.contains("Location not found")){
                Log.i("UUID check", uuid + " not found in server");
                return false;
            }
            Log.i("UUID check", "UUID exists in server: " + body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    /**
     * Async check if uuid exists in the server already
     * @param uuid to check
     * @return a future which will contain the result of checking the server
     */
    @AnyThread
    public Future<Boolean> uuidTakenAsync(String uuid) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> uuidTaken(uuid));

        // We can use future.get(1, SECONDS) to wait for the result.
        return future;
    }


}

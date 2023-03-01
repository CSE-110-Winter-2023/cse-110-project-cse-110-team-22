package edu.ucsd.cse110.cse110lab4part5;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static ServerAPI provide() {
        if (instance == null) {
            instance = new ServerAPI();
        }
        return instance;
    }

    /**
     * An example of sending a GET request to the server.
     *
     * The /echo/{msg} endpoint always just returns {"message": msg}.
     */
    public void echo(String msg) {
        // URLs cannot contain spaces, so we replace them with %20.
        msg = msg.replace(" ", "%20");

        Request request = new Request.Builder()
                .url("https://sharednotes.goto.ucsd.edu/echo/" + msg)
                .method("GET", null)
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String body = response.body().string();
            Log.i("ECHO", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Friend getFriend(String uuid){
        // URLs cannot contain spaces, so we replace them with %20.
        uuid = uuid.replace(" ", "%20");


        Request request = new Request.Builder()
                .url("https://sharednotes.goto.ucsd.edu/notes/" + uuid)
                .method("GET", null)
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String body = response.body().string();
            if(body.contains("Note not found.")){
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

    public void upsertFriend(String uuid, String json){

        // URLs cannot contain spaces, so we replace them with %20.
        uuid = uuid.replace(" ", "%20");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String finalTitle = uuid;
        executor.submit(()->{
            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url("https://sharednotes.goto.ucsd.edu/notes/" + finalTitle)
                    .put(body)
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                assert response.body() != null;
                String responseBody = response.body().string();
                Log.i("UpsertFriend", "Upserted friend with response: " + responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}

package edu.ucsd.cse110.cse110lab4part5;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerAPI {

// TODO: Implement the API using OkHttp!
    // TODO: Read the docs: https://square.github.io/okhttp/
    // TODO: Read the docs: https://sharednotes.goto.ucsd.edu/docs

    private volatile static ServerAPI instance = null;

    private OkHttpClient client;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");


    public NoteAPI() {
        this.client = new OkHttpClient();
    }

    public static NoteAPI provide() {
        if (instance == null) {
            instance = new NoteAPI();
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

        var request = new Request.Builder()
                .url("https://sharednotes.goto.ucsd.edu/echo/" + msg)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            Log.i("ECHO", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Note getNote(String title){
        // URLs cannot contain spaces, so we replace them with %20.
        title = title.replace(" ", "%20");


        var request = new Request.Builder()
                .url("https://sharednotes.goto.ucsd.edu/notes/" + title)
                .method("GET", null)
                .build();

        try (var response = client.newCall(request).execute()) {
            assert response.body() != null;
            var body = response.body().string();
            if(body.contains("Note not found.")){
                Log.i("getNote", "note not in database");
                return null;
            }
            Log.i("getNote", body);
            return Note.fromJSON(body);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public void upsertNote(String title, String json){

        // URLs cannot contain spaces, so we replace them with %20.
        title = title.replace(" ", "%20");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String finalTitle = title;
        executor.submit(()->{
            RequestBody body = RequestBody.create(json, JSON);
            var request = new Request.Builder()
                    .url("https://sharednotes.goto.ucsd.edu/notes/" + finalTitle)
                    .put(body)
                    .build();

            try (var response = client.newCall(request).execute()) {
                assert response.body() != null;
                var responseBody = response.body().string();
                Log.i("UpsertNote", responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}

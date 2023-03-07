package edu.ucsd.cse110.cse110lab4part5;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ServerTests {

    ServerAPI serverAPI = ServerAPI.getInstance();
    Friend friend1;

    // updated values for friend 1 that share UUIDs
    Friend friend1New;
    String friend1PrivateCode;
    Friend friend2;
    String friend2PrivateCode;

    /**
     * Testing helper method that returns an unused UUID
     * @return
     */
    private String getNewUUID(){
        String uuid;
        while(true){
            uuid = String.valueOf(UserUUID.generate_own_uid());
            boolean exists = true;
            try {
                exists = serverAPI.uuidExistsAsync(uuid).get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(exists == false){
                break;
            }
        }
        return uuid;
    }


//    @Before
//    public void setUp() {
//        String publicUUID = getNewUUID();
//        String privateUUID = getNewUUID();
//
//        friend1 = new Friend("Julia", publicUUID);
//        friend1.setLocation(new LandmarkLocation(32.88014354083708, -117.2318005216365, "Julia's Location"));
//        friend1PrivateCode = privateUUID;
//
//        friend1New = new Friend("Owen", publicUUID);
//        friend1New.setLocation(new LandmarkLocation(100, -100, "Owen's Location"));
//
//
//        friend2 = new Friend("Lisa", publicUUID);
//        friend2.setLocation(new LandmarkLocation(32.87986803114829,  -117.24313628066673, "Lisa's Location"));
//        friend2PrivateCode = privateUUID;
//    }


    /**
     * Check that the api can upsert a friend who does not exist in the database and get it back
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testUpsertNew() throws ExecutionException, InterruptedException {
        Future<String> response = serverAPI.upsertUserAsync(friend1.uuid
                , serverAPI.formatUpsertJSON(friend1PrivateCode
                        , friend1.name
                        , friend1.getLocation().getLatitude()
                        , friend1.getLocation().getLongitude()));

        String responseString;
        try {
            responseString = response.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assert(!serverAPI.badUpsertResponse(responseString));

        Friend serverFriend = serverAPI.getFriendAsync(friend1.uuid).get();

        assert(friend1.equals(serverFriend));

        serverAPI.deleteFriendAsync(friend1.uuid, friend1PrivateCode);

    }


    /**
     * Tests that the server will successfully replace an existing friend upon upsert (new data with
     * same public/private key
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testUpsertReplace() throws ExecutionException, InterruptedException {
        // upsert first friend
        Future<String> response = serverAPI.upsertUserAsync(friend1.uuid
                , serverAPI.formatUpsertJSON(friend1PrivateCode
                        , friend1.name
                        , friend1.getLocation().getLatitude()
                        , friend1.getLocation().getLongitude()));

        // upsert friend again with values from friend 2
        Future<String> response2 = serverAPI.upsertUserAsync(friend1New.uuid
                , serverAPI.formatUpsertJSON(friend1PrivateCode
                        , friend1New.name
                        , friend1New.getLocation().getLatitude()
                        , friend1New.getLocation().getLongitude()));

        // check that second upsert successfully replaced the first friend
        String responseString2;
        try {
            responseString2 = response2.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Friend serverFriend2 = serverAPI.getFriendAsync(friend1New.uuid).get();

        assert(friend1New.equals(serverFriend2));
        serverAPI.deleteFriendAsync(friend1New.uuid, friend1PrivateCode);

    }

    @Test
    public void testUpsertBadPrivateKey() throws ExecutionException, InterruptedException {
        // upsert first friend
        Future<String> response = serverAPI.upsertUserAsync(friend1.uuid
                , serverAPI.formatUpsertJSON(friend1PrivateCode
                        , friend1.name
                        , friend1.getLocation().getLatitude()
                        , friend1.getLocation().getLongitude()));

        String responseString;
        try {
            responseString = response.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assert(responseString.contains("200"));

        // upsert second friend with same public UUID, wrong private code
        Future<String> response2 = serverAPI.upsertUserAsync(friend1.uuid
                , serverAPI.formatUpsertJSON(friend2PrivateCode
                        , friend1.name
                        , friend1.getLocation().getLatitude()
                        , friend1.getLocation().getLongitude()));

        // TODO: check that server rejected the upsert
        String responseString2;
        try {
            responseString2 = response2.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // make sure original data remains after rejected call
        Friend serverFriend = serverAPI.getFriendAsync(friend1.uuid).get();

        assert(friend1.equals(serverFriend));
        serverAPI.deleteFriendAsync(friend1New.uuid, friend1PrivateCode);

    }

    @Test
    public void testUpsertBadJSON() throws ExecutionException, InterruptedException {
        // upsert with bad JSON
        Future<String> response = serverAPI.upsertUserAsync(friend1.uuid
                , "LOL BAD JSON");

        String responseString;
        try {
            responseString = response.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assert(!responseString.contains("200"));

        // make sure data was not added to server
        Friend serverFriend = serverAPI.getFriendAsync(friend1.uuid).get();

        assert(!responseString.contains("200"));

    }


    public void testGetNonExistingFriend() throws ExecutionException, InterruptedException {
        // TODO: Figure out later: Check for invalid uuid passed
        Friend serverFriend = serverAPI.getFriendAsync(friend1.uuid).get();

        //assert(!responseString.contains("200"));
    }

    @Test
    public void ihatejson(){
        String json = "";
        String JSON_STRING = "{\"employee\":{\"name\":\"Abhishek Saini\",\"salary\":65000}}";
        Friend friend = null;
        try {
            JSONObject obj = new JSONObject(JSON_STRING);
            // fetch JSONObject named employee
            JSONObject employee = obj.getJSONObject("employee");
            // get employee name and salary
            String name = employee.getString("name");
            String salary = employee.getString("salary");
            // set employee name and salary in TextView's

            JSONObject responseJSON = new JSONObject(json);
            //String name = responseJSON.getString("label");
            String uuid = responseJSON.getString("public_code");
            double latitude = responseJSON.getDouble("latitude");
            double longitude = responseJSON.getDouble("longitude");
            friend = new Friend(name, uuid);
            friend.setLocation(new LandmarkLocation(latitude, longitude, name + "'s location"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}

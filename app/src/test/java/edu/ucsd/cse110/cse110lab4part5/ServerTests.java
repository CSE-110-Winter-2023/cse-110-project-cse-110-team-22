package edu.ucsd.cse110.cse110lab4part5;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ServerTests {

    ServerAPI serverAPI = ServerAPI.getInstance();
    Friend friend1;
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


    @Before
    public void setUp() {
        String publicUUID = getNewUUID();
        String privateUUID = getNewUUID();

        Friend friend1 = new Friend("Julia", publicUUID);
        friend1.setLocation(new LandmarkLocation(32.88014354083708, -117.2318005216365, "Julia's Location"));
        friend1PrivateCode = privateUUID;

        Friend friend2 = new Friend("Lisa", publicUUID);
        friend2.setLocation(new LandmarkLocation(32.87986803114829,  -117.24313628066673, "Lisa's Location"));
        friend2PrivateCode = privateUUID;
    }



    @Test
    public void testUpsertNew(){
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

        assert(responseString.contains(""));

        //Friend serverFriend = serverAPI.getFriendAsync(friend1.uuid).get();

    }

    @Test
    public void testUpsertReplace(){

    }

    @Test
    public void testUpsertBadPrivateKey(){

    }

    @Test
    public void testUpsertBadJSON(){

    }


    public void testGetExistingFriend(){

    }

    public void testGetNonExistingFriend(){

    }




}

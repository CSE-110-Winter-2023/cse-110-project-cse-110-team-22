package edu.ucsd.cse110.cse110lab4part5;

import org.junit.Before;
import org.junit.Test;

public class ServerTests {

    ServerAPI serverAPI = ServerAPI.getInstance();
    @Before
    public void setUp() {
        Friend friend1 = new Friend("Julia", "823342789");
        //friend1.setLocation(new LandmarkLocation("Julia's Location"));
    }

    @Test
    public void testUpsertNormal(){

    }

    @Test
    public void testUpsertError(){

    }

    public void testGetError(){

    }




}

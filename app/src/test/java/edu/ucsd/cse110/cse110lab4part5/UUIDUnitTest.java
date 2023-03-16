package edu.ucsd.cse110.cse110lab4part5;

import static org.junit.Assert.assertEquals;
import static edu.ucsd.cse110.cse110lab4part5.UserUUID.String_toUUID;
import static edu.ucsd.cse110.cse110lab4part5.UserUUID.uuid_toString;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class UUIDUnitTest {

    @Test
    public void Test_UUID_Conversion_1(){
        int uuid = 0;
        String uuid_str = uuid_toString(uuid);
        String goal = "000000000";
        System.out.println(uuid_str);
        assertEquals(uuid_str, goal);
        assertEquals(uuid, String_toUUID(goal));
    }

    @Test
    public void Test_UUID_Conversion_2(){
        int uuid = 1;
        String uuid_str = uuid_toString(uuid);
        String goal = "000000001";
        System.out.println(uuid_str);
        assertEquals(uuid_str, goal);
        assertEquals(uuid, String_toUUID(goal));
    }

    public void Test_UUID_Conversion_3(){
        int uuid = 123456789;
        String uuid_str = uuid_toString(uuid);
        String goal = "123456789";
        assertEquals(uuid_str, goal);
        assertEquals(uuid, String_toUUID(goal));
    }

    public void Test_UUID_Conversion_4(){
        int uuid = 98765;
        String uuid_str = uuid_toString(uuid);
        String goal = "000098765";
        assertEquals(uuid_str, goal);
        assertEquals(uuid, String_toUUID(goal));
    }
    @Test
    public void Test_UUID_Conversion_5(){
        String uuid = "c81d4e2e-bcf2-11e6-869b-7df92533d2db";
        int goal = 843331641;
        assertEquals(goal, String_toUUID(uuid));
    }

    @Test
    public void Test_UUID_Conversion_6(){
        String uuid = "test";
        int goal = 3556498;
        assertEquals(goal, String_toUUID(uuid));
    }
}

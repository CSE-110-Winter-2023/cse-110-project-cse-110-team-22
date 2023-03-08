package edu.ucsd.cse110.cse110lab4part5;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class IDPrefTests {
    private static final String uuidPrefFile = "uuid_pref";

    private static final String uuidFriends = "uuidFriends";
    private static final String uuidPublic = "uuidPub";
    private static final String uuidPrivate = "uuidPrivate";
    private static final String myName = "myName";

    @Test
    public void emptyTest(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);

        List<String> publicID = SharedPrefUtils.getAllID(context);
        boolean hasName = SharedPrefUtils.hasName(context);
        String name = SharedPrefUtils.getName(context);
        assertEquals(0, publicID.size());
        assertFalse(hasName);
        assertNull(name);

        assertTrue(preferences.getString(uuidFriends, "").equals(""));
        assertTrue(preferences.getString(myName, "").equals(""));
    }

    @Test
    public void addTest(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);

        // write tests
        assertFalse(SharedPrefUtils.hasName(context));
        SharedPrefUtils.writeName(context, "Max");
        SharedPrefUtils.writeID(context, "100");
        assertTrue(preferences.getString(uuidFriends, "").equals("100"));
        List<String> pubID = SharedPrefUtils.getAllID(context);
        assertEquals(1, pubID.size());
        assertEquals("100", pubID.get(0));
        assertTrue(SharedPrefUtils.getName(context).equals("Max"));
        assertTrue(SharedPrefUtils.hasName(context));

        SharedPrefUtils.writeID(context, "101");
        assertTrue(preferences.getString(uuidFriends, "").equals("100\u0000101"));
        pubID = SharedPrefUtils.getAllID(context);
        assertEquals(2, pubID.size());
        assertEquals("100", pubID.get(0));
        assertEquals("101", pubID.get(1));

        // change name
        SharedPrefUtils.writeName(context, "Jon");
        assertTrue(SharedPrefUtils.hasName(context));
        assertTrue(SharedPrefUtils.getName(context).equals("Jon"));

        SharedPrefUtils.clearIDPrefs(context);
        emptyTest();
    }

    @Test
    public void rmFromNoneOneTest(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);
        SharedPrefUtils.rmID(context, "100");
        assertTrue(preferences.getString(uuidFriends, "").equals(""));
        SharedPrefUtils.writeID(context, "100");
        SharedPrefUtils.rmID(context, "100");
        assertTrue(preferences.getString(uuidFriends, "").equals(""));
        emptyTest();
    }

    @Test
    public void rmFromManyTest(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);
        SharedPrefUtils.writeID(context, "100");
        SharedPrefUtils.writeID(context, "101");
        SharedPrefUtils.writeID(context, "102");
        assertTrue(preferences.getString(uuidFriends, "").equals("100\u0000101\u0000102"));
        SharedPrefUtils.rmID(context, "101");
        assertTrue(preferences.getString(uuidFriends, "").equals("100\u0000102"));
        List<String> pubID = SharedPrefUtils.getAllID(context);
        assertEquals(2, pubID.size());
        assertEquals("100", pubID.get(0));
        assertEquals("102", pubID.get(1));
        SharedPrefUtils.clearIDPrefs(context);
        emptyTest();
    }

    @Test
    public void setMyPubID(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);
        assertFalse(SharedPrefUtils.hasPubUUID(context));
        SharedPrefUtils.setPubUUID(context, 12345);
        assertTrue(SharedPrefUtils.hasPubUUID(context));
        assertEquals(12345, SharedPrefUtils.getPubUUID(context));
        assertEquals(12345, preferences.getInt(uuidPublic, -1));
        SharedPrefUtils.clearIDPrefs(context);
        emptyTest();
    }

    @Test
    public void setMyPrivID(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);
        assertFalse(SharedPrefUtils.hasPrivUUID(context));
        SharedPrefUtils.setPrivUUID(context, 12345);
        assertTrue(SharedPrefUtils.hasPrivUUID(context));
        assertEquals(12345, SharedPrefUtils.getPrivUUID(context));
        assertEquals(12345, preferences.getInt(uuidPrivate, -1));
        SharedPrefUtils.clearIDPrefs(context);
        emptyTest();
    }
}
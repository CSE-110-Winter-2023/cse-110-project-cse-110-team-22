package edu.ucsd.cse110.cse110lab4part5;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class IDPrefTests {
    private static final String uuidPrefFile = "uuid_pref";
    private static final String uuidPublic = "uuidPub";
    private static final String uuidPrivate = "uuidPriv";
    private static final String myName = "myName";

    @Test
    public void emptyTest(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);

        List<String> publicID = SharedPrefUtils.getAllID(context, true);
        List<String> privateID = SharedPrefUtils.getAllID(context, false);
        boolean hasName = SharedPrefUtils.hasName(context);
        String name = SharedPrefUtils.getName(context);
        assertEquals(0, publicID.size());
        assertEquals(0, privateID.size());
        assertFalse(hasName);
        assertNull(name);

        assertTrue(preferences.getString(uuidPublic, "").equals(""));
        assertTrue(preferences.getString(uuidPrivate, "").equals(""));
        assertTrue(preferences.getString(myName, "").equals(""));
    }

    @Test
    public void addTest(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);

        // write tests
        SharedPrefUtils.writeName(context, "Max");
        SharedPrefUtils.writeID(context, "100", true);
        SharedPrefUtils.writeID(context, "200", false);
        assertTrue(preferences.getString(uuidPublic, "").equals("100"));
        assertTrue(preferences.getString(uuidPrivate, "").equals("200"));
        List<String> pubID = SharedPrefUtils.getAllID(context, true);
        List<String> privID = SharedPrefUtils.getAllID(context, false);
        assertEquals(1, pubID.size());
        assertEquals("100", pubID.get(0));
        assertEquals(1, privID.size());
        assertEquals("200", privID.get(0));
        assertTrue(SharedPrefUtils.getName(context).equals("Max"));

        SharedPrefUtils.writeID(context, "101", true);
        SharedPrefUtils.writeID(context, "201", false);
        assertTrue(preferences.getString(uuidPublic, "").equals("100\u0000101"));
        assertTrue(preferences.getString(uuidPrivate, "").equals("200\u0000201"));
        pubID = SharedPrefUtils.getAllID(context, true);
        privID = SharedPrefUtils.getAllID(context, false);
        assertEquals(2, pubID.size());
        assertEquals("100", pubID.get(0));
        assertEquals("101", pubID.get(1));
        assertEquals(2, privID.size());
        assertEquals("200", privID.get(0));
        assertEquals("201", privID.get(1));

        SharedPrefUtils.clearIDPrefs(context);
        emptyTest();
    }

    @Test
    public void removeTests(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);
        SharedPrefUtils.writeID(context, "100", true);
        SharedPrefUtils.writeID(context, "101", true);
        SharedPrefUtils.writeID(context, "102", true);
        SharedPrefUtils.writeID(context, "200", false);
        SharedPrefUtils.writeID(context, "201", false);
        SharedPrefUtils.writeID(context, "202", false);
        assertTrue(preferences.getString(uuidPublic, "").equals("100\u0000101\u0000102"));
        assertTrue(preferences.getString(uuidPrivate, "").equals("200\u0000201\u0000202"));
        SharedPrefUtils.rmID(context, "101", true);
        SharedPrefUtils.rmID(context, "201", false);
        assertTrue(preferences.getString(uuidPublic, "").equals("100\u0000102"));
        assertTrue(preferences.getString(uuidPrivate, "").equals("200\u0000202"));
        List<String> pubID = SharedPrefUtils.getAllID(context, true);
        List<String> privID = SharedPrefUtils.getAllID(context, false);
        assertEquals(2, pubID.size());
        assertEquals(2, privID.size());
        assertEquals("100", pubID.get(0));
        assertEquals("102", pubID.get(1));
        assertEquals("200", privID.get(0));
        assertEquals("202", privID.get(1));
    }
}
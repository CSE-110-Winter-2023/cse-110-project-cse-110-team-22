package edu.ucsd.cse110.cse110lab4part5;

import static android.content.Context.MODE_PRIVATE;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class GPSPrefTest {
    private static final String gpsPrefFile = "gps_pref";
    private static final String GPSTime = "GPSTime";

    @Test
    public void emptyGPS(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(gpsPrefFile, MODE_PRIVATE);
        assertEquals(-1, preferences.getLong(GPSTime, -1));
        assertEquals(-1, SharedPrefUtils.getLastGPSTime(context));
    }

    @Test
    public void addGPS(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(gpsPrefFile, MODE_PRIVATE);
        SharedPrefUtils.storeLastGPSTime(context, 10101010);
        assertEquals(10101010, preferences.getLong(GPSTime, -1));
        assertEquals(10101010, SharedPrefUtils.getLastGPSTime(context));
        SharedPrefUtils.clearGPSPref(context);
        emptyGPS();
    }
}

package edu.ucsd.cse110.cse110lab4part5;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

@RunWith(AndroidJUnit4.class)
public class GPSTest {

    private GPSStatus test_gpsStatus;
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        test_gpsStatus = new GPSStatus(context);
    }

    @Test
    public void testMockNotHasGPSStatus_1() {
    test_gpsStatus.storeLastActiveTime(System.currentTimeMillis()/60000);
    ScheduledFuture<?> future = test_gpsStatus.setMockNotHaveGPSStatus(2, 5);
    assertFalse(test_gpsStatus.hasGPSService);
    try {
        future.get();
    } catch (Exception e) {
    }
    assertEquals("0 m.", test_gpsStatus.timeSpanDisconnected);
}

    @Test
    public void testMockNotHasGPSStatus_2() {
        test_gpsStatus.storeLastActiveTime(System.currentTimeMillis()/60000);
        ScheduledFuture<?> future = test_gpsStatus.setMockNotHaveGPSStatus(6, 10);
        assertFalse(test_gpsStatus.hasGPSService);
        try {
            future.get();
        } catch (Exception e) {
        }
        assertEquals("1 m.", test_gpsStatus.timeSpanDisconnected);
    }


}

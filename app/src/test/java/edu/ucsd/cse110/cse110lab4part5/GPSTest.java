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
import org.robolectric.RobolectricTestRunner;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

@RunWith(RobolectricTestRunner.class)
public class GPSTest {

    private GPSStatusMock test_gpsStatus;
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        test_gpsStatus = new GPSStatusMock(context);
    }

    @Test
    public void testMockHasGPSStatus_1() {
        //test_gpsStatus.storeLastActiveTime(System.currentTimeMillis()/60000);
        ScheduledFuture<?> future = test_gpsStatus.setMockHaveGPSStatus(8, 10);
        assertTrue(test_gpsStatus.hasGPSService);
        try {
            future.get();
        } catch (Exception e) {
        }
        assertTrue(test_gpsStatus.hasGPSService);
        assertEquals("0 m.", test_gpsStatus.timeSpanDisconnected);

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
    assertFalse(test_gpsStatus.hasGPSService);
    assertEquals("0 m.", test_gpsStatus.timeSpanDisconnected);
}

    @Test
    public void testMockNotHasGPSStatus_2() {
        test_gpsStatus.storeLastActiveTime(System.currentTimeMillis()/60000);
        ScheduledFuture<?> future = test_gpsStatus.setMockNotHaveGPSStatus(8, 10);
        assertFalse(test_gpsStatus.hasGPSService);
        try {
            future.get();
        } catch (Exception e) {
        }
        assertFalse(test_gpsStatus.hasGPSService);
        assertEquals("1 m.", test_gpsStatus.timeSpanDisconnected);
    }


}

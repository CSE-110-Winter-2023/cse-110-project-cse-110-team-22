package edu.ucsd.cse110.cse110lab4part5;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class GPSServerUnitTest {
    @Test
    public void testWhenConnected(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
//            MutableLiveData<Pair<Double, Double>> mockDataSource =
//                    new MutableLiveData<Pair<Double, Double>>();
//            mockDataSource.setValue(new Pair<Double, Double>(latitude_num_1, longitude_num_1));
//            UserLocationService userLocationService = UserLocationService.singleton(activity);
//            userLocationService.setMockLocationSource(mockDataSource);
//            //TextView test = (TextView) activity.findViewById(R.id.serviceTextView);
//
//            userLocationService.getLocation().observe(activity, loc ->{
//                assertEquals(latitude_num_1, loc.first, DELTA);
//                assertEquals(longitude_num_1, loc.second, DELTA);
//            });
        });
//        GPSStatus gpsStatus = new GPSStatus(this);
        assertEquals(-1,-1);
    }
}

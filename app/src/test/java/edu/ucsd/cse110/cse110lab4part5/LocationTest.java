package edu.ucsd.cse110.cse110lab4part5;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.*;

import android.Manifest;
import android.app.Application;
import android.util.Pair;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

@RunWith(RobolectricTestRunner.class)
public class LocationTest {
    final double DELTA = 0.01;
    double latitude_num_1;
    double longitude_num_1;
    double latitude_num_2;
    double longitude_num_2;

    /* setup for all tests below */
    @Before
    public void setUp() {
        latitude_num_1 = 90.0;
        longitude_num_1 = 0.3;
        latitude_num_2 = -0.01;
        longitude_num_2 = 45;
    }

    /* Test with mock value = longitude_num_1 and latitude_num_1 */
    @Test
    public void Location_Test_1() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            MutableLiveData<Pair<Double, Double>> mockDataSource =
                    new MutableLiveData<Pair<Double, Double>>();
            mockDataSource.setValue(new Pair<Double, Double>(latitude_num_1, longitude_num_1));
            UserLocationService userLocationService = UserLocationService.singleton(activity);
            userLocationService.setMockLocationSource(mockDataSource);
            //TextView test = (TextView) activity.findViewById(R.id.serviceTextView);

            userLocationService.getLocation().observe(activity, loc ->{
                assertEquals(latitude_num_1, loc.first, DELTA);
                assertEquals(longitude_num_1, loc.second, DELTA);
            });
        });
    }

    /* Test with mock value = longitude_num_2 and latitude_num_2 */
    @Test
    public void Location_Test_2() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            MutableLiveData<Pair<Double, Double>> mockDataSource =
                    new MutableLiveData<Pair<Double, Double>>();
            mockDataSource.setValue(new Pair<Double, Double>(latitude_num_2, longitude_num_2));
            UserLocationService userLocationService = UserLocationService.singleton(activity);
            userLocationService.setMockLocationSource(mockDataSource);
            //TextView test = (TextView) activity.findViewById(R.id.serviceTextView);

            userLocationService.getLocation().observe(activity, loc ->{
                assertEquals(latitude_num_2, loc.first, DELTA);
                assertEquals(longitude_num_2, loc.second, DELTA);
            });
        });
    }
}

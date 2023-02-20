package edu.ucsd.cse110.cse110lab4part5;

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

    @Test
    public void Location_Test() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            MutableLiveData<Pair<Double, Double>> mockDataSource =
                    new MutableLiveData<Pair<Double, Double>>();
            Double first = (double) 123;
            Double second = (double) 121;
            mockDataSource.setValue(new Pair<Double, Double>(first, second));
            UserLocationService userLocationService = UserLocationService.singleton(activity);
            userLocationService.setMockLocationSource(mockDataSource);
            //TextView test = (TextView) activity.findViewById(R.id.serviceTextView);

            userLocationService.getLocation().observe(activity, loc ->{
                assertEquals(Double.toString(loc.first), Double.toString(first));
                assertEquals(Double.toString(loc.second), Double.toString(second));
            });
        });

    }
}

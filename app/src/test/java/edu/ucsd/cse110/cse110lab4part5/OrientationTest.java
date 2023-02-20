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
import android.graphics.drawable.GradientDrawable;
import android.util.Pair;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;


@RunWith(RobolectricTestRunner.class)
public class OrientationTest {

    final float DELTA = 0.01f;
    float orientation_num_1;
    float orientation_num_2;

    /* setup for all tests below */
    @Before
    public void setUp() {
        orientation_num_1 = 90.0f;
        orientation_num_2 = 0.01f;
    }

    /* Test with mock value = orientation_num_1 */
    @Test
    public void Orientation_Test_1(){
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            MutableLiveData<Float> mockDataSource = new MutableLiveData<>();
            mockDataSource.setValue(orientation_num_1);
            UserOrientationService userOrientationService = UserOrientationService.singleton(activity);
            userOrientationService.setMockOrientationSource(mockDataSource);

            userOrientationService.getOrientation().observe(activity, dir ->{
                assertEquals(orientation_num_1, dir, DELTA);
            });
        });
    }

    /* Test with mock value = orientation_num_2 */
    @Test
    public void Orientation_Test_2(){
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            MutableLiveData<Float> mockDataSource = new MutableLiveData<>();
            mockDataSource.setValue(orientation_num_2);
            UserOrientationService userOrientationService = UserOrientationService.singleton(activity);
            userOrientationService.setMockOrientationSource(mockDataSource);

            userOrientationService.getOrientation().observe(activity, dir ->{
                assertEquals(orientation_num_2, dir, DELTA);
            });
        });
    }
}

package edu.ucsd.cse110.cse110lab4part5;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(RobolectricTestRunner.class)
public class BDDTestUS4 {
    // Try commenting out this rule and running the test, it will fail!
    ServerAPI serverAPI = MockServerAPI.getInstance();

    Location mockLocation = new LandmarkLocation(32.88014354083708, -117.2318005216365, "mock_user_location");


    @Before
    public void setup() throws ExecutionException, InterruptedException {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test
    public void BDDTestUS4(){

        // init the friend mediator from main activity
        ActivityScenario initScenario = ActivityScenario.launch(MainActivity.class);
        initScenario.moveToState(Lifecycle.State.CREATED);
        initScenario.moveToState(Lifecycle.State.STARTED);
        FriendMediator.getInstance().setMockServerAPI(serverAPI);


        initScenario.onActivity(activity -> {
            // init mediator and mock the location and orientation services
        });

        // start from the input name activity
        ActivityScenario scenario = ActivityScenario.launch(input_name.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            EditText text = activity.findViewById(R.id.enter_name);
            text.setText("Julia");

            Button continue_click = activity.findViewById(R.id.to_show_uid);
            continue_click.performClick();

            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(user_uid_showing.class, actual).create().get();
            String publicUUID = ((TextView)activity.findViewById(R.id.your_uid)).getText().toString();

            Button toCompass = activity.findViewById(R.id.to_compass_activity);
            toCompass.performClick();
            Intent actual_2 = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(CompassActivity.class, actual_2).create().get();

            FriendMediator.getInstance().mockLocationChange(mockLocation);

            try {
                Friend serverSelf = serverAPI.getFriendAsync(publicUUID).get();
                assertEquals("Julia", serverSelf.name);
                assertEquals(mockLocation.getLatitude(), serverSelf.getLocation().getLatitude(), .1);
                assertEquals(mockLocation.getLongitude(), serverSelf.getLocation().getLongitude(), .1);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        });

    }


}
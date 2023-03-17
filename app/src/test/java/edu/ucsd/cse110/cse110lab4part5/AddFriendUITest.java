package edu.ucsd.cse110.cse110lab4part5;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

@RunWith(RobolectricTestRunner.class)
public class AddFriendUITest {
    @Before
    public void setUp() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }
    @Test
    public void add_friend_UI_test() {
        // init the friend mediator from main activity
        ActivityScenario initScenario = ActivityScenario.launch(MainActivity.class);
        initScenario.moveToState(Lifecycle.State.CREATED);
        initScenario.moveToState(Lifecycle.State.STARTED);

        initScenario.onActivity(activity -> {
            FriendMediator.getInstance().init((MainActivity) activity);
            FriendMediator.getInstance().setMockServerAPI(MockServerAPI.getInstance());


        });

        // Actually start the testing
        ActivityScenario scenario = ActivityScenario.launch(input_name.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            EditText text = activity.findViewById(R.id.enter_name);
            text.setText("Julia");
            //May have further test after more implementation
            assertEquals(text.getText().toString(), "Julia");

            Button continue_click = activity.findViewById(R.id.to_show_uid);
            continue_click.performClick();

            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();

            activity = Robolectric.buildActivity(user_uid_showing.class, actual).create().get();
            assertEquals(activity.getClass(), user_uid_showing.class);

            Button add_friend = activity.findViewById(R.id.add_friend);
            add_friend.performClick();

            actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(entering_friend_uid.class, actual).create().get();
            assertEquals(activity.getClass(), entering_friend_uid.class);

            Button go_back = activity.findViewById(R.id.back_to_your_uid);
            go_back.performClick();

            actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(user_uid_showing.class, actual).create().get();
            assertEquals(activity.getClass(), user_uid_showing.class);

            Button to_compass = activity.findViewById(R.id.to_compass_activity);
            to_compass.performClick();

//            actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
//            activity = Robolectric.buildActivity(CompassActivity.class, actual).create().get();
//            assertEquals(activity.getClass(), CompassActivity.class);
        });

    }
}

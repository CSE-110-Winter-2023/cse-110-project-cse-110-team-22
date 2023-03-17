package edu.ucsd.cse110.cse110lab4part5;


import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

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
public class BDDTestUS3 {
    ServerAPI serverAPI = MockServerAPI.getInstance();
    Friend friend1;

    String publicUUID;
    String privateUUID;

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test
    public void BDDTestUS3SendName() {
        String name = "Julia";


        // init the friend mediator from main activity
        ActivityScenario initScenario = ActivityScenario.launch(MainActivity.class);
        initScenario.moveToState(Lifecycle.State.CREATED);
        initScenario.moveToState(Lifecycle.State.STARTED);

        initScenario.onActivity(activity -> {
            FriendMediator.getInstance().init((MainActivity) activity);
            FriendMediator.getInstance().setMockServerAPI(serverAPI);

        });

        // start the actual scenario form the input_name class
        ActivityScenario scenario = ActivityScenario.launch(input_name.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);

        scenario.onActivity(activity -> {
            EditText text = activity.findViewById(R.id.enter_name);
            text.setText(name);

            // now the app should send our name to the server
            Button continue_click = activity.findViewById(R.id.to_show_uid);
            continue_click.performClick();

            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(user_uid_showing.class, actual).create().get();

            publicUUID = String.valueOf(SharedPrefUtils.getPubUUID(activity));
            privateUUID = String.valueOf(SharedPrefUtils.getPrivUUID(activity));

            // call the server to see any data associated with our UUID
            Friend serverSelf;
            try {
                serverSelf = serverAPI.getFriendAsync(publicUUID).get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // check that name was successfully sent to server
            assertNotNull(serverSelf);
            assert (serverSelf.name.equals(name));

            // cleanup
            serverAPI.deleteFriendAsync(publicUUID, privateUUID);

        });

    }

}

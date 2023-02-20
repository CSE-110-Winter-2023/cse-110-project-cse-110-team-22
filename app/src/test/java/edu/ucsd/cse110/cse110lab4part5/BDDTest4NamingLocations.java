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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)

public class BDDTest4NamingLocations {
    /**
     * 1. Julia opens the app.
     * 2. Julia consents to the location tracking
     * 3. Julia enters the coordinates of her friends and family
     * 4. Julia clicks submit and was taken to the compass view page
     * 5. The compass view will show the direction of the inputted locations with default names and default labels
     * 6. Julia closes the app fully
     */
    @Test
    public void NamingLocationScenarioTest() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario scenario = ActivityScenario.launch(InputCoordinateActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            EditText famHomeLat = (EditText) activity.findViewById(R.id.latitude_family);
            EditText famHomeLong = (EditText) activity.findViewById(R.id.longitude_family);
            EditText famHomeLabel = (EditText) activity.findViewById(R.id.label_family);
            famHomeLat.setText("100");
            famHomeLong.setText("200");
            famHomeLabel.setText("Tom's home");

            EditText ownHomeLat = (EditText) activity.findViewById(R.id.latitude_home);
            EditText ownHomeLong = (EditText) activity.findViewById(R.id.longitude_home);
            EditText ownHomeLabel = (EditText) activity.findViewById(R.id.label_home);
            ownHomeLat.setText("00");
            ownHomeLong.setText("00");
            ownHomeLabel.setText("John's home");

            EditText friendHomeLat = (EditText) activity.findViewById(R.id.latitude_friend);
            EditText friendHomeLong = (EditText) activity.findViewById(R.id.longitude_friend);
            EditText friendHomeLabel = (EditText) activity.findViewById(R.id.label_friend);
            friendHomeLat.setText("-100");
            friendHomeLong.setText("-200");
            friendHomeLabel.setText("Peter's home");

            Button submitButton = activity.findViewById(R.id.submit);
            submitButton.performClick();
            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            CompassActivity compassActivity = Robolectric.buildActivity(CompassActivity.class, actual).create().get();

            TextView actualFamHomeLabel = (TextView) compassActivity.findViewById(R.id.family_label_text);
            TextView actualOwnHomeLabel = (TextView)compassActivity.findViewById(R.id.home_label_text);
            TextView actualFriendHomeLabel = (TextView)compassActivity.findViewById(R.id.friend_label_text);
            assertEquals("Tom's home",actualFamHomeLabel.getText());
            assertEquals("John's home",actualOwnHomeLabel.getText());
            assertEquals("Peter's home",actualFriendHomeLabel.getText());

            ActivityController<InputCoordinateActivity> controller = Robolectric.buildActivity(InputCoordinateActivity.class).create().start().resume();
            controller.stop();
        });
    }
}

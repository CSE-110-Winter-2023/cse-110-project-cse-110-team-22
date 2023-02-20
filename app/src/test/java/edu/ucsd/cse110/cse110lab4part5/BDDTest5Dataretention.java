package edu.ucsd.cse110.cse110lab4part5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
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
public class BDDTest5Dataretention {
    /**
     * Julia opens the app
     * she has given consent to the location permission
     * she enters the coordinates of her friends
     * she shutdown her phone and reopen the app
     * she continue filling out her family house coordinate and her apartment coordinate
     * she clicks submit and was taken to the compass view page
     * The compass view page correctly shows everything she entered
     * she closes the app fully
     */
    @Test
    public void test_on_data_retention(){
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario scenario = ActivityScenario.launch(InputCoordinateActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            EditText famHomeLat = (EditText) activity.findViewById(R.id.latitude_family);
            EditText famHomeLong = (EditText) activity.findViewById(R.id.longitude_family);
            famHomeLat.setText("10");
            famHomeLong.setText("10");

            EditText ownHomeLat = (EditText) activity.findViewById(R.id.latitude_home);
            EditText ownHomeLong = (EditText) activity.findViewById(R.id.longitude_home);
            ownHomeLat.setText("20");
            ownHomeLong.setText("20");
            ActivityController<InputCoordinateActivity> controller = Robolectric.buildActivity(InputCoordinateActivity.class).create().start().resume();
            controller.stop();
            controller.start().resume();

            EditText friendHomeLat = (EditText) activity.findViewById(R.id.latitude_friend);
            EditText friendHomeLong = (EditText) activity.findViewById(R.id.longitude_friend);
            friendHomeLat.setText("-100");
            friendHomeLong.setText("-200");

            Button submitButton = activity.findViewById(R.id.submit);
            submitButton.performClick();
            Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            CompassActivity compassActivity = Robolectric.buildActivity(CompassActivity.class, actual).create().get();
            int imageViewId = R.id.friend;
            ImageView imageView = (ImageView) compassActivity.findViewById(imageViewId);
            float expected = 45f;
            compassActivity.updateCircleAngle(imageViewId, expected);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
            float test_float = layoutParams.circleAngle;
            assertEquals(expected, test_float, 0.01);









        });
    }
}

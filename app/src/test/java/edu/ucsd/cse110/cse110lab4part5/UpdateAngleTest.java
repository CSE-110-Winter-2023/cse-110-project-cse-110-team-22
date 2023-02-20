package edu.ucsd.cse110.cse110lab4part5;

import static android.app.PendingIntent.getActivity;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
public class UpdateAngleTest{

    @Test
    public void testUpdateCircleAngle() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario scenario = ActivityScenario.launch(InputCoordinateActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            EditText famHomeLat = (EditText) activity.findViewById(R.id.latitude_family);
            EditText famHomeLong = (EditText) activity.findViewById(R.id.longitude_family);
            famHomeLat.setText("100");
            famHomeLong.setText("200");

            EditText ownHomeLat = (EditText) activity.findViewById(R.id.latitude_home);
            EditText ownHomeLong = (EditText) activity.findViewById(R.id.longitude_home);
            ownHomeLat.setText("00");
            ownHomeLong.setText("00");

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

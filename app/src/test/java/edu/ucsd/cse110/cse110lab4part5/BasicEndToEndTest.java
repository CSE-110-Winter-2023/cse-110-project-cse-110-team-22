package edu.ucsd.cse110.cse110lab4part5;

import static android.app.PendingIntent.getActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class BasicEndToEndTest{

    /**
     * Basic end to end test that starts from blank app startup (no data in shared prefs),
     * enters 3 locations, click submit, then leaves the app
     */
    @Test
    public void testEndtoEnd() {
        float homeDegree = 8.380f;
        float familyDegree = 83.401f;
        float friendDegree = 65.178f;
        float northDegree = 0f;

        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            SharedPrefUtils.clearLocationSharedPreferences(activity);
            Intent inputIntent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            InputCoordinateActivity inputActivity = Robolectric.buildActivity(InputCoordinateActivity.class, inputIntent).create().get();

            EditText famHomeLat = (EditText) inputActivity.findViewById(R.id.latitude_family);
            EditText famHomeLong = (EditText) inputActivity.findViewById(R.id.longitude_family);
            famHomeLat.setText("-20");
            famHomeLong.setText("20");

            EditText ownHomeLat = (EditText) inputActivity.findViewById(R.id.latitude_home);
            EditText ownHomeLong = (EditText) inputActivity.findViewById(R.id.longitude_home);
            ownHomeLat.setText("50");
            ownHomeLong.setText("50");

            EditText friendHomeLat = (EditText) inputActivity.findViewById(R.id.latitude_friend);
            EditText friendHomeLong = (EditText) inputActivity.findViewById(R.id.longitude_friend);
            friendHomeLat.setText("30");
            friendHomeLong.setText("-30");





            Button submitButton = inputActivity.findViewById(R.id.submit);
            submitButton.performClick();

            Intent compassIntent = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            CompassActivity compassActivity = Robolectric.buildActivity(CompassActivity.class, compassIntent).create().get();
            UserLocation userLocation = UserLocation.singleton(32, -117, "You");

            //Landmark locations
            List<Location> locations = SharedPrefUtils.readAllLocations(compassActivity);
            LandmarkLocation homeLocation = (LandmarkLocation) locations.get(0);
            homeLocation.setIconNum(2);
            LandmarkLocation friendLocation = (LandmarkLocation) locations.get(1);
            friendLocation.setIconNum(1);
            LandmarkLocation familyLocation = (LandmarkLocation) locations.get(2);
            familyLocation.setIconNum(0);

            //north
            LandmarkLocation northLocation = new LandmarkLocation(90, 10, "North_Pole");
            northLocation.setIconNum(3);

            List<Location> locList = new ArrayList<>();
            locList.add(familyLocation);
            locList.add(friendLocation);
            locList.add(homeLocation);
            locList.add(northLocation);

            compassActivity.update(0, LocationUtils.computeAllAngles(userLocation, locList));


            boolean x = (assertIconRotation(compassActivity, R.id.friend, friendDegree, .5f));
            boolean y = (assertIconRotation(compassActivity, R.id.home, homeDegree, .5f));
            boolean z = (assertIconRotation(compassActivity, R.id.familyhouse, familyDegree, .5f));
            boolean a = (assertIconRotation(compassActivity, R.id.letter_n, northDegree, .5f));

            // rotate clockwise 90 degrees

        });
    }

    /**
     * Helper
     * @param compassActivity
     * @param imageViewId
     * @param expected
     * @param delta
     * @return
     */
    boolean assertIconRotation(CompassActivity compassActivity, int imageViewId, float expected, float delta){
        ImageView imageView = (ImageView) compassActivity.findViewById(imageViewId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        float test_float = layoutParams.circleAngle;
        float max = expected + delta;
        float min = expected - delta;

        return(test_float >= min && test_float <= max);
    }
}

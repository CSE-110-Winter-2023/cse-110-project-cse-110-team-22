package edu.ucsd.cse110.cse110lab4part5;


import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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

import org.junit.Before;
import org.junit.Rule;
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
public class BDDTestUS1 {
    // Try commenting out this rule and running the test, it will fail!
    ServerAPI serverAPI = ServerAPI.getInstance();
    Friend friend1;

    // updated values for friend 1 that share UUIDs
    Friend friend1New;
    String friend1PrivateCode;
    Friend friend2;
    String friend2PrivateCode;

    Location mockLocation = new LandmarkLocation(32.88014354083708, -117.2318005216365, "mock_user_location");


    @Before
    public void setup() throws ExecutionException, InterruptedException {
        String publicUUID1 = serverAPI.getNewUUID();
        String privateUUID1 = serverAPI.getNewUUID();
        String publicUUID2 = serverAPI.getNewUUID();
        String privateUUID2 = serverAPI.getNewUUID();
        friend1 = new Friend("Bill", publicUUID1);
        friend1.setLocation(new LandmarkLocation(32.905088554461926, -117.12111266246087, "Bill's Location"));
        friend1PrivateCode = privateUUID1;

        friend2 = new Friend("Peter", publicUUID2);
        friend2.setLocation(new LandmarkLocation(32.86804329909429,  -117.25037729620158, "Peter's Location")); // scripps institute
        friend2PrivateCode = privateUUID2;

        Future<String> response = serverAPI.upsertUserAsync(friend1.uuid
                , serverAPI.formatUpsertJSON(friend1PrivateCode
                        , friend1.name
                        , friend1.getLocation().getLatitude()
                        , friend1.getLocation().getLongitude()));

        Future<String> response_2 = serverAPI.upsertUserAsync(friend2.uuid
                , serverAPI.formatUpsertJSON(friend2PrivateCode
                        , friend2.name
                        , friend2.getLocation().getLatitude()
                        , friend2.getLocation().getLongitude()));

        try {
            response.get();
            response_2.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test
    public void BDDTestUS1(){

        // init the friend mediator from main activity
        ActivityScenario initScenario = ActivityScenario.launch(MainActivity.class);
        initScenario.moveToState(Lifecycle.State.CREATED);
        initScenario.moveToState(Lifecycle.State.STARTED);

        initScenario.onActivity(activity -> {
            // init mediator and mock the location and orientation services
            FriendMediator.getInstance().init((MainActivity) activity);


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

            Button add_friend = activity.findViewById(R.id.add_friend);
            add_friend.performClick();

            Intent actual_1 = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(entering_friend_uid.class, actual_1).create().get();
            EditText enter_id = activity.findViewById(R.id.enter_friend_id_blank);
            enter_id.setText(friend1.uuid);
            Button add_button = activity.findViewById(R.id.add_friend_to_database);
            add_button.performClick();
            enter_id.setText(friend2.uuid);
            add_button.performClick();

            Button backButton = activity.findViewById(R.id.back_to_your_uid);
            backButton.performClick();
            Intent actual_2 = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(user_uid_showing.class, actual_2).create().get();

            Button toCompass = activity.findViewById(R.id.to_compass_activity);
            toCompass.performClick();
            Intent actual_3 = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(CompassActivity.class, actual_3).create().get();

            FriendMediator.getInstance().mockLocationChange(mockLocation);

            // assert angles in the compass UI exist and are set properly
            assertTrue(assertTextViewRotation((CompassActivity) activity, Integer.valueOf((friend1.uuid)), 74.946434f, .5f));
            assertTrue(assertTextViewRotation((CompassActivity) activity, Integer.valueOf((friend2.uuid)), -127.790596f, .5f));


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
    boolean assertTextViewRotation(CompassActivity compassActivity, int imageViewId, float expected, float delta){
        TextView textView = compassActivity.findViewById(imageViewId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
        float test_float = layoutParams.circleAngle;
        float max = expected + delta;
        float min = expected - delta;

        return(test_float >= min && test_float <= max);
    }



}
package edu.ucsd.cse110.cse110lab4part5;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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
public class BDDTestUS6zoom_in_out {
    ServerAPI serverAPI = MockServerAPI.getInstance();
    Friend friend1;

    // updated values for friend 1 that share UUIDs
    Friend friend1New;
    String friend1PrivateCode;
    Friend friend2;
    String friend2PrivateCode;


    @Before
    public void setup() throws ExecutionException, InterruptedException {
        String publicUUID1 = serverAPI.getNewUUID();
        String privateUUID1 = serverAPI.getNewUUID();
        String publicUUID2 = serverAPI.getNewUUID();
        String privateUUID2 = serverAPI.getNewUUID();
        friend1 = new Friend("Julia", publicUUID1);
        friend1.setLocation(new LandmarkLocation(32.88014354083708, -117.2318005216365, "Julia's Location"));
        friend1PrivateCode = privateUUID1;

        friend1New = new Friend("Owen", publicUUID1);
        friend1New.setLocation(new LandmarkLocation(100, -100, "Owen's Location"));


        friend2 = new Friend("Lisa", publicUUID2);
        friend2.setLocation(new LandmarkLocation(32.87986803114829,  -117.24313628066673, "Lisa's Location"));
        friend2PrivateCode = privateUUID2;

        Future<String> response = serverAPI.upsertUserAsync(friend1.uuid
                , serverAPI.formatUpsertJSON(friend1PrivateCode
                        , friend1.name
                        , friend1.getLocation().getLatitude()
                        , friend1.getLocation().getLongitude()));

        Future<String> response_2 = serverAPI.upsertUserAsync(friend1New.uuid
                , serverAPI.formatUpsertJSON(friend1PrivateCode
                        , friend1New.name
                        , friend1New.getLocation().getLatitude()
                        , friend1New.getLocation().getLongitude()));

        String responseString;
        try {
            responseString = response.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        Friend serverFriend = serverAPI.getFriendAsync(friend1.uuid).get();
        //serverAPI.deleteFriendAsync(friend1.uuid, friend1PrivateCode);
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Test
    public void BDDTest6GZoom(){
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario initScenario = ActivityScenario.launch(MainActivity.class);
        initScenario.moveToState(Lifecycle.State.CREATED);
        initScenario.moveToState(Lifecycle.State.STARTED);

        initScenario.onActivity(activity -> {
            FriendMediator.getInstance().init((MainActivity) activity);
            FriendMediator.getInstance().setMockServerAPI(serverAPI);


        });

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

            Button add_friend = activity.findViewById(R.id.to_compass_activity);
            add_friend.performClick();

            Intent actual_1 = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
            activity = Robolectric.buildActivity(CompassActivity.class, actual_1).create().get();
            ImageView ring12 = activity.findViewById(R.id.ring12);
            assertEquals(ring12.getVisibility(), VISIBLE);
            ImageView ring14 = activity.findViewById(R.id.ring14);
            assertEquals(ring14.getVisibility(), INVISIBLE);
            ImageView ring13 = activity.findViewById(R.id.ring13);
            assertEquals(ring13.getVisibility(), INVISIBLE);
            ImageView ring23 = activity.findViewById(R.id.ring23);
            assertEquals(ring23.getVisibility(), INVISIBLE);
            ImageButton zoom_in = activity.findViewById(R.id.zoom_in);
            zoom_in.performClick();
            assertEquals(ring12.getVisibility(), INVISIBLE);
            assertEquals(ring13.getVisibility(), VISIBLE);
            assertEquals(ring23.getVisibility(), VISIBLE);
            assertEquals(ring14.getVisibility(), INVISIBLE);
            zoom_in.performClick();
            assertEquals(ring12.getVisibility(), VISIBLE);
            assertEquals(ring13.getVisibility(), INVISIBLE);
            assertEquals(ring23.getVisibility(), INVISIBLE);
            assertEquals(ring14.getVisibility(), VISIBLE);
            ImageButton zoom_out = activity.findViewById(R.id.zoom_out);
            zoom_out.performClick();
            assertEquals(ring12.getVisibility(), INVISIBLE);
            assertEquals(ring13.getVisibility(), VISIBLE);
            assertEquals(ring23.getVisibility(), VISIBLE);
            assertEquals(ring14.getVisibility(), INVISIBLE);
            zoom_out.performClick();
            assertEquals(ring12.getVisibility(), VISIBLE);
            assertEquals(ring13.getVisibility(), INVISIBLE);
            assertEquals(ring23.getVisibility(), INVISIBLE);
            assertEquals(ring14.getVisibility(), INVISIBLE);
            zoom_out.performClick();
            assertEquals(ring12.getVisibility(), INVISIBLE);
            assertEquals(ring13.getVisibility(), INVISIBLE);
            assertEquals(ring23.getVisibility(), INVISIBLE);
            assertEquals(ring14.getVisibility(), INVISIBLE);







        });

    }
}
package edu.ucsd.cse110.cse110lab4part5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.*;

import android.Manifest;
import android.app.Application;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;


@RunWith(RobolectricTestRunner.class)
public class InputUnitTest {
    @Test
    public void test_latitude_home() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.onActivity(activity -> {
            TextView textView = (TextView) activity.findViewById(R.id.latitude_home);
            assertEquals(textView.getHint(),"Latitude");
        });
    }


    @Test
    public void test_longitude_home() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                TextView textView = (TextView) activity.findViewById(R.id.longitude_home);
                assertEquals(textView.getHint(),"Longitude");
            });
        }
    }
    @Test
    public void test_label_family() {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        try(ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(activity -> {
                TextView textView = (TextView) activity.findViewById(R.id.label_family);
                assertEquals(textView.getHint(),"Family Home");
            });
        }
    }

    @Test
    public void test_input_latitude(){
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityScenario scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            TextView lat_home = (TextView) activity.findViewById(R.id.latitude_home);
            lat_home.setText("123");
            assertEquals("123", lat_home.getText().toString());
            TextView lat_family = (TextView) activity.findViewById(R.id.latitude_family);
            lat_family.setText("132");
            assertEquals("132", lat_family.getText().toString());
            TextView lat_friend = (TextView) activity.findViewById(R.id.latitude_friend);
            lat_friend.setText("321");
            assertEquals("321", lat_friend.getText().toString());

        });
    }

    @Test
    public void test_input_longitude(){
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication app = Shadows.shadowOf(application);
        app.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityScenario scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            TextView long_home = (TextView) activity.findViewById(R.id.longitude_home);
            long_home.setText("123");
            assertEquals("123", long_home.getText().toString());
            TextView long_family = (TextView) activity.findViewById(R.id.longitude_family);
            long_family.setText("132");
            assertEquals("132", long_family.getText().toString());
            TextView long_friend = (TextView) activity.findViewById(R.id.longitude_friend);
            long_friend.setText("321");
            assertEquals("321", long_friend.getText().toString());

        });
    }



}
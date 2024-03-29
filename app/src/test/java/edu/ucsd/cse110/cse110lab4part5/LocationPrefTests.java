package edu.ucsd.cse110.cse110lab4part5;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocationPrefTests {
    private static final String locationPreferencesFile = "location_preferences";
    private static final String locationLabelsFile = "location_labels";

    @Test
    public void cleanTest(){
        // initialize context
        Context context = ApplicationProvider.getApplicationContext();
        // Create locations
        Location loc1 = new LandmarkLocation(10, 10, "home");
        // Shared Preference stuff
        SharedPrefUtils.writeLocation(context, loc1);
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        SharedPrefUtils.clearLocationSharedPreferences(context);
        String delimitedString = preferences.getString(locationLabelsFile, "Not present");
        assertTrue(delimitedString.equals("Not present"));
    }

    @After
    public void cleanUp(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        SharedPrefUtils.clearLocationSharedPreferences(context);
    }

    @Test
    public void emptyPref(){
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        String delimitedString = preferences.getString(locationLabelsFile, "No Labels");
        assertTrue(delimitedString.equals("No Labels"));
        assertFalse(SharedPrefUtils.hasStoredLocations(context));
    }

    @Test
    public void addSinglePref(){
        // initialize context
        Context context = ApplicationProvider.getApplicationContext();
        // Create locations
        Location loc1 = new LandmarkLocation(10, 10, "home");
        // Shared Preference stuff
        SharedPrefUtils.writeLocation(context, loc1);
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        String delimitedString = preferences.getString(locationLabelsFile, "");
        String lat_string = preferences.getString("home_lat", "");
        String long_string = preferences.getString("home_long", "");
        //Assertions
        assertTrue(delimitedString.equals("home"));
        assertEquals(Double.parseDouble(lat_string), 10, 0);
        assertEquals(Double.parseDouble(long_string), 10, 0);
        assertTrue(SharedPrefUtils.hasStoredLocations(context));
    }

    @Test
    public void addMultiplePref(){
        // initialize context
        Context context = ApplicationProvider.getApplicationContext();
        // Create locations
        Location loc1 = new LandmarkLocation(10, 10, "home");
        Location loc2 = new LandmarkLocation(15, 20, "school");
        Location loc3 = new LandmarkLocation(21, 69, "work");
        // Shared Preference stuff
        SharedPrefUtils.writeLocation(context, loc1);
        SharedPrefUtils.writeLocation(context, loc2);
        SharedPrefUtils.writeLocation(context, loc3);
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        String delimitedString = preferences.getString(locationLabelsFile, "");
        String lat_home = preferences.getString("home_lat", "");
        String long_home = preferences.getString("home_long", "");
        String lat_school = preferences.getString("school_lat", "");
        String long_school = preferences.getString("school_long", "");
        String lat_work = preferences.getString("work_lat", "");
        String long_work = preferences.getString("work_long", "");
        //Assertions
        assertTrue(delimitedString.equals("home\u0000school\u0000work"));
        assertEquals(Double.parseDouble(lat_home), 10, 0);
        assertEquals(Double.parseDouble(long_home), 10, 0);
        assertEquals(Double.parseDouble(lat_school), 15, 0);
        assertEquals(Double.parseDouble(long_school), 20, 0);
        assertEquals(Double.parseDouble(lat_work), 21, 0);
        assertEquals(Double.parseDouble(long_work), 69, 0);
    }

    @Test
    public void readTest(){
        Context context = ApplicationProvider.getApplicationContext();
        // Create locations
        Location loc1 = new LandmarkLocation(10, 10, "home");
        // Shared Preference stuff
        SharedPrefUtils.writeLocation(context, loc1);
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        Location ret_loc = SharedPrefUtils.readLocation(context, "home");
        assertEquals(loc1.getLatitude(), ret_loc.getLatitude(), 0);
        assertEquals(loc1.getLongitude(), ret_loc.getLongitude(), 0);
        assertEquals(loc1.getLabel(), ret_loc.getLabel());
    }

    @Test
    public void remLocTest(){
        // initialize context
        Context context = ApplicationProvider.getApplicationContext();
        // Create locations
        Location loc1 = new LandmarkLocation(10, 10, "home");
        Location loc2 = new LandmarkLocation(15, 20, "school");
        Location loc3 = new LandmarkLocation(21, 69, "work");
        // Shared Preference stuff
        SharedPrefUtils.writeLocation(context, loc1);
        SharedPrefUtils.writeLocation(context, loc2);
        SharedPrefUtils.writeLocation(context, loc3);
        SharedPrefUtils.rmLocationLabels(context, "school");
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        String delimitedString = preferences.getString(locationLabelsFile, "");
        String lat_home = preferences.getString("home_lat", "Not found");
        String long_home = preferences.getString("home_long", "Not found");
        String lat_school = preferences.getString("school_lat", "Not found");
        String long_school = preferences.getString("school_long", "Not found");
        String lat_work = preferences.getString("work_lat", "Not found");
        String long_work = preferences.getString("work_long", "Not found");
        //Assertions
        assertTrue(delimitedString.equals("home\u0000work"));
        assertEquals(Double.parseDouble(lat_home), 10, 0);
        assertEquals(Double.parseDouble(long_home), 10, 0);
        assertTrue(lat_school.equals("Not found"));
        assertTrue(long_school.equals("Not found"));
        assertEquals(Double.parseDouble(lat_work), 21, 0);
        assertEquals(Double.parseDouble(long_work), 69, 0);
    }

    @Test
    public void replaceLabelOne(){
        // initialize context
        Context context = ApplicationProvider.getApplicationContext();
        // Create locations
        Location loc1 = new LandmarkLocation(10, 10, "home");
        // Shared Preference stuff
        SharedPrefUtils.writeLocation(context, loc1);

        //edit loc1
        loc1.setLatitude(20);
        loc1.setLongitude(21);
        SharedPrefUtils.writeLocation(context, loc1);

        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        String delimitedString = preferences.getString(locationLabelsFile, "");
        String lat_string = preferences.getString("home_lat", "");
        String long_string = preferences.getString("home_long", "");
        //Assertions
        assertTrue(delimitedString.equals("home"));
        assertEquals(Double.parseDouble(lat_string), 20, 0);
        assertEquals(Double.parseDouble(long_string), 21, 0);
        assertTrue(SharedPrefUtils.hasStoredLocations(context));
    }

    @Test
    public void replaceLabelTwo() {
        // initialize context
        Context context = ApplicationProvider.getApplicationContext();
        // Create locations
        Location loc1 = new LandmarkLocation(10, 10, "home");
        Location loc2 = new LandmarkLocation(25, 30, "home_friend");
        // Shared Preference stuff
        SharedPrefUtils.writeLocation(context, loc1);
        SharedPrefUtils.writeLocation(context, loc2);

        //edit loc1
        loc1.setLatitude(20);
        loc1.setLongitude(21);
        SharedPrefUtils.writeLocation(context, loc1);

        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        String delimitedString = preferences.getString(locationLabelsFile, "");
        String lat_loc1 = preferences.getString("home_lat", "");
        String long_loc1 = preferences.getString("home_long", "");
        String lat_loc2 = preferences.getString("home_friend_lat", "");
        String long_loc2 = preferences.getString("home_friend_long", "");
        //Assertions
        assertTrue(delimitedString.equals("home_friend\u0000home"));
        assertEquals(Double.parseDouble(lat_loc1), 20, 0);
        assertEquals(Double.parseDouble(long_loc1), 21, 0);
        assertEquals(Double.parseDouble(lat_loc2), 25, 0);
        assertEquals(Double.parseDouble(long_loc2), 30, 0);

        assertTrue(SharedPrefUtils.hasStoredLocations(context));
    }

    @Test
    public void replaceLabelMore() {
        // initialize context
        Context context = ApplicationProvider.getApplicationContext();
        // Create locations
        Location loc1 = new LandmarkLocation(-10, 22, "school");
        Location loc2 = new LandmarkLocation(10, 10, "home");
        Location loc3 = new LandmarkLocation(25, 30, "home_friend");
        // Shared Preference stuff
        SharedPrefUtils.writeLocation(context, loc1);
        SharedPrefUtils.writeLocation(context, loc2);
        SharedPrefUtils.writeLocation(context, loc3);

        //edit loc1
        loc2.setLatitude(20);
        loc2.setLongitude(21);
        SharedPrefUtils.writeLocation(context, loc2);

        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        String delimitedString = preferences.getString(locationLabelsFile, "");
        String lat_loc1 = preferences.getString("school_lat", "");
        String long_loc1 = preferences.getString("school_long", "");
        String lat_loc2 = preferences.getString("home_lat", "");
        String long_loc2 = preferences.getString("home_long", "");
        String lat_loc3 = preferences.getString("home_friend_lat", "");
        String long_loc3 = preferences.getString("home_friend_long", "");
        //Assertions
        assertTrue(delimitedString.equals("school\u0000home_friend\u0000home"));
        assertEquals(Double.parseDouble(lat_loc1), -10, 0);
        assertEquals(Double.parseDouble(long_loc1), 22, 0);
        assertEquals(Double.parseDouble(lat_loc2), 20, 0);
        assertEquals(Double.parseDouble(long_loc2), 21, 0);
        assertEquals(Double.parseDouble(lat_loc3), 25, 0);
        assertEquals(Double.parseDouble(long_loc3), 30, 0);

        assertTrue(SharedPrefUtils.hasStoredLocations(context));
    }
}
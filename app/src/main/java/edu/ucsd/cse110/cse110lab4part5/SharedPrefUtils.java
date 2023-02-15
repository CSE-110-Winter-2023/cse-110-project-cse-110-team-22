package edu.ucsd.cse110.cse110lab4part5;

import static android.content.Context.MODE_PRIVATE;

import static java.util.Arrays.asList;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class SharedPrefUtils {

    /*
    public static List<LandmarkLocation> readLocations(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("location_preferences", MODE_PRIVATE);
        List<String> labels =
    }


     */

    /**
     * adds locations to existingLocations in shared_preferences
     * @param context
     * @param location
     */
    public static void writeLocation(Context context, Location location) {
        SharedPreferences preferences = context.getSharedPreferences("location_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String label = location.getLabel();
        String existingLocations = preferences.getString("locations", "");
        editor.putString("locations", (existingLocations + "\u0000" + location.getLabel()));

        String latLabel = label + "_lat";
        String longLabel = label + "_long";

        editor.putString(latLabel, String.valueOf(location.getLatitude()));
        editor.putString(longLabel, String.valueOf(location.getLongitude()));

    }

    /**
     *
     * @param label
     * @return
     */
    public static Location getLocation(Context context, String label){
        SharedPreferences preferences = context.getSharedPreferences("location_preferences", MODE_PRIVATE);
        double la = Double.parseDouble(preferences.getString(label+"_lat", ""));
        double lo = Double.parseDouble(preferences.getString(label+"_long", ""));
        return new LandmarkLocation(la, lo, label);
    }

    public static List<String> loadLocationLabels(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("location_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String deliminatedString = preferences.getString("existingLocations", "");
        return Arrays.asList(deliminatedString.split("\u0000", -1));

    }
}
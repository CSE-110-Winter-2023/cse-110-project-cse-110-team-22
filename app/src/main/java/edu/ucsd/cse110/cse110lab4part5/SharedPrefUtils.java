package edu.ucsd.cse110.cse110lab4part5;

import static android.content.Context.MODE_PRIVATE;

import static java.util.Arrays.asList;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.location.LocationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPrefUtils {

    private static final String locationPreferencesFile = "location_preferences";
    private static final String locationLabelsFile = "location_labels";



    /**
     *
     * @param context of the requester (ex some Activity)
     * @return list of all locations stored in SharedPreferences
     */
    public static List<Location> readAllLocations(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        List<Location> locations = new ArrayList<>();
        List<String> labels = readLocationLabels(context);
        for(String label : labels){
            locations.add(readLocation(context, label));
        }
        return locations;
    }

    /***
     *
     * Clears the SharedPreferences of the app storing locations removing their data and allowing the
     * user or a tester to launch the app and return to the inputUI screen.
     * @param context of the requester (ex some Activity)
     */
    public static void clearLocationSharedPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     *
     * @param context of the requester (ex some Activity)
     * @return true if there are locations stored in SharedPreferences, false otherwise
     */
    public static boolean hasStoredLocations(Context context){
        if(readLocationLabels(context).size() == 0){
            return false;
        }
        return true;
    }

    /**
     * adds locations to existingLocations in shared_preferences
     * @param context of the requester (ex some Activity)
     * @param location object to write to SharedPreferences
     */
    public static void writeLocation(Context context, Location location) {
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String label = location.getLabel();
        String existingLocations = preferences.getString(locationLabelsFile, "");
        // first location added
        if(existingLocations.equals("")){
            editor.putString(locationLabelsFile, (location.getLabel()));
        } else{
            // not first location
            editor.putString(locationLabelsFile, (existingLocations + "\u0000" + location.getLabel()));
        }

        String latLabel = label + "_lat";
        String longLabel = label + "_long";

        editor.putString(latLabel, String.valueOf(location.getLatitude()));
        editor.putString(longLabel, String.valueOf(location.getLongitude()));
        editor.commit();

    }

    /**
     * @param context of the requester (ex some Activity)
     * @param label of the location stored in SharedPreferences
     * @return
     */
    public static Location readLocation(Context context, String label){
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        double la = Double.parseDouble(preferences.getString(label+"_lat", ""));
        double lo = Double.parseDouble(preferences.getString(label+"_long", ""));
        return new LandmarkLocation(la, lo, label);
    }

    /**
     *
     * @param context of the requester (ex some Activity)
     * @return list of all labels of locations stored in SharedPreferences
     */
    public static List<String> readLocationLabels(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String deliminatedString = preferences.getString(locationLabelsFile, "");
        return Arrays.asList(deliminatedString.split("\u0000", -1));

    }
}
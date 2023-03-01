package edu.ucsd.cse110.cse110lab4part5;

import static android.content.Context.MODE_PRIVATE;

import static java.util.Arrays.asList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.location.LocationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class SharedPrefUtils {

    private static final String locationPreferencesFile = "location_preferences";
    private static final String locationLabelsFile = "location_labels";
    private static final String uuidPrefFile = "uuid_pref";
    private static final String uuidFile = "uuids";

    // Location methods
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
        if(hasStoredLocations(context)){
            Log.d("SharedPrefUtils", "cleared Shared Preferences Locations");
            SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        } else{
            Log.d("SharedPrefUtils", "attempted to clear empty Shared Preferences Locations");
        }
    }

    /**
     *
     * @param context of the requester (ex some Activity)
     * @return true if there are locations stored in SharedPreferences, false otherwise
     */
    public static boolean hasStoredLocations(Context context){
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile, MODE_PRIVATE);
        String delimitedString = preferences.getString(locationLabelsFile, "");
        if(delimitedString.length() == 0){
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
        if (!existingLocations.equals("")){
            for (String s: existingLocations.split("\u0000")) {
                if (s.equals(label)) {
                    rmLocationLabels(context, label);
                }
            }
        }
        existingLocations = preferences.getString(locationLabelsFile, "");
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
        String delimitedString = preferences.getString(locationLabelsFile, "");
        return Arrays.asList(delimitedString.split("\u0000", -1));
    }
    /**
     *
     *@paramcontextof the requester (ex some Activity)
     *@paramlabelof the location to be removed
     */
    public static void rmLocationLabels(Context context, String label){
        SharedPreferences preferences = context.getSharedPreferences(locationPreferencesFile,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(label+"_lat");
        editor.remove(label+"_long");
        String delimitedString = preferences.getString(locationLabelsFile, "");
        String newLocations = "";
        for (String s: delimitedString.split("\u0000")){
            if (s.equals(label)){
                continue;
            }
            if (newLocations.length() != 0){
                newLocations = newLocations + "\u0000";
            }
            newLocations = newLocations + s;
        }
        if (newLocations.equals("")){
            editor.clear();
        }
        else {
            editor.putString(locationLabelsFile, newLocations);
        }
        editor.commit();
    }

    // UUID Methods
    public static void writeID(Context context, String id){
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String idString = preferences.getString(uuidFile, "");
        // check if id is already present
        if (hasID(context, id)){
            return;
        }
        idString = idString + "/u0000" + id;
        editor.putString(uuidFile, idString);
        editor.commit();
    }
    public static void rmID(Context context, String id){
        // check if id is present
        if (hasID(context, id)){
            SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            String idString = preferences.getString(uuidFile, "");
            String newIdString = "";
            for (String s:idString.split("/u0000")){
                if (s.equals(id)){
                    continue;
                };
                newIdString = newIdString + s;
            }
            if (newIdString.equals("")){
                editor.remove(uuidFile);
            }
        }
    }

    public static List<String> getAllID(Context context){
        // need to implement
        ArrayList<String> ids = new ArrayList<>();
        return ids;
    }

    // helper

    /**
     * Checks for presence of ID
     * @param context
     * @param id
     * @return
     */
    private static boolean hasID(Context context, String id){
        if (id == null){
            throw new NullPointerException();
        }
        if (id == ""){
            throw new IllegalArgumentException();
        }
        SharedPreferences preferences = context.getSharedPreferences(uuidPrefFile, MODE_PRIVATE);
        String idString = preferences.getString(uuidFile, "");
        if(id.length() == 0){
            return false;
        }
        for (String s:idString.split("/u0000")){
            if (s.equals(id)){
                return true;
            };
        }
        return false;
    }
}
package edu.ucsd.cse110.cse110lab4part5;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class Friend {
    Location myLocation;
    final String name;
    final String uuid;

    public Friend(String name, String uuid){
        this.name = name;
        this.uuid = uuid;
    }
    public void setLocation(Location l){
        if (l == null) {
            throw new NullPointerException();
        }
        myLocation = l;
    }
    public Location getLocation() {
        return myLocation;
    }

    /**
     * Given a JSON string formatted like in the global server, try to construct a Friend object
     * @param json string from server
     * @return Friend from parsed json info, null if bad parse given
     */
    public static Friend fromJSON(String json) {
        JSONObject responseJSON = null;
        Friend friend = null;
        try {
            responseJSON = new JSONObject(json);
            String name = responseJSON.getString("label");
            String uuid = responseJSON.getString("public_code");
            double latitude = responseJSON.getDouble("latitude");
            double longitude = responseJSON.getDouble("longitude");
            friend = new Friend(name, uuid);
            friend.setLocation(new LandmarkLocation(latitude, longitude, name + "'s location"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return friend;//return new Gson().fromJson(json, Note.class);
    }



}

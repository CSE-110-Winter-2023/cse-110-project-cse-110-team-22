package edu.ucsd.cse110.cse110lab4part5;

import com.google.gson.Gson;

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

    // TODO
    public static Friend fromJSON(String json) {
        return new Friend("", "");
        //return new Gson().fromJson(json, Note.class);
    }

}

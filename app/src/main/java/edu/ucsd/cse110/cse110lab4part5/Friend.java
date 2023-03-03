package edu.ucsd.cse110.cse110lab4part5;

import com.google.gson.Gson;

public class Friend {
    Location myLocation;
    String name;
    final String uuid;

    public Friend(String name, String uuid){
        this.name = name;
        this.uuid = uuid;
        myLocation = null;
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

    public void setName(String name) {this.name = name;};

    public String getName() {return this.name;}

    public String getUuid() {return this.uuid;}

    // TODO
    public static Friend fromJSON(String json) {
        return new Friend("", "");
        //return new Gson().fromJson(json, Note.class);
    }

}

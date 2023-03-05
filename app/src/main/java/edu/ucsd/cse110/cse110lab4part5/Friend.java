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

    /**
     * Checks for attribute equality between two friends
     * @param other friend to compare to
     * @return true if equal, false otherwise
     */
    public boolean equals(Friend other){
        Location thisLocation = this.getLocation();
        Location otherLocation = other.getLocation();
        if(this.name.equals(other.name)
                && this.uuid.equals(other.uuid)
                && thisLocation.getLongitude() == otherLocation.getLongitude()
                && thisLocation.getLatitude() == otherLocation.getLatitude()
        ){
            return true;
        }
        return false;
    }
}

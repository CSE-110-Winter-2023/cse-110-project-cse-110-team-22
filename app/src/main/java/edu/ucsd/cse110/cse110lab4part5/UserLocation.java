package edu.ucsd.cse110.cse110lab4part5;

public class UserLocation implements Location{
    private static UserLocation instance;
    private double longitude;
    private double latitude;
    private String label;

    public static UserLocation singleton(double latitude, double longitude, String label) {
        if(instance == null){
            instance = new UserLocation(latitude, longitude, label);
            return instance;
        }
        instance.setLabel(label);
        instance.setLatitude(latitude);
        instance.setLongitude(longitude);
        return instance;
    }

    public UserLocation(double latitude, double longitude, String label) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.label = label;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public String getLabel() { return label; }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public void setLabel(String label) { this.label = label; }
}

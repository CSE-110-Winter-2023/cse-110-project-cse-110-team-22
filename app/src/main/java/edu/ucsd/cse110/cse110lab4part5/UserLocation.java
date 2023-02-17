package edu.ucsd.cse110.cse110lab4part5;

public class UserLocation implements Location{
    private double longitude;
    private double latitude;
    private String label;

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

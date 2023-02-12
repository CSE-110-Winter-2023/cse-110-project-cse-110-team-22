package edu.ucsd.cse110.cse110lab4part5;

public class UserLocation implements Location{
    private double longitude;
    private double latitude;

    public UserLocation(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
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
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
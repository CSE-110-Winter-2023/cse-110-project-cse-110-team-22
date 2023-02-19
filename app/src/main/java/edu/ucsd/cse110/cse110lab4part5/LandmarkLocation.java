package edu.ucsd.cse110.cse110lab4part5;

public class LandmarkLocation implements Location{
    private double longitude;
    private double latitude;
    private String label;
    private int icon_num;

    public LandmarkLocation(double latitude, double longitude, String label) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.label = label;
        this.icon_num = icon_num;
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

    public void setIconNum(int num) {this.icon_num = num;}

    public int getIconNum() {return icon_num;}
}

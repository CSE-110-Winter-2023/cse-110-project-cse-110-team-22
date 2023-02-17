package edu.ucsd.cse110.cse110lab4part5;

import java.util.List;

public class LocationUtils {
    /**
     * Computes angle between locations
     * @param startLoc
     * @param endLoc
     * @return
     */
    public static double computeAngle(Location startLoc, Location endLoc) {
        double startLongitude = Math.toRadians(startLoc.getLongitude());
        double startLatitude = Math.toRadians(startLoc.getLatitude());
        double endLongitude = Math.toRadians(endLoc.getLongitude());
        double endLatitude = Math.toRadians(endLoc.getLatitude());
        double X = Math.cos(endLatitude) * Math.sin(endLongitude - startLongitude);
        double Y = Math.cos(startLatitude) * Math.sin(endLatitude)
                - Math.sin(startLatitude) * Math.cos(endLatitude) * Math.cos(endLongitude - startLongitude);
        double beta_rad = Math.atan2(X, Y);
        return Math.toDegrees(beta_rad);
    }

    /**
     * compute display angle on compass
     * @param userLoc
     * @param landscapeLocs
     * @return
     */
    public static double computeAllAngles (Location userLoc, List<Location> landscapeLocs) {
        //TODO
        return 0;
    }
}

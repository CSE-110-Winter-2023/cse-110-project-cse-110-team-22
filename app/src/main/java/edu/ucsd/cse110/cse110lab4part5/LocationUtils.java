package edu.ucsd.cse110.cse110lab4part5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static Map<Integer, Double> computeAllAngles (Location userLoc, List<Location> landscapeLocs) {
        Map<Integer, Double> idToAngleMap = new HashMap<>();
        for (Location lc: landscapeLocs) {
            int i = findPicID(lc);
            idToAngleMap.put(i, computeAngle(userLoc, lc));
        }
        return idToAngleMap;
    }

    public static int findPicID(Location loc) {
        final int FAMILYHOUSE = 0;
        final int FRIEND = 1;
        final int HOME = 2;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(FAMILYHOUSE, R.id.familyhouse);
        map.put(FRIEND, R.id.friend);
        map.put(HOME, R.id.home);
        return map.get(((LandmarkLocation)loc).getIconNum());
    }
}

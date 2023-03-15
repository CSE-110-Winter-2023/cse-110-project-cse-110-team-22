package edu.ucsd.cse110.cse110lab4part5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationUtils {

    static final int FAMILYHOUSE = 0;
    static final int FRIEND = 1;
    static final int HOME = 2;
    static final int NORTH = 3;
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

    /**
     * compute display angle on compass
     * @param userLoc
     * @param uuidToFriendMap
     * @return a Map<String, Double> containing (uuid -> bearing angle)
     */
    public static Map<String, Double> computeAllFriendAngles (Location userLoc, Map<String, Friend> uuidToFriendMap) {
        Map<String, Double> uuidToAngleMap = new HashMap<>();
        for (String uuid: uuidToFriendMap.keySet()) {
            Location friendLoc = uuidToFriendMap.get(uuid).getLocation();
            double angle = computeAngle(userLoc, friendLoc);
            uuidToAngleMap.put(uuid, angle);
        }
        return uuidToAngleMap;
    }

    /**
     * find the id of the icons
     * @param loc
     * @return
     */
    public static int findPicID(Location loc) {
        Map<Integer, Integer> map = new HashMap<>();
//        map.put(FAMILYHOUSE, R.id.familyhouse);
//        map.put(FRIEND, R.id.friend);
//        map.put(HOME, R.id.home);
        map.put(NORTH, R.id.letter_n);
        return map.get(((LandmarkLocation)loc).getIconNum());
    }

    /**
     * find the distance between two locations
     * @param loc1
     * @param loc2
     * @return distance between loc1 and loc2
     */
    public static double computeDistance(Location loc1, Location loc2) {
        double RADIUS = 3958.8; // earth radius from google in miles
        double lat1 = loc1.getLatitude();
        double lon1 = loc1.getLongitude();
        double lat2 = loc2.getLatitude();
        double lon2 = loc2.getLongitude();
        double p = Math.PI / 180.0;
        double tmp = 0.5 - Math.cos((lat2 - lat1) * p)/2 +  Math.cos(lat1 * p)
                * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon1) * p))/2;

        return 2 * RADIUS * Math.asin(Math.sqrt(tmp));
        //return 200.0;
    }

    /**
     * find the distance between the user location and a friend
     * @param loc1
     * @param friend
     * @return distance between the user and the friend
     */
    public static double computeDistance(Location loc1, Friend friend) {
        Location loc2 = friend.getLocation();
        return computeDistance(loc1, loc2);
    }

    /**
     * find the distance between the user location and a friend
     * @param loc1
     * @param uuidToFriendMap
     * @return a Map<String, Double> containing (uuid -> distance in miles)
     */
    public static Map<String, Double> computeAllDistances(Location loc1, Map<String, Friend> uuidToFriendMap) {
        Map<String, Double> ret = new HashMap<>();
        for(String uuid: uuidToFriendMap.keySet()) {
            Friend f = uuidToFriendMap.get(uuid);
            ret.put(uuid, computeDistance(loc1, f));
            //ret.put(uuid, 200.0);
        }
        return ret;
    }
}

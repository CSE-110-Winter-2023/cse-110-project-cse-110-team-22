package edu.ucsd.cse110.cse110lab4part5;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationUtilsTest {
    Location userLoc_pos_pos;
    Location userLoc_pos_neg;
    Location userLoc_neg_pos;
    Location userLoc_neg_neg;
    Location userLoc_zerolat;
    Location userLoc_zerolon;
    Location userLoc_allzero;
    Location landLoc_pos_pos;
    Location landLoc_pos_neg;
    Location landLoc_neg_pos;
    Location landLoc_neg_neg;
    Location landLoc_zerolat;
    Location landLoc_zerolon;
    Location landLoc_allzero;
    double delta = 0.001;
    double deltaD = 0.2;
    String name = "TestName";

    /*
    Setting up some fields to be used in tests
     */
    @Before
    public void setUp() {
        userLoc_pos_pos = new UserLocation(10, 10, name);
        userLoc_pos_neg = new UserLocation(10, -10, name);
        userLoc_neg_pos = new UserLocation(-10, 10, name);
        userLoc_neg_neg = new UserLocation(-10, -10, name);
        userLoc_zerolat = new UserLocation(0, 10, name);
        userLoc_zerolon = new UserLocation(-10, 0, name);
        userLoc_allzero = new UserLocation(0, 0, name);
        landLoc_pos_pos = new LandmarkLocation(30, 30, name);
        landLoc_pos_neg = new LandmarkLocation(30, -30, name);
        landLoc_neg_pos = new LandmarkLocation(-30, 30, name);
        landLoc_neg_neg = new LandmarkLocation(-30, -30, name);
        landLoc_zerolat = new LandmarkLocation(0, 30, name);
        landLoc_zerolon = new LandmarkLocation(-30, 0, name);
        landLoc_allzero = new UserLocation(0, 0, name);
    }

    /*
    Test if LocationUtils.computeAngle works correctly for no zero appearing in user and landmark locations
     */
    @Test
    public void computeAngleNonzeroTest() {
        assertEquals(-139.847, LocationUtils.computeAngle(userLoc_neg_neg, landLoc_neg_neg), delta);
        assertEquals(40.153, LocationUtils.computeAngle(userLoc_pos_pos, landLoc_pos_pos), delta);
    }

    /*
    Test if LocationUtils.computeAngle works correctly for 1 zero appearing in user and landmark locations
     */
    @Test
    public void computeAngleOneZeroTest() {
        assertEquals(-129.909, LocationUtils.computeAngle(userLoc_zerolon, landLoc_neg_neg), delta);
        assertEquals(30.642, LocationUtils.computeAngle(userLoc_zerolat, landLoc_pos_pos), delta);
        assertEquals(101.692, LocationUtils.computeAngle(userLoc_pos_neg, landLoc_zerolat), delta);
    }

    /*
    Test if LocationUtils.computeAngle works correctly for more than 1 zero appearing in user and landmark locations
     */
    @Test
    public void computeAngleTwoZeroTest() {
        assertEquals(180, LocationUtils.computeAngle(userLoc_zerolon, landLoc_zerolon), delta);
        assertEquals(40.893, LocationUtils.computeAngle(userLoc_allzero, landLoc_pos_pos), delta);
        assertEquals(-163.26, LocationUtils.computeAngle(userLoc_zerolat, landLoc_zerolon), delta);
        assertEquals(180, LocationUtils.computeAngle(userLoc_allzero, landLoc_zerolon), delta);
    }

    /*
    Test if LocationUtils.computeDistance works correctly
     */
    @Test
    public void computeDistanceTest() {
        assertEquals(1381.80, LocationUtils.computeDistance(userLoc_zerolon, landLoc_zerolon), deltaD); //(-10, 0/ -30, 0)
        assertEquals(2860.99, LocationUtils.computeDistance(userLoc_allzero, landLoc_pos_pos), deltaD);//(0, 0/ 30 30)
        assertEquals(2174.60, LocationUtils.computeDistance(userLoc_zerolat, landLoc_zerolon), deltaD);// (0, 10/ -30, 0)
        assertEquals(2072.70, LocationUtils.computeDistance(userLoc_allzero, landLoc_zerolon), deltaD); // (0, 0/ -30, 0)

        Location ny = new LandmarkLocation(40.7128, -74.0060, name);
        Location sd = new LandmarkLocation(32.7157, -117.1611, name);
        assertEquals(2427.68, LocationUtils.computeDistance(ny, sd), deltaD);
    }
}

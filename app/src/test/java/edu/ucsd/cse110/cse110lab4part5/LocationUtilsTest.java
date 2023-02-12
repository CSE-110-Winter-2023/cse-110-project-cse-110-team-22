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
    Location landLoc_pos_pos;
    Location landLoc_pos_neg;
    Location landLoc_neg_pos;
    Location landLoc_neg_neg;
    Location landLoc_zerolat;
    Location landLoc_zerolon;
    double delta = 0.001;
    String name = "TestName";

    @Before
    public void setUp() {
        userLoc_pos_pos = new UserLocation(10, 10, name);
        userLoc_pos_neg = new UserLocation(10, -10, name);
        userLoc_neg_pos = new UserLocation(-10, 10, name);
        userLoc_neg_neg = new UserLocation(-10, -10, name);
        userLoc_zerolat = new UserLocation(0, 10, name);
        userLoc_zerolon = new UserLocation(-10, 0, name);
        landLoc_pos_pos = new LandmarkLocation(30, 30, name);
        landLoc_pos_neg = new LandmarkLocation(30, -30, name);
        landLoc_neg_pos = new LandmarkLocation(-30, 30, name);
        landLoc_neg_neg = new LandmarkLocation(-30, -30, name);
        landLoc_zerolat = new LandmarkLocation(0, 30, name);
        landLoc_zerolon = new LandmarkLocation(-30, 0, name);
    }
    @Test
    public void computeAngleTest() {
        assertEquals(-139.847, LocationUtils.computeAngle(userLoc_neg_neg, landLoc_neg_neg), delta);
    }
}

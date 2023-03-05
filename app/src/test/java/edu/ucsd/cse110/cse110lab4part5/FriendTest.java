package edu.ucsd.cse110.cse110lab4part5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class FriendTest {
    Friend friend1;
    Friend friend2;
    String label;
    double delta = 0.0001;

    @Before
    public void setUp() {
        friend1 = new Friend(null, "123");
        friend2 = new Friend(null, "456789");
        label = "TESTLABEL";
    }

    @Test
    public void setLocationTest() {
        friend1.setLocation(new LandmarkLocation(10, 20, label));
        friend2.setLocation(new LandmarkLocation(-10, -20, label));
        assertEquals(10, friend1.getLocation().getLatitude(), delta);
        assertEquals(20, friend1.getLocation().getLongitude(), delta);
        assertEquals(-10, friend2.getLocation().getLatitude(), delta);
        assertEquals(-20, friend2.getLocation().getLongitude(), delta);
        friend1.setLocation(new LandmarkLocation(40, 60, label));
        assertEquals(40, friend1.getLocation().getLatitude(), delta);
        assertEquals(60, friend1.getLocation().getLongitude(), delta);
    }

    @Test
    public void setNameTest() {
        assertNull(friend1.getName());
        assertNull(friend2.getName());
        friend1.setName("abc");
        assertEquals("abc", friend1.getName());
        friend1.setName("Heyu");
        assertEquals("Heyu", friend1.getName());
        friend2.setName("cse110");
        assertEquals("cse110", friend2.getName());
    }

    @Test
    public void friendEqualsTest() {
        assertNotEquals(friend1, friend2);

        String sampleName = "abc";
        double sampleLongitude = -10.3;
        double sampleLatitude = 20.3;
        String sampleUuid = "some_uuid";
        String sampleTime = "2023-02-18T18:30:00Z";
        friend1 = new Friend(sampleName, sampleUuid);
        friend2 = new Friend(sampleName, sampleUuid);
        friend1.setLocation(new LandmarkLocation(sampleLatitude, sampleLongitude, "1"));
        friend2.setLocation(new LandmarkLocation(sampleLatitude, sampleLongitude, "2"));
        assertTrue(friend1.equals(friend2)); // equals should be true when labels are different
        assertTrue(friend2.equals(friend1)); // equals should be true when labels are different
        friend1.setName("something else");
        assertFalse(friend1.equals(friend2));
        assertFalse(friend2.equals(friend1));
    }

    @Test
    public void fromJsonTest() {
        String sampleName = "abc";
        double sampleLongitude = -10.3;
        double sampleLatitude = 20.3;
        String sampleUuid = "some_uuid";
        String sampleTime = "2023-02-18T18:30:00Z";
        String newLine = System.getProperty("line.separator");

        friend1.setName(sampleName);
        friend1.setLocation(new LandmarkLocation(sampleLatitude, sampleLongitude, label));

        String json = "{"                                                       // {
                    + "\t\"public_code\": \"" + sampleUuid + "\"," + newLine    //     "public_code": "point-nemo",
                    + "\t\"label\": \"" + sampleName + "\"," + newLine          //     "label": "Point Nemo",
                    + "\t\"latitude\": \"" + sampleLatitude + "\"," + newLine   //     "latitude": -48.876667,
                    + "\t\"longitude\": \"" + sampleLongitude + "\"," + newLine //     "longitude": -123.393333,
                    + "\t\"created_at\": \"" + sampleTime + "\"," + newLine     //     "created_at": "2023-02-18T12:00:00Z",
                    + "\t\"updated_at\": \"" + sampleTime + "\"" + newLine      //     "updated_at": "2023-02-18T18:30:00Z"
                    + "}";                                                      // }
        friend2 = Friend.fromJSON(json);
        assertEquals(friend1, friend2);
    }
}

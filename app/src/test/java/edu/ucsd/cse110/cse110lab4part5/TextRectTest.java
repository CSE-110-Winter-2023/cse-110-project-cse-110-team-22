package edu.ucsd.cse110.cse110lab4part5;

import android.graphics.Rect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextRectTest {
    TextRect t1, t2, t3, t4, t5;

    /**
     * Setting up variables for the tests
     */
    @Before
    public void setUp() {
        t1 = new TextRect("Emma's big big house", 200, 28);
        t2 = new TextRect("Someone's here", 200, 30);
        t3 = new TextRect("closer", 198, 30);
        t4 = new TextRect("farther", 202, 30);
        t5 = new TextRect("2farther", 202, 30);
    }

    /**
     * Tests for the makeRect() method
     */
    @Test
    public void makeRectTest() {
        Rectangle newRect = t1.makeRect();
        int centerX = (int)(t1.getCenterDist() * Math.cos(Math.toRadians(t1.getCenterAngle())));
        int centerY = (int)(t1.getCenterDist() * Math.sin(Math.toRadians(t1.getCenterAngle())));
        assertEquals(2 * centerX, t1.r.left + t1.r.right);
        assertEquals(2 * centerY, t1.r.top + t1.r.bottom);
    }

    /**
     * Tests for the static intersect() method
     */
    @Test
    public void intersectTest() {
        assertTrue(TextRect.intersect(t1, t2));
        assertTrue(TextRect.intersect(t1, t3));
        assertTrue(TextRect.intersect(t1, t4));
        assertTrue(TextRect.intersect(t3, t2));
        assertTrue(TextRect.intersect(t4, t2));
        assertTrue(TextRect.intersect(t3, t4));
    }

    /**
     * Tests for the nudge() method
     */
    @Test
    public void nudgeTest() {
        TextRect.nudge(t1, t3);
        TextRect.nudge(t2, t5);
        assertTrue(t1.getCenterDist() - t3.getCenterDist() > 10);
        assertTrue(t5.getCenterDist() - t2.getCenterDist() > 10);
    }

    /**
     * Tests for the truncate() method
     */
    @Test
    public void truncateTest() {
        assertTrue(t1.truncate());
        assertTrue(t2.truncate());
        assertTrue(t3.truncate());
        assertTrue(t4.truncate());
        assertTrue(t1.truncate());
        assertTrue(t3.truncate());
        assertTrue(t3.truncate());
        assertFalse(t3.truncate());
    }
}

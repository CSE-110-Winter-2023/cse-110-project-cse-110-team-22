package edu.ucsd.cse110.cse110lab4part5;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangleTest {
    Rectangle r1, r2, r3, r4, r5;

    /**
     * Setting up variables for the tests
     */
    @Before
    public void setUp() {
        r1 = new Rectangle(1, 4, 4, 1);
        r2 = new Rectangle(-4, 3, 0, 1);
        r3 = new Rectangle(-2, 0, 3, -2);
        r4 = new Rectangle(0, 2, 1, -1);
        r5 = new Rectangle(-5, 2, -1, -1);
    }

    /**
     * Test the static intersects() method in Rectangle
     */
    @Test
    public void testIntersects() {
        assertTrue(Rectangle.intersects(r4, r3));
        assertTrue(Rectangle.intersects(r3, r4));
        assertTrue(Rectangle.intersects(r5, r2));
        assertTrue(Rectangle.intersects(r5, r3));
        assertFalse(Rectangle.intersects(r1, r2));
        assertFalse(Rectangle.intersects(r1, r3));
        assertFalse(Rectangle.intersects(r1, r4));
        assertFalse(Rectangle.intersects(r1, r5));
        assertFalse(Rectangle.intersects(r4, r2));
        assertFalse(Rectangle.intersects(r2, r4));
        assertFalse(Rectangle.intersects(r4, r2));
    }
}

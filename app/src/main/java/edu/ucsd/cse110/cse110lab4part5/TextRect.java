package edu.ucsd.cse110.cse110lab4part5;

import android.util.Log;

public class TextRect {
    int centerDist;
    double centerAngle;
    Rectangle r;
    String name;
    int length;
    static double LONGCHAR = 21.8;
    static double SHORTCHAR = 12;
    static int HEIGHT = 38;

    TextRect(String name, int dist, double angle) {
        this.name = name;
        this.centerDist = dist;
        this.centerAngle = 90 - angle; // horizontal to the right is 0 degrees.
        length = calculateLength(name);
        r = makeRect();
    }

    /**
     * Create a new Rect object when called
     * @return an updated Rect object
     */
    public Rectangle makeRect(){
        int l, r, u, b;
        int centerX = (int)(centerDist * Math.cos(Math.toRadians(centerAngle)));
        int centerY = (int)(centerDist * Math.sin(Math.toRadians(centerAngle)));
        l = centerX - length / 2;
        r = centerX + length / 2;
        u = centerY + HEIGHT / 2;
        b = centerY - HEIGHT / 2;
        Rectangle rect = new Rectangle(l, u, r, b);
        return rect;
    }

    /**
     * Find if two TextRect intersects.
     * @param r1
     * @param r2
     * @return whether r1 and r2 intersects
     */
    public static boolean intersect(TextRect r1, TextRect r2) {
        return Rectangle.intersects(r1.getRect(), r2.getRect());
    }

    /**
     * When two TextRect overlaps after both are truncated, move them a little bit
     * @param r1 TextRect number 1.
     * @param r2 TextRect number 2.
     */
    public static void nudge(TextRect r1, TextRect r2) {
        if (Math.abs(r1.getCenterAngle() - r2.getCenterAngle()) < 1) {
            if (r1.getName().length() > 3) {
                r1.setName(r1.getName().substring(0, 3));
            }
            if (r2.getName().length() > 3) {
                r2.setName(r2.getName().substring(0, 3));
            }
            if (!intersect(r1, r2)) return;
        }
        if (r1.getCenterDist() > r2.getCenterDist()) {
            int addedDist = (HEIGHT - r1.getCenterDist() + r2.getCenterDist()) / 2;
            r1.addDist(addedDist);
            r2.addDist(-addedDist);
//            while (intersect(r1, r2)) {
//                r1.addDist(5);
//                r2.addDist(-5);
//                Log.d("..", "..................");
//            }
            for (int i = 0; i < 5; i++) {
                if (intersect(r1, r2)) {
                    r1.addDist(5);
                    r2.addDist(-5);
                } else break;
            }
            if (r2.getCenterDist() < 0) {
                r2.setCenterDist(1);
                r1.setCenterDist(HEIGHT + 1);
            }
        } else {
            int addedDist = (HEIGHT - r2.getCenterDist() + r1.getCenterDist()) / 2;
            r2.addDist(addedDist);
            r1.addDist(-addedDist);
//            while (intersect(r1, r2)) {
//                r2.addDist(5);
//                r1.addDist(-5);
//            }
            for (int i = 0; i < 5; i++) {
                if (intersect(r1, r2)) {
                    r2.addDist(5);
                    r1.addDist(-5);
                } else break;
            }
            if (r1.getCenterDist() < 0) {
                r1.setCenterDist(1);
                r2.setCenterDist(HEIGHT + 1);
            }
        }
    }

    /**
     * Truncate the string. If length <= 3, do nothing.
     * If 3 < length <= 8, remove last char. Otherwise remove 2 chars.
     * @return if the operation has been done
     */
    public boolean truncate() {
        if (name.length() <= 3) return false;
        if (name.length() > 8) {
            length -= calculateLength(name.substring(name.length() - 2));
            name = name.substring(0, name.length() - 2);
        } else {
            length -= calculateLength(name.substring(name.length() - 1));
            name = name.substring(0, name.length() - 1);
        }
        r = makeRect();
        return true;
    }

    private int calculateLength(String name) {
        double l = 0;
        for (char c: name.toCharArray()) {
            if (c == 'i' || c == 'j' || c == 'l' || c == 't' || c == '\'') {
                l += SHORTCHAR;
            } else l += LONGCHAR;
        }
        return (int)l;
    }

    public int getCenterDist() {return centerDist;}
    public double getCenterAngle() {return centerAngle;}
    public Rectangle getRect() {return r;}
    public String getName() {return name;}
    public void setCenterDist(int dist) {this.centerDist = dist;r = makeRect();}
    public void setName(String name) {
        this.name = name;
        length = calculateLength(name);
        r = makeRect();
    }
    public void setCenterAngle(double centerAngle) {this.centerAngle = centerAngle;r = makeRect();}
    public void setRect(Rectangle r) {this.r = r;}
    public void addDist(int d) {this.centerDist += d;r = makeRect();}
}
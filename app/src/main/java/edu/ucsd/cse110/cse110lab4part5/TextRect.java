package edu.ucsd.cse110.cse110lab4part5;

import android.graphics.Rect;

public class TextRect {
    int centerDist;
    double centerAngle;
    Rect r;
    String name;

    TextRect(String name, int dist, double angle) {
        this.name = name;
        this.centerDist = dist;
        this.centerAngle = 90 - angle; // horizontal to the right is 0 degrees.
        r = makeRect();
    }

    /**
     * Create a new Rect object when called
     * @return an updated Rect object
     */
    public Rect makeRect(){
        int l, r, u, b;
        int centerX = (int)(centerDist * Math.cos(Math.toRadians(centerAngle)));
        int centerY = (int)(centerDist * Math.sin(Math.toRadians(centerAngle)));
        l = centerX - name.length() * 13;
        r = centerX + name.length() * 13;
        u = centerY - 18;
        b = centerY + 18; // top less than bottom
        Rect rect = new Rect(l, u, r, b);
        return rect;
    }

    /**
     * Find if two TextRect intersects.
     * @param r1
     * @param r2
     * @return whether r1 and r2 intersects
     */
    public static boolean intersect(TextRect r1, TextRect r2) {
        return Rect.intersects(r1.getRect(), r2.getRect());
    }

    /**
     * When two TextRect overlaps after both are truncated, move them a little bit
     * @param r1 TextRect number 1.
     * @param r2 TextRect number 2.
     */
    public static void nudge(TextRect r1, TextRect r2) {
        if (r1.getCenterDist() > r2.getCenterDist()) {
            int addedDist = (37 - r1.getCenterDist() + r2.getCenterDist()) / 2;
            r1.addDist(addedDist);
            r2.addDist(-addedDist);
            if (r2.getCenterDist() < 0) {
                r2.setCenterDist(1);
                r1.setCenterDist(36);
            }
        } else {
            int addedDist = (37 - r2.getCenterDist() + r1.getCenterDist()) / 2;
            r2.addDist(addedDist);
            r1.addDist(-addedDist);
            if (r1.getCenterDist() < 0) {
                r1.setCenterDist(1);
                r2.setCenterDist(36);
            }
        }
    }

    /**
     * Truncate the string to the first 3 characters.
     * @return if the operation has been done
     */
    public boolean truncate() {
        if (name.length() <= 3) return false;
        name = name.substring(0, 3);
        r = makeRect();
        return true;
    }

    public int getCenterDist() {return centerDist;}
    public double getCenterAngle() {return centerAngle;}
    public Rect getRect() {return r;}
    public String getName() {return name;}
    public void setCenterDist(int dist) {this.centerDist = dist;r = makeRect();}
    public void setName(String name) {this.name = name;r = makeRect();}
    public void setCenterAngle(double centerAngle) {this.centerAngle = centerAngle;r = makeRect();}
    public void setRect(Rect r) {this.r = r;}
    public void addDist(int d) {this.centerDist += d;r = makeRect();}
}

package edu.ucsd.cse110.cse110lab4part5;

public class Rectangle {
    public int left;
    public int right;
    public int top;
    public int bottom;
    public Rectangle(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public static boolean intersects(Rectangle r1, Rectangle r2) {
        Rectangle a, b;
        if (r1.top > r2.top) {
            a = r1; b = r2;
        } else {a = r2; b = r1;}
        if (a.bottom < b.top) {
            if (a.left > b.left) {
                if (b.right > a.left) return true;
            } else {
                if (a.right > b.left) return true;
            }
        }
        return false;
    }
}
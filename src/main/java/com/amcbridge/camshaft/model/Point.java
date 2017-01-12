package com.amcbridge.camshaft.model;

/**
 * Class contains data about point of motion law and cam shaft.
 */

public class Point {
    /**
     * X value of 2D point
     */
    private double x;
    /**
     * Y value of 2D point
     */
    private double y;

    /**
     *
     * @param x - X value of point
     * @param y - Y value of point
     */
    public Point(double x, double y) {

        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;
        return(x == point.getX() && y == point.getY());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public double getX() {

        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public String toString(){
        return x + " " + y;
    }
}

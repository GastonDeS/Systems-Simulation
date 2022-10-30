package models;

import java.awt.geom.Point2D;

public class Point extends Point2D.Double {

    public Point(double x, double y) {
        super(x, y);
    }

    public Point normalize() {
        double norm = this.norm();
        return new Point(x/norm, y/norm);
    }

    public double norm() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Point sub(Point p) {
        return new Point(x - p.x, y - p.y);
    }

    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }

    public Point prod(double constant) {
        return new Point(x * constant, y * constant);
    }

    public double dist(Point p) {
        return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

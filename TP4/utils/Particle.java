package utils;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Particle implements Cloneable {
    private final int label;
    private final double mass;
    private final double radius;
    private double posX;
    private double posY;
    private double velX;
    private double velY;
    private double accX;
    private double accY;

    public Particle(int label, double mass, double radius, double posX, double posY,
                    double velX, double velY, double accX, double accY) {
        this.label = label;
        this.mass = mass;
        this.radius = radius;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.accX = accX;
        this.accY = accY;
    }

    public Double distance(Particle particle) {
        return Math.sqrt(Math.pow(this.posX - particle.posX, 2) + Math.pow(this.posY - particle.posY, 2));
    }

    // this has a problem on precision example 1.400000000000000004 compared to 1.4
    public boolean isOverlap(Particle particle2) {
        return this.distance(particle2) <= this.radius + particle2.radius;
    }

    public void refreshToTime(double time) {
        this.posX += this.velX * time;
        this.posY += this.velY * time;
    }

    public double calculateCollisionTimeWithParticle(Particle p) {
        Point2D.Double dr = new Point2D.Double(p.posX - posX, p.posY - posY); // Δr
        Point2D.Double dv = new Point2D.Double(p.velX - velX, p.velY - velY); // Δv
        double rv = dr.x*dv.x+dr.y*dv.y; // Δr.Δv

        if (rv >= 0)
            return Double.POSITIVE_INFINITY;

        double rr = Math.pow(dr.x, 2) + Math.pow(dr.y, 2); // Δr.Δr
        double vv = Math.pow(dv.x, 2) + Math.pow(dv.y, 2); // Δv.Δv
        double sigma = radius + p.radius;
        double d = Math.pow(rv, 2) - vv * (rr - Math.pow(sigma, 2));

        if (d < 0) {
            return Double.POSITIVE_INFINITY;
        }

        return - (rv + Math.sqrt(d))/vv;
    }

    public double calculateCollisionTimeWithWall(Double L, char direction) {
        double tc = Double.POSITIVE_INFINITY;
        if (velX == 0 || velY == 0) return tc;
        switch (direction) {
            case 'x':
                if (velX > 0) {
                    tc = (L - radius - posX)/velX;
                } else {
                    tc = (radius - posX)/velX;
                }
                break;
            case 'y':
                if (velY > 0) {
                    tc = (L - radius - posY)/velY;
                } else {
                    tc = (radius - posY)/velY;
                }
                break;
        }
        return tc;
    }

    /*
        Getters and setters
     */

    public double getMass() {
        return mass;
    }

    public double getRadius() {
        return radius;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public double getAccX() {
        return accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    @Override
    public String toString() {
        return posX +
                " " + posY +
                " " + velX +
                " " + velY + " " + radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof Particle)) return false;
        Particle particle = (Particle) o;
        return label == particle.label;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public Particle clone() {
        try {
            return (Particle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
}

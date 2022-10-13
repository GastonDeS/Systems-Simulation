package utils;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Particle implements Cloneable {
    private String name;
    private int label;
    private double mass;
    private double radius;
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

    public Particle() {}

    public Double distance(Particle particle) {
        return Math.sqrt(Math.pow(this.posX - particle.posX, 2) + Math.pow(this.posY - particle.posY, 2));
    }

    public Double distanceRadius(Particle particle) {
        return Math.sqrt(Math.pow(this.posX - particle.posX, 2) + Math.pow(this.posY - particle.posY, 2)) - this.radius - particle.radius;
    }

    // this has a problem on precision example 1.400000000000000004 compared to 1.4
    public boolean isOverlap(Particle particle2) {
        return this.distance(particle2) <= this.radius + particle2.radius;
    }

    /*
        Getters and setters
     */

    public Particle withLabel(int label) {
        this.label = label;
        return this;
    }

    public Particle withMass(double mass) {
        this.mass = mass;
        return this;
    }

    public Particle withRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public Particle withPosX(double posX) {
        this.posX = posX;
        return this;
    }

    public Particle withPosY(double posY) {
        this.posY = posY;
        return this;
    }

    public Particle withVelX(double velX) {
        this.velX = velX;
        return this;
    }

    public Particle withVelY(double velY) {
        this.velY = velY;
        return this;
    }

    public Particle withAccX(double accX) {
        this.accX = accX;
        return this;
    }

    public Particle withAccY(double accY) {
        this.accY = accY;
        return this;
    }

    public Particle withName(String name) {
        this.name = name;
        return this;
    }

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

    public int getLabel() {
        return label;
    }

    public String getName() {
        return name;
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

package utils;

public class Planet {
    private final double x;
    private final double y;
    private final double velocityX;
    private final double velocityY;

    public Planet(double x, double y, double velocityX, double velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    @Override
    public String toString() {
        return "InitialConditions{" +
                "x=" + x +
                ", y=" + y +
                ", velx=" + velocityX +
                ", vely=" + velocityY +
                '}';
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }
}

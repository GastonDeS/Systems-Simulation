package utils;

public class Planet {
    private final double radius;
//    private final double mass;
    private final double x;
    private final double y;
    private final double velocityX;
    private final double velocityY;

    public Planet(double radius, double x, double y, double velocityX, double velocityY) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    @Override
    public String toString() {
        return x + " "+
                y + " "+
                velocityX + " " +
                velocityY + " " + radius;
    }


    public double getRadius() {
        return radius;
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

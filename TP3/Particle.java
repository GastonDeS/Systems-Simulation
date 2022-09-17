

public class Particle {

    private final double mass;
    private final double radius;
    private double posX;
    private double posY;
    private double velX;
    private double velY;

    public Particle(double mass, double radius, double posX, double posY, double velX, double velY) {
        this.mass = mass;
        this.radius = radius;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
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

    public Double distance(Particle particle) {
        return Math.sqrt(Math.pow(this.posX - particle.posX, 2) + Math.pow(this.posY - particle.posY, 2));
    }

    // this has a problem on precision example 1.400000000000000004 compared to 1.4
    public boolean isOverlap(Particle particle2) {
        if (this.distance(particle2) <= this.radius + particle2.radius) {
            return true;
        }
        return false;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }
}

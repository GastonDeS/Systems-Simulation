import java.awt.geom.Point2D;

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

    public double calculateCollisionTimeWithParticle(Particle p) {
        Point2D.Double dr = new Point2D.Double(p.posX - posX, p.posY - posY); // Δr
        Point2D.Double dv = new Point2D.Double(p.velX - velX, p.velY - velY); // Δv
        double rv = dr.x * dv.x + dr.y * dv.y; // Δr.Δv

        if (rv > 0)
            return Double.POSITIVE_INFINITY;

        double rr = Math.pow(dr.x, 2) + Math.pow(dr.y, 2); // Δr.Δr
        double vv = Math.pow(dv.x, 2) + Math.pow(dv.y, 2); // Δv.Δv
        double sigma = radius + p.radius;
        double d = Math.pow(rv, 2) - vv * (rr - Math.pow(sigma, 2));

        if (d > 0) {
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
        return this.distance(particle2) <= this.radius + particle2.radius;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }
}

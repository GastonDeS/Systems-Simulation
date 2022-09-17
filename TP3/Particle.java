import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import java.awt.geom.Point2D;
import java.util.Objects;

public class Particle {
    private final int label;
    private final double mass;
    private final double radius;
    private double posX;
    private double posY;
    private double velX;
    private double velY;

    public Particle(int label, double mass, double radius, double posX, double posY, double velX, double velY) {
        this.label = label;
        this.mass = mass;
        this.radius = radius;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
    }

    public static void updateAfterCollision(Event event) {
        switch (event.getDirection()) {
            case 'x', 'y' -> updateAfterCollisionWithWall(event.getP1(), event.getDirection());
            case '-' -> updateAfterCollisionWithParticle(event.getP1(), event.getP2());
        }
    }

    private static void updateAfterCollisionWithWall(Particle p, char direction) {
        if (direction == 'x') {
            p.setVelX(-p.velX);
        } else {
            p.setVelY(-p.velY);
        }
    }

    private static void updateAfterCollisionWithParticle(Particle p1, Particle p2) {
        double dx = p2.posX - p1.posX; // Δx
        double dy = p2.posY - p1.posY; // Δy
        Point2D.Double dr = new Point2D.Double(dx, dy); // Δr
        Point2D.Double dv = new Point2D.Double(p2.velX - p1.velX, p2.velY - p1.velY); // Δv
        double sigma = p1.radius + p2.radius;
        double m1 = p1.mass;
        double m2 = p2.mass;
        double rv = dr.x*dv.x+dr.y*dv.y; // Δr.Δv
        double J = (2*m1*m2*rv)/(sigma*(m1+m2));
        double Jx = J*dx/sigma;
        double Jy = J*dy/sigma;

        p1.setVelX(p1.velX + Jx/m1);
        p1.setVelY(p1.velY + Jy/m1);

        p2.setVelX(p2.velX - Jx/m2);
        p2.setVelY(p2.velY - Jy/m2);

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

    @Override
    public String toString() {
        return posX +
                "," + posY +
                "," + velX +
                "," + velY;
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
}

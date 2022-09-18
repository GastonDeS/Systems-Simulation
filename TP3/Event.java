import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private final double time;
    private final Particle p1;
    private final Particle p2;
    private final char direction; // '-' means collision between two particles has taken place

    public Event(double time, Particle p1, Particle p2, char direction) {
        this.time = time;
        this.p1 = p1;
        this.p2 = p2;
        this.direction = direction;
    }

    public void updateAfterCollision() {
        switch (direction) {
            case 'x':
            case 'y':
                updateAfterCollisionWithWall();
                break;
            case '-':
                updateAfterCollisionWithParticle();
                break;
        }
    }

    private void updateAfterCollisionWithWall() {
        if (direction == 'x') {
            p1.setVelX(-p1.getVelX());
        } else {
            p1.setVelY(-p1.getVelY());
        }
    }

    private void updateAfterCollisionWithParticle() {
        double dx = p2.getPosX() - p1.getPosX(); // Δx
        double dy = p2.getPosY() - p1.getPosY(); // Δy
        Point2D.Double dr = new Point2D.Double(dx, dy); // Δr
        Point2D.Double dv = new Point2D.Double(p2.getVelX() - p1.getVelX(), p2.getVelY() - p1.getVelY()); // Δv
        double sigma = p1.getRadius() + p2.getRadius();
        double m1 = p1.getMass();
        double m2 = p2.getMass();
        double rv = dr.x*dv.x+dr.y*dv.y; // Δr.Δv
        double J = (2*m1*m2*rv)/(sigma*(m1+m2));
        double Jx = (J*dx)/sigma;
        double Jy = (J*dy)/sigma;


        p1.setVelX(p1.getVelX() + Jx/m1);
        p1.setVelY(p1.getVelY() + Jy/m1);

        p2.setVelX(p2.getVelX() - Jx/m2);
        p2.setVelY(p2.getVelY() - Jy/m2);
    }

    public boolean containsParticle(Particle p) {
        return p2 == null ? p1.equals(p) : p1.equals(p) || p2.equals(p);
    }

    public List<Particle> getParticles() {
        List<Particle> particles = new ArrayList<>();
        particles.add(p1);
        if (p2 != null) particles.add(p2);
        return particles;
    }

    public double getTime() {
        return time;
    }

    public Particle getP1() {
        return p1;
    }

    public Particle getP2() {
        return p2;
    }

    public char getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", direction=" + direction +
                '}';
    }
}

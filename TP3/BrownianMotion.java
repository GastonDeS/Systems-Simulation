import com.sun.xml.internal.ws.wsdl.writer.document.Part;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class BrownianMotion {
    private final List<Particle> particles;
    private PriorityQueue<Event> events;
    private final double L;

    public BrownianMotion(List<Particle> particles, double l) {
        this.particles = particles;
        this.L = l;
        this.events = new PriorityQueue<>(Comparator.comparing(Event::getTime));
    }

    private void calculateEvents() {
        for (Particle particle : particles) {
            calculateEventForParticle(particle, null);
        }
    }

    private void calculateEventForParticle(Particle p, Particle omitted) {
        double tc = Double.POSITIVE_INFINITY;
        double tcWithWalls = Math.min(p.calculateCollisionTimeWithWall(L, 'x'),
                p.calculateCollisionTimeWithWall(L, 'y'));

        Particle p2 = null;
        for (Particle other : particles) {
            if (!other.equals(p) && !other.equals(omitted)) {
                double tcAux = p.calculateCollisionTimeWithParticle(other);
                if (tcAux < tc) {
                    tc = tcAux;
                    p2 = other;
                }
            }
        }

        tc = Math.min(tc, tcWithWalls);
        events.add(new Event(tc, p, p2));

    }
}

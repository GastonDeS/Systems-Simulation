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
        for (Particle p : particles) {
            calculateEventForParticle(p, null);
        }
    }

    private void calculateEventForParticle(Particle p, Particle omitted) {
        double tc = Double.POSITIVE_INFINITY;
        double tcVerticalWalls = p.calculateCollisionTimeWithWall(L, 'x');
        double tcHorizontalWalls = p.calculateCollisionTimeWithWall(L, 'y');
    }
}

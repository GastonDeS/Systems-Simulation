import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BrownianMotion {
    private final List<Particle> particles;
    private Queue<Event> events;

    public BrownianMotion(List<Particle> particles) {
        this.particles = particles;
        this.events = new LinkedList<>();
    }

    private void calculateEvents() {
        for (Particle p : particles) {
            calculateEventForParticle(p, null);
        }
    }

    private void calculateEventForParticle(Particle p, Particle omitted) {
        double tc = Double.POSITIVE_INFINITY;

    }
}

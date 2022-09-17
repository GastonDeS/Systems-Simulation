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

    public void calculateEventForParticle(Particle p, Particle omitted) {
        double tc = Double.POSITIVE_INFINITY;
        double tcDirX = p.calculateCollisionTimeWithWall(L, 'x');
        double tcDirY = p.calculateCollisionTimeWithWall(L, 'y');
        char direction = '-';
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

        if (tcDirX < tc) {
            tc = tcDirX;
            direction = 'x';
        }

        if (tcDirY < tc) {
            tc = tcDirY;
            direction = 'y';
        }

        events.add(new Event(tc, p, p2, direction));
    }

    private void refreshBeforeEvent() {
        Event event = events.poll(); // We don't have to check if it's empty because there is always a next event
        if (event == null) return; // This should happen on normal activity
        double time = event.getTime();
        particles.forEach(particle -> particle.refreshToTime(time));
    }
}

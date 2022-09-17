import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;


public class BrownianMotion {
    private final List<Particle> particles;
    private PriorityQueue<Event> events;
    private final double L;

    public BrownianMotion(List<Particle> particles, double l) {
        this.particles = particles;
        this.L = l;
        this.events = new PriorityQueue<>(Comparator.comparing(Event::getTime));
    }

    public void calculateEvents() {
        for (Particle particle : particles) {
            calculateEventForParticle(particle, null);
        }
        while (!events.isEmpty()) {
            System.out.println(events.poll());
        }
    }

    public Event performCollision() {
        Event event = events.poll();
        if (event == null) return null; // this shouldn't happen
        event.updateAfterCollision();
        return event;
    }

    public void removeAndCalculateEventForParticle(Particle p1, Particle p2) {
        List<Event> eventsWithoutP1AndP2 = events.stream().filter(e -> {
            boolean filterCondition = e.getP1().equals(p1);
            if (e.getP2()!=null) filterCondition = filterCondition || e.getP2().equals(p2);
            return filterCondition;
        }).collect(Collectors.toList());
        this.events = new PriorityQueue<>(Comparator.comparing(Event::getTime));
        this.events.addAll(eventsWithoutP1AndP2);

        calculateEventForParticle(p1, p2);
        if (p2 != null)
            calculateEventForParticle(p2, p1);
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

        if (tc > 0) // remove this its highly weird that we need to check that its positive
        events.add(new Event(tc, p, p2, direction));
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void refreshBeforeEvent() {
        Event event = events.peek(); // We don't have to check if it's empty because there is always a next event
        if (event == null) return; // This should happen on normal activity
        double time = event.getTime();
        for (Particle particle : particles) {
            particle.refreshToTime(time);
        }
    }
}

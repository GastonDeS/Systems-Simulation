import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainTP3 {
    private final static double L = 6; // double because then its get divided so we avoid casting
    private final static double maxVelocity = 2; // max velocity module
    private final static int eventsQty = 10000;

    public static void main(String[] args) {
        List<Particle> particles = generateParticles();
        System.out.println(particles.size());

        BrownianMotion brownianMotion = new BrownianMotion(particles, L);
        brownianMotion.calculateEvents();
        List<Event> events = new ArrayList<>();
        Event lastEvent;
        for (int i = 0; i < eventsQty ; i++) {
            brownianMotion.refreshBeforeEvent();
            saveState(brownianMotion.getParticles(), i);
            lastEvent = brownianMotion.performCollision();
            if ( lastEvent != null) {
                brownianMotion.removeAndCalculateEventForParticle(lastEvent.getP1(), lastEvent.getP2());
                if (lastEvent.containsParticle(particles.get(0)) && lastEvent.getP2() == null) {
                    System.out.println("big particle crash");
                    break;
                }
            }
            else throw new NullPointerException();
            events.add(lastEvent);
        }

        saveEvents(events);
    }

    private static void saveEvents(List<Event> events) {
        File positions = new File("./TP3/events.csv");
        try {
            FileWriter positionsFile = new FileWriter(positions);
            for (Event event : events) {
                positionsFile.write(event+ "\n");
            }

            positionsFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void saveState(List<Particle> particles, int iteration) {
        File positions = new File("./../positionTP3/positions"+iteration+".xyz");
        try {
            FileWriter positionsFile = new FileWriter(positions);

            positionsFile.write(
                    particles.size()+"\n" +
                    "Lattice=\"6 0.0 0.0 0.0 6 0.0 0.0 0.0 6\"" +
                    "\n");
            for (Particle particle : particles) {
                positionsFile.write(particle.toString() + "\n");
            }

            positionsFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static List<Particle> generateParticles() {
        List<Particle> particles = new ArrayList<>();

        int particlesAmount = (int) Math.round((Math.random() * 50) + 100);

        particles.add(new Particle(0, 2, 0.7,L/2,L/2, 0,0));
        for (int i = 0; i < particlesAmount; i++) {
            Pair<Double, Double> velocity = getRandomVelocity();
            Particle particle = new Particle(i + 1,0.9,0.2, getRandomPos(), getRandomPos(), velocity.getValue1(), velocity.getValue2());
            while (isOverlap(particles, particle)) {
                particle.setPosX(getRandomPos());
                particle.setPosY(getRandomPos());
            }
            particles.add(particle);
        }

        return particles;
    }

    private static Pair<Double, Double> getRandomVelocity() {
        double speed = Math.random() * maxVelocity;
        double angle = Math.random() * 2 * Math.PI;

        return new Pair<>(speed * Math.cos(angle), speed * Math.sin(angle));
    }

    private static double getRandomPos() {
        double radius = 0.2;
        return Math.random() * (L - 2*radius) + radius;
    }

    private static boolean isOverlap(List<Particle> particles , Particle particle) {
        for (Particle particle2: particles) {
            if (particle.isOverlap(particle2)) return true;
        }
        return false;
    }


}

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class MainTP3 {
    private final static double L = 6; // double because then its get divided so we avoid casting
    private final static double maxVelocity = 2; // max velocity module

    public static void main(String[] args) {
        List<Particle> particles = generateParticles();
        System.out.println(particles.size());
    }

    private static List<Particle> generateParticles() {
        List<Particle> particles = new ArrayList<>();

        int particlesAmount = (int) Math.round((Math.random() * 50) + 100);

        particles.add(new Particle(0, 2, 0.7,L/2,L/2, 0,0));
        for (int i = 0; i < particlesAmount; i++) {
            Pair<Double, Double> velocity = getRandomVelocity();
            Particle particle = new Particle(i + 1,0.9,0.2, getRandomPos(), getRandomPos(), velocity.getKey(), velocity.getValue());
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
        return Math.random() * L;
    }

    private static boolean isOverlap(List<Particle> particles , Particle particle) {
        for (Particle particle2: particles) {
            if (particle.isOverlap(particle2)) return true;
        }
        return false;
    }


}

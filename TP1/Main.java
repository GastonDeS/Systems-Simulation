import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {
        List<Particle> particles = generateRandomParticles(1000);
        IndexTable index = new IndexTable(0.1, 1.0);
        index.index(particles);

        index.printTable();
        List<Particle> nearParticles = index.findCloseParticles(particles.get(2), 0.1);
        System.out.println(nearParticles.size());
        nearParticles.forEach((particle)-> System.out.printf("x: %g \ty: %g\t%g\n", particle.getX(), particle.getY(), particle.distance(particles.get(2))));
    }

    private static List<Particle> generateRandomParticles(int amount) {
        List<Particle> randomParticles = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            randomParticles.add(new Particle(Math.random(),Math.random()));
        }
        return randomParticles;
    }
}

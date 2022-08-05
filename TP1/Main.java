import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {
        List<Particle> particles = generateRandomParticles(1000000);

        long time1 = System.currentTimeMillis();
        IndexTable index = new IndexTable(0.1, 1.0);
        index.index(particles);
        for (int i = 0; i < 1000; i++) {
            testIndexWithList(index, particles.get(i), i);
        }
        System.out.printf("time1: %d\n", System.currentTimeMillis() - time1);

        long time2 = System.currentTimeMillis();
        IndexHashTable indexHash = new IndexHashTable(0.1, 1.0);
        index.index(particles);
        for (int i = 0; i < 1000; i++) {
            testIndexWithHash(indexHash, particles.get(i), i);
        }
        System.out.printf("time2: %d\n", System.currentTimeMillis() - time2);
    }

    private static void testIndexWithList(IndexTable index, Particle particle, int indexClose) {
//        index.printTable();
        List<Particle> nearParticles = index.findCloseParticles(particle, 0.1);
//        System.out.println(nearParticles.size());
//        nearParticles.forEach((particle)-> System.out.printf("x: %g \ty: %g\tdist: %g\n", particle.getX(), particle.getY(), particle.distance(particles.get(2))));
    }

    private static void testIndexWithHash(IndexHashTable indexHash, Particle particle, int indexClose) {
//        indexHash.printTable();
        List<Particle> nearParticlesHashed = indexHash.findCloseParticles(particle, 0.1);
//        System.out.println(nearParticlesHashed.size());
//        nearParticlesHashed.forEach((particle)-> System.out.printf("x: %g \ty: %g\tdist: %g\n", particle.getX(), particle.getY(), particle.distance(particles.get(2))));
    }

    private static List<Particle> generateRandomParticles(int amount) {
        List<Particle> randomParticles = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            randomParticles.add(new Particle(Math.random(),Math.random()));
        }
        return randomParticles;
    }
}

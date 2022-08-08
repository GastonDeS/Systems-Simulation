import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String args[]) {
        List<Particle> particles = generateRandomParticles(100000);

        long time1 = System.currentTimeMillis();
        IndexHashTable indexHash = new IndexHashTable(0.1, 1.0);
        indexHash.index(particles);
//        indexHash.printTable();
        for (int i = 0; i < particles.size(); i++) {
            if (i % 10000 == 0) System.out.println(i);
            particles.get(i).setNearParticles(indexHash.findCloseParticles(particles.get(i), 0.1));
        }
        System.out.printf("time1: %d\n", System.currentTimeMillis() - time1);
//        System.out.printf("sumCheck: %d\n", particles.stream()
//                .map(particle -> particle.getNearParticles().size())
//                .reduce(0, Integer::sum));
//        indexHash.printTable();
//        indexHash.resetIndex();

//        List<Particle> check = new ArrayList<>(particles.get(0).getNearParticles());
//        particles.forEach(particle -> particle.setNearParticles(new ArrayList<>()));

        long time2 = System.currentTimeMillis();
//        indexHash.index(particles);
        particles = indexHash.addNearParticlesWithFastAlgo(particles, 0.1);
        System.out.printf("time2: %d\n", System.currentTimeMillis() - time2);

//        int repeatedCount = particles.stream().map(p -> countRepeated(p.getNearParticles())).reduce(0, Integer::sum) ;
//        System.out.printf("repeatedCheck: %d\n", repeatedCount);
//        System.out.printf("sumCheck: %d\n", particles.stream()
//                .map(particle -> particle.getNearParticles().size())
//                .reduce(0, Integer::sum));
//        System.out.println(particles.get(0).getNearParticles().stream().map(Particle::getLabel).sorted(Comparator.comparingInt(Integer::parseInt)).collect(Collectors.toList()));
//        System.out.println(check.stream().map(Particle::getLabel).collect(Collectors.toList()));
    }

    private static int countRepeated(List<Particle> particles) {
        int sum = 0;
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i+1; j < particles.size(); j++) {
                if (particles.get(i) == particles.get(j))
                    sum++;
            }
        }
        return sum;
    }

    private static List<Particle> generateRandomParticles(int amount) {
        List<Particle> randomParticles = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            randomParticles.add(new Particle(Math.random(),Math.random(), ""+i));
        }
        return randomParticles;
    }
}

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String args[]) {
        List<Particle> particles = generateRandomParticles(200);

        System.out.println("test 1");
        long time1 = System.currentTimeMillis();
        IndexHashTable indexHash = new IndexHashTable(0.1, 1.0);
        indexHash.index(particles);
//        indexHash.printTable();
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).setNearParticles(indexHash.findCloseParticles(particles.get(i), 0.1));
        }
//        System.out.println(particles.get(0).getNearParticles().stream().map(Particle::getLabel).sorted(Comparator.comparingInt(Integer::parseInt)).collect(Collectors.toList()));
        System.out.printf("time1: %d\n", System.currentTimeMillis() - time1);
        System.out.printf("sumCheck: %d\n", particles.stream()
                .map(particle -> particle.getNearParticles().size())
                .reduce(0, Integer::sum));
//        indexHash.printTable();
//        indexHash.resetIndex();

//        System.out.println(particles.get(0).getNearParticles().stream().map(Particle::getLabel).sorted(Comparator.comparingInt(Integer::parseInt)).collect(Collectors.toList()));
        List<Particle> check = new ArrayList<>(particles.get(0).getNearParticles());
        particles.forEach(particle -> particle.setNearParticles(new ArrayList<>()));

        System.out.println("test 2");
        long time2 = System.currentTimeMillis();
//        indexHash.index(particles);
        particles = indexHash.addNearParticlesWithFastAlgo(particles, 0.1);
        System.out.printf("time2: %d\n", System.currentTimeMillis() - time2);

        int repeatedCount = particles.stream().map(p -> countRepeated(p.getNearParticles())).reduce(0, Integer::sum) ;
        System.out.printf("repeatedCheck: %d\n", repeatedCount);
        System.out.printf("sumCheck: %d\n", particles.stream()
                .map(particle -> particle.getNearParticles().size())
                .reduce(0, Integer::sum));
        System.out.println(particles.get(0).getNearParticles().stream().map(Particle::getLabel).sorted(Comparator.comparingInt(Integer::parseInt)).collect(Collectors.toList()));
        System.out.println(check.stream().map(Particle::getLabel).collect(Collectors.toList()));

        Particle particle = particles.get(0);
//        particle.getNearParticles().stream().filter((p) -> p.distance(particle) > .1).forEach(p-> System.out.println(particle.getLabel()+" "+Math.round(particle.getX()*10)+" "+Math.round(particle.getY()*10)));
//        particles.get(0).getNearParticles().stream().forEach((particle -> System.out.println(particle.getLabel()+" "+Math.round(particle.getX()*10)+" "+Math.round(particle.getY()*10))));
//        indexHash.printTable();
        System.out.println(particle.getLabel()+" "+Math.round(particle.getX()*10)+" "+Math.round(particle.getY()*10));
        particle.getNearParticles().stream()
                .filter(particle1 -> !check.contains(particle1))
                .forEach(p-> System.out.println(p.getLabel()+" "+p.distance(particle)+" "+Math.round(p.getX()*10)+" "+Math.round(p.getY()*10)));
        check.stream()
                .filter(particle1 -> !particle.getNearParticles().contains(particle1))
                .forEach(p-> System.out.println(p.getLabel()+" "+p.distance(particle)+" "+Math.round(p.getX()*10)+" "+Math.round(p.getY()*10)));


        System.out.println(indexHash.skipped);
//        System.out.println(indexHash.getIndexTable().keySet().stream().sorted().collect(Collectors.toList()));
//        IndexHashTable indexHashTableAux = new IndexHashTable(0.1, 1.);
//        indexHashTableAux.index(particle.getNearParticles());
//        indexHash.printTable();
//        System.out.println();
//        indexHashTableAux.printTable();
    }

    private static int countRepeated(List<Particle> particles) {
        int sum = 0;
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i+1; j < particles.size(); j++) {
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

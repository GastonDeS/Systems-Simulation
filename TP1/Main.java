import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Particle> particles = generateRandomParticles(10000);
        Double Rd = 0.1;

        List<Double> radiusSorted = particles.stream().map(Particle::getRadius).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        Double maxRadius1 = radiusSorted.get(radiusSorted.size()-1);
        Double maxRadius2 = radiusSorted.get(radiusSorted.size()-2);

        Double cellLength = Rd+maxRadius1+maxRadius2;

        long time1 = System.currentTimeMillis();
        IndexHashTable indexHash = new IndexHashTable(cellLength, 1.0);
        indexHash.index(particles);
//        indexHash.printTable();
        for (int i = 0; i < particles.size(); i++) {
//            if (i % 100 == 0) System.out.println(i);
            particles.get(i).setNearParticles(indexHash.findCloseParticles(particles.get(i), 0.1));
        }
        System.out.printf("time1: %d\n", System.currentTimeMillis() - time1);
        System.out.printf("sumCheck: %d\n", particles.stream()
                .map(particle -> particle.getNearParticles().size())
                .reduce(0, Integer::sum));
//        indexHash.printTable();
//        indexHash.resetIndex();

//        List<Particle> check = new ArrayList<>(particles.get(0).getNearParticles());
        particles.forEach(particle -> particle.setNearParticles(new ArrayList<>()));

        long time2 = System.currentTimeMillis();
//        indexHash.index(particles);
        particles = indexHash.addNearParticlesWithFastAlgo(particles, 0.1);
        System.out.printf("time2: %d\n", System.currentTimeMillis() - time2);

//        int repeatedCount = particles.stream().map(p -> countRepeated(p.getNearParticles())).reduce(0, Integer::sum) ;
//        System.out.printf("repeatedCheck: %d\n", repeatedCount);
        System.out.printf("sumCheck: %d\n", particles.stream()
                .map(particle -> particle.getNearParticles().size())
                .reduce(0, Integer::sum));
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
            randomParticles.add(new Particle(Math.random(),Math.random(), Math.random()/4, ""+i));
        }
        return randomParticles;
    }

    private static void printResults(List<Particle> particles) throws IOException {
        File positions = new File("positions.csv");
        File neighbors = new File("neighbors.csv");
        FileWriter positionsFile = new FileWriter(positions);
        FileWriter neighborsFile = new FileWriter(neighbors);

        for (Particle p : particles) {
            positionsFile.write(p.getX().toString() + "," + p.getY().toString() + "\n");
            if (p.getNearParticles().size() > 0) {
                StringBuilder labels = new StringBuilder();
                labels.append(p.getLabel()).append(",");
                for (int i = 0; i < p.getNearParticles().size(); i++) {
                    labels.append(p.getNearParticles().get(i).getLabel());
                    if (i != p.getNearParticles().size() - 1) labels.append(",");
                }
                labels.append("\n");
                neighborsFile.write(labels.toString());
            }
        }

        positionsFile.close();
        neighborsFile.close();
    }
}

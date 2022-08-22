import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        int particlesQty = 0;
        boolean isCircular = false;
        Double Rc = 0.0, L = 0.0;
        File file = new File("TP1/static.txt");
        Scanner sc = new Scanner(file);
        sc.useDelimiter("=|\\n");
        while (sc.hasNext()) {
            String token = sc.next();
            if (token.equals("PARTICLES_QTY")) particlesQty = Integer.parseInt(sc.next());
            if (token.equals("Rc")) Rc = Double.parseDouble(sc.next());
            if (token.equals("L")) L = Double.parseDouble(sc.next());
            if (token.equals("CIRCULAR")) isCircular = Boolean.parseBoolean(sc.next());
        }

        List<Particle> particles = generateRandomParticles(particlesQty);

        List<Double> radiusSorted = particles.stream().map(Particle::getRadius).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        Double maxRadius1 = radiusSorted.get(radiusSorted.size()-1);


        Double cellLength = Rc + maxRadius1 *2;

//        long time1 = System.currentTimeMillis();
        IndexHashTable indexHash = new IndexHashTable(cellLength, L);
        indexHash.index(particles);
//        for (int i = 0; i < particles.size(); i++) {
//            if (isCircular) {
//                particles.get(i).setNearParticles(indexHash.findCloseParticlesCircular(particles.get(i), Rc));
//            } else
//                particles.get(i).setNearParticles(indexHash.findCloseParticles(particles.get(i), Rc));
//        }
//        System.out.printf("time1: %d\n", System.currentTimeMillis() - time1);

//        List<Particle> check = new ArrayList<>(particles.get(0).getNearParticles());
//        particles.forEach(particle -> particle.setNearParticles(new ArrayList<>()));

//        long time2 = System.currentTimeMillis();
//        indexHash.index(particles);
        particles = indexHash.addNearParticlesWithFastAlgo(particles, Rc, isCircular);

//        System.out.printf("time2: %d\n", System.currentTimeMillis() - time2);
//        System.out.printf("sumCheck: %d\n", particles.stream()
//                .map(particle -> particle.getNearParticles().size())
//                .reduce(0, Integer::sum));

        printResults(particles);
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
            randomParticles.add(new Particle(Math.random(),Math.random(), 0.1, ""+i));
        }
        return randomParticles;
    }

    private static void printResults(List<Particle> particles) throws IOException {
        File positions = new File("./TP1/positions.csv");
        File neighbors = new File("./TP1/neighbors.csv");
        FileWriter positionsFile = new FileWriter(positions);
        FileWriter neighborsFile = new FileWriter(neighbors);

        for (Particle p : particles) {
            positionsFile.write(p.getX().toString() + "," + p.getY().toString() + "," + p.getRadius() + "\n");
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MainTP3 {
    private final static double L = 6; // double because then its get divided so we avoid casting
    private final static double maxVelocity = 2; // max velocity module
    private final static int eventsQty = 10000;
    private final static double smallRadius = 0.2;
    private final static double bigRadius = 0.7;


    public static void main(String[] args) {
        List<Pair<Double, Double>> speedsRange = new ArrayList<>();
//        speedsRange.add(new Pair<>(0.5, 1.));
//        speedsRange.add(new Pair<>(1., 1.5));
//        speedsRange.add(new Pair<>(1.5, 2.));
        speedsRange.add(new Pair<>(0., 2.0));

        List<Integer> particlesQty = new ArrayList<>();
//        particlesQty.add(105);
//        particlesQty.add(110);
//        particlesQty.add(115);
//        particlesQty.add(120);
//        particlesQty.add(125);
        particlesQty.add(130);
//        particlesQty.add(135);


        try {
            for (int i = 0; i < speedsRange.size(); i++) {
                for (int j = 0; j < particlesQty.size(); j++) {
                    List<StatisticsData> statistics = new ArrayList<>();
//                    for (int k = 0; k < 25; k++) {
                        statistics.add(brownianMotion(eventsQty, particlesQty.get(i), new Pair<>(0., 2.), 0, true));
//                    }
//                    double eventsSize = statistics.stream().map(statistics1 -> statistics1.eventsSize).reduce(0. , Double::sum);
//                    double timeTotal = statistics.stream().map(statistics1 -> statistics1.totalTime).reduce(0. , Double::sum);

//                    List<StatisticsData> sortedByTime = statistics.stream().sorted(Comparator.comparingDouble(s -> s.totalTime)).collect(Collectors.toList());
//                    System.out.println(particlesQty.get(i)+" "+eventsSize+" "+timeTotal / eventsSize+" "+eventsSize / timeTotal +" " + sortedByTime.get(6).eventsSize / sortedByTime.get(6).totalTime +" " +sortedByTime.get(19).eventsSize / sortedByTime.get(19).totalTime);
//                    System.out.println("Lista de tiempos: \n" + statistics.toString());
//                    System.out.println();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static StatisticsData brownianMotion(int eventsQty, int particlesQty, Pair<Double, Double> speedRange, int speedI, boolean isSimulation) {
        List<Particle> particles = generateParticles(particlesQty, speedRange);
        saveState(particles, 0.,0, speedI);
        BrownianMotion brownianMotion = new BrownianMotion(particles, L);
        List<Event> events = new ArrayList<>();
        Event lastEvent;
        brownianMotion.calculateEvents();
        double timeStep = 0.05;
        double timeForScreenshot = 0;
        for (int i = 0;  ; i++) {
            brownianMotion.refreshBeforeEvent();
            if (isSimulation && timeForScreenshot < brownianMotion.getTime()){
                saveState(brownianMotion.getParticles(), brownianMotion.getTime(), i, speedI);
                timeForScreenshot += timeStep;
            }
            lastEvent = brownianMotion.performCollision();
            if ( lastEvent != null) {
                brownianMotion.removeAndCalculateEventForParticle(lastEvent.getP1(), lastEvent.getP2());
                if (lastEvent.containsParticle(particles.get(0)) && lastEvent.getP2() == null) {
                    System.out.println("big particle crash in event: "+ i);
                    break;
                }
            }
            else throw new NullPointerException();
            events.add(lastEvent);
        }

//        CompletableFuture.allOf(
//                CompletableFuture.supplyAsync(() -> saveEvents(events, particlesQty))
//        );
        return new StatisticsData( events.size(), events.get(events.size()-1).getTime());
//        System.out.println(particlesQty+"\nCantidad total de colisiones: "+events.size()+"\nTiempo entre colisiones promedio: "+
//                events.get(events.size()-1).getTime() / events.size()+"\nFrecuencia de colisiones: "+events.size() / events.get(events.size()-1).getTime());
    }


    private static Integer saveEvents(List<Event> events, int particlesQty) {
        File positions = new File("./TP3/events"+particlesQty+".csv");
        try {
            FileWriter positionsFile = new FileWriter(positions);
            for (Event event : events) {
                positionsFile.write(event+ "\n");
            }

            positionsFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    private static void saveState(List<Particle> particles, double time,int iteration, int speedIndex) {
        File smallLads = new File("./../positionTP3/"+"smallLads"+iteration+".xyz");
//        File bigboy = new File("./../positionTP3/I"+(speedIndex+1)+"/bigBoy"+iteration+".xyz");
        try {
            FileWriter smallLadsFile = new FileWriter(smallLads);
//            FileWriter bigBoyFile = new FileWriter(bigboy);

            smallLadsFile.write(
                    particles.size()+"\n" +
                    "Lattice=\"6 0.0 0.0 0.0 6 0.0 0.0 0.0 6\"" +
                    "\n");
//            smallLadsFile.write(""+time);
            for (int i = 0 ; i < particles.size()  ; i ++) {
                smallLadsFile.write(particles.get(i).toString() + "\n");
            }
//            bigBoyFile.write(particles.get(0).toString() + "\n");

//            bigBoyFile.close();
            smallLadsFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static List<Particle> generateParticles(int particlesQty, Pair<Double, Double> speedRange) {
        List<Particle> particles = new ArrayList<>();
        if (particlesQty == 140) System.out.println(140);

        ThreadLocalRandom r = ThreadLocalRandom.current();

        particles.add(new Particle(0, 2, bigRadius,L/2,L/2, 0,0));
        int iter =0;
        for (int i = 0; i < particlesQty; iter++) {
            Pair<Double, Double> velocity = getRandomVelocity( r, speedRange);
            double posX = 0.01+smallRadius + (L-2*smallRadius-0.01) * r.nextDouble();
            double posY = 0.01+smallRadius + (L-2*smallRadius-0.01) * r.nextDouble();
            Particle particle = new Particle(i + 1,0.9,0.2, posX, posY, velocity.getValue1(), velocity.getValue2());
            if (!isOverlap(particles, particle)) {
                particles.add(particle);
                i++;
                iter = 0;
            }
            if (iter>1000) {
//                System.out.println(particles.size());
                return generateParticles(particlesQty, speedRange);
            }
        }

        return particles;
    }

    private static Pair<Double, Double> getRandomVelocity(ThreadLocalRandom r, Pair<Double, Double> speedRange) {
        double speed = r.nextDouble() * (speedRange.getValue2() - speedRange.getValue1()) + speedRange.getValue1();
        double angle = r.nextDouble() * 2 * Math.PI;

        return new Pair<>(speed * Math.cos(angle), speed * Math.sin(angle));
    }

    private static double getRandomPos(ThreadLocalRandom r) {
        double radius = 0.2;
        return r.nextDouble() * (L - 2*radius) + radius;
    }

    private static boolean isOverlap(List<Particle> particles , Particle particle) {
        for (Particle particle2: particles) {
            if (particle.isOverlap(particle2)) return true;
        }
        return false;
    }


}

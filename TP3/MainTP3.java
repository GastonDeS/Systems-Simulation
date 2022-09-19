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
    private final static double L = 6;
    private final static double maxVelocity = 2;
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
                    statistics.add(brownianMotion(eventsQty, particlesQty.get(i), new Pair<>(0., 2.), true));
//                    }
                    double eventsSize = statistics.stream().map(statistics1 -> statistics1.eventsSize).reduce(0. , Double::sum);
                    double timeTotal = statistics.stream().map(statistics1 -> statistics1.totalTime).reduce(0. , Double::sum);

                    double desvFrecxSec = desvFrecXsec(statistics, eventsSize / timeTotal);
                    double desvFrec = desvFrec(statistics, timeTotal / eventsSize);
                    System.out.println(particlesQty.get(i)+" "+eventsSize+" "+timeTotal / eventsSize+" "+desvFrec+" "+eventsSize / timeTotal +" " +desvFrecxSec);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static double desvFrecXsec(List<StatisticsData> statisticsData, double medium) {
        double sum = 0.;
        for (StatisticsData s : statisticsData) {
            sum += Math.pow( s.eventsSize / s.totalTime - medium,2);
        }
        return Math.sqrt(sum / statisticsData.size());
    }

    private static double desvFrec(List<StatisticsData> statisticsData, double medium) {
        double sum = 0.;
        for (StatisticsData s : statisticsData) {
            sum += Math.pow( s.totalTime / s.eventsSize - medium,2);
        }
        return Math.sqrt(sum / statisticsData.size());
    }

    private static StatisticsData brownianMotion(int eventsQty, int particlesQty, Pair<Double, Double> speedRange, boolean isSimulation) {
        List<Particle> particles = generateParticles(particlesQty, speedRange);
        if (isSimulation)
            saveState(particles,0);
        BrownianMotion brownianMotion = new BrownianMotion(particles, L);
        List<Event> events = new ArrayList<>();
        Event lastEvent;
        brownianMotion.calculateEvents();
        double timeStep = 0.05;
        double timeForScreenshot = timeStep; // the first is already saved
        for (int i = 0; i < eventsQty ; i++) {
            brownianMotion.refreshBeforeEvent();
            if (isSimulation && timeForScreenshot < brownianMotion.getTime()){
                saveState(brownianMotion.getParticles(), i);
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

        if (isSimulation)
            saveEvents(events, particlesQty);
        return new StatisticsData( events.size(), events.get(events.size()-1).getTime());
    }


    private static Integer saveEvents(List<Event> events, int particlesQty) {
        File positions = new File("./../events"+particlesQty+".csv");
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

    private static void saveState(List<Particle> particles, int iteration) {
        File smallLads = new File("./../positionTP3/positions"+iteration+".xyz");
        try {
            FileWriter smallLadsFile = new FileWriter(smallLads);

            smallLadsFile.write(
                    particles.size()+"\n" +
                            "Lattice=\"6 0.0 0.0 0.0 6 0.0 0.0 0.0 6\"" +
                            "\n");
            for (int i = 0 ; i < particles.size()  ; i ++) {
                smallLadsFile.write(particles.get(i).toString() + "\n");
            }

            smallLadsFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static List<Particle> generateParticles(int particlesQty, Pair<Double, Double> speedRange) {
        List<Particle> particles = new ArrayList<>();

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

    private static boolean isOverlap(List<Particle> particles , Particle particle) {
        for (Particle particle2: particles) {
            if (particle.isOverlap(particle2)) return true;
        }
        return false;
    }
}

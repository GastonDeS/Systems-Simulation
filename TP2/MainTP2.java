import java.util.ArrayList;
import java.util.List;

public class MainTP2 {
    final static Integer PARTICLES_AMOUNT = 1;
    final static Double SPEED = 0.03;
    final static Double R_C = 0.1;

    public static void main(String[] args) {
        System.out.println("TP2");

        generateRandomParticles(PARTICLES_AMOUNT, SPEED);

    }

    private static List<Agent> generateRandomParticles(int amount, double speed) {
        List<Agent> randomAgents = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = Math.random() * 360;
            Pair<Double, Double> velocity = new Pair<>(Math.cos(angle) *speed, Math.sin(angle) *speed);
            randomAgents.add(new Agent( Math.random(), Math.random(), 0., velocity, ""+i));
        }
        return randomAgents;
    }

    private static Double speed(Pair<Double, Double> velocity ) {
        return Math.sqrt(Math.pow(velocity.getValue1(), 2) + Math.pow(velocity.getValue2(), 2));
    }

}
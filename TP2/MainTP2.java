import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainTP2 {
    final static Integer AGENTS_AMOUNT = 1000;
    final static Double SPEED = 0.03;
    final static Double R_C = 0.1;

    public static void main(String[] args) throws IOException {
        System.out.println("TP2");

        List<Agent> agentList = generateRandomParticles(AGENTS_AMOUNT, SPEED);

        Index index = new Index(R_C, 1.);
        for (int i = 0; i < 100; i++) {
            printResults(agentList, i);
            index.index(agentList);
            index.addNearAgentsWithFastAlgo(agentList, R_C, true);

            index.resetIndex();
            agentList.forEach(Agent::resetNearAgents);
            agentList.forEach(Agent::nextStep);
        }

    }

    private static List<Agent> generateRandomParticles(int amount, double speed) {
        List<Agent> randomAgents = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = Math.random() *2 * Math.PI;
            System.out.println(speed(new Pair<>(speed * Math.cos(angle) , speed * Math.sin(angle))));
            randomAgents.add(new Agent( Math.random(), Math.random(), 0., speed, angle, ""+i));
        }
        return randomAgents;
    }

    private static void printResults(List<Agent> agents, int iteration) throws IOException {
        File positions = new File("./TP2/position/positions"+iteration+".txt");
        FileWriter positionsFile = new FileWriter(positions);

        positionsFile.write(agents.size()+"\n\n");
        for (Agent p : agents) {
            positionsFile.write(p.getX().toString() + " " + p.getY().toString() + " " + p.getRadius() + "\n");
        }

        positionsFile.close();
    }

    private static Double speed(Pair<Double, Double> velocity ) {
        return Math.sqrt(Math.pow(velocity.getValue1(), 2) + Math.pow(velocity.getValue2(), 2));
    }

}
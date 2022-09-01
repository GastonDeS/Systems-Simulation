import utils.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainTP2 {

    static List<String> tokens;

    static {
        try {
            tokens = Parser.parse("TP2/static.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    final static Integer AGENTS_AMOUNT = Integer.valueOf(tokens.get(Constants.AGENTS_QTY.ordinal()));
    final static Double SPEED = Double.valueOf(tokens.get(Constants.SPEED.ordinal()));
    final static Double R_C = Double.valueOf(tokens.get(Constants.R_C.ordinal()));
    final static Integer STEPS = Integer.valueOf(tokens.get(Constants.STEPS.ordinal()));
    final static Double ETA = Double.valueOf(tokens.get(Constants.ETA.ordinal()));
    final static Double L = Double.valueOf(tokens.get(Constants.L.ordinal()));



    public static void main(String[] args) throws IOException {
        System.out.println("TP2");
        List<Agent> agentList = generateRandomParticles(AGENTS_AMOUNT, SPEED, ETA);

        Index index = new Index(R_C, L);
        for (int j = 0 ; j < 25 ; j++)
            for (int i = 0; i < STEPS; i++) {
                printResults(agentList, i, j);
                index.index(agentList);
                index.addNearAgentsWithFastAlgo(agentList, R_C, true);

                agentList.forEach(Agent::nextStep);
                index.resetIndex();
                agentList.forEach(Agent::resetNearAgents);
            }

    }

    private static List<Agent> generateRandomParticles(int amount, double speed, double eta) {
        List<Agent> randomAgents = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = Math.random() * 2 * Math.PI;
            randomAgents.add(new Agent( Math.random(), Math.random(), 0., speed, angle, ""+i, eta));
        }
        return randomAgents;
    }

    private static void printResults(List<Agent> agents, int iteration, int sameEtaIteration) throws IOException {
        File positions = new File("./TP2/position/positions_eta:" + ETA + "_" + sameEtaIteration + "_" + iteration+".xyz");
        FileWriter positionsFile = new FileWriter(positions);

        positionsFile.write(agents.size()+"\n" +
                "Lattice=\"3 0.0 0.0 0.0 3 0.0 0.0 0.0 3\"" +
                "\n");
        for (Agent p : agents) {
            positionsFile.write(p.getX().toString() + " " + p.getY().toString() + " " + p.getAngle() + "\n");
        }

        positionsFile.close();
    }

    private static Double speed(Pair<Double, Double> velocity ) {
        return Math.sqrt(Math.pow(velocity.getValue1(), 2) + Math.pow(velocity.getValue2(), 2));
    }

}
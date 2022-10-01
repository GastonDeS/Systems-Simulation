package utils;

import utils.algorithms.Algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Oscillator {
    private final int r0 = 1;
    // Set in main
    private final Algorithm algorithm;
    private final double tf;
    private final int steps;

    public Oscillator(Algorithm algorithm, double tf, int steps) {
        this.algorithm = algorithm;
        this.tf = tf;
        this.steps = steps;
    }

    public void simulate(double deltaT, double gamma, double k, double m) {
        File file = createFile();
        try (FileWriter data = new FileWriter(file)) {
            double initialSpeed = -r0 * gamma/(2 * m);
            double initialForce = -k * r0 - gamma * initialSpeed;
            Particle current = new Particle('-', m, 0, r0, 0, initialSpeed, 0, initialForce, 0);
            Particle future;
            Particle previous = null;
            double time = 0;

            while (time < tf) {
                future = algorithm.update(previous, current, deltaT, time);
                printResult(data, time, current);
                System.out.println(current);
                previous = current;
                current = future;
                time += deltaT;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printResult(FileWriter data, Double time, Particle current) throws IOException {
        data.write(time + " " + current.getPosX()+" "+current.getPosY()+"\n");
    }
    private File createFile() {
        return new File("TP4/data/" + algorithm.getName() + ".ssv");
    }
}

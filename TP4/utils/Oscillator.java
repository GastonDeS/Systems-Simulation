package utils;

import utils.algorithms.Algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;

public class Oscillator {
    private final double m = 70.;
    private final double k = Math.pow(10,4);
    private final double gamma = 100.;
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

    public void simulate(double deltaT) {
        File file = createFile();
        try (FileWriter data = new FileWriter(file)) {
            double initialSpeed = -r0 * gamma/(2 * m);
            double initialForce = -k * r0 - gamma * initialSpeed;
            Particle current = new Particle('-', 1, 0, r0, 0, initialSpeed, 0, initialForce, 0);
            Particle previous = null;
            double time = 0;
            int iter = 1;

            while (time < tf) {
                previous = current;
                current = algorithm.update(null, current, deltaT);
                if (iter % steps == 0) {
                    printResult(data, time, current);
                    System.out.println(previous.toString());
                }
                time += deltaT;
                iter++;
            }
        } catch (Exception e) {
            System.out.println("ERROR: Create folder data");
        }
    }

    private void printResult(FileWriter data, Double time, Particle current) throws IOException {
        data.write(time + " " + current.getPosX()+" "+current.getPosY()+"\n");
    }
    private File createFile() {
        return new File("TP4/data/" + algorithm.getName() + ".ssv");
    }
}

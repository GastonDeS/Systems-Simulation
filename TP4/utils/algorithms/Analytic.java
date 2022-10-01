package utils.algorithms;

import utils.Particle;

public class Analytic implements Algorithm {
    private final double K;
    private final double gamma;
    private final double A;

    public Analytic(double k, double gamma, double amplitude) {
        this.K = k;
        this.gamma = gamma;
        this.A = amplitude;
    }

    @Override
    public Particle update(Particle previous, Particle current, double deltaT, double t){
        if (current == null) throw new NullPointerException("No current particle to update with");
        System.out.println("MASS: " + current.getMass() + "\nK: " + K + "\nGAMMA: " + gamma);
        double rNext = A * Math.exp(-(gamma/(2*current.getMass()))*t) * Math.cos(Math.sqrt(((K/current.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(current.getMass(), 2))))) * t);;
        double vNext = A *(-(gamma/(2*current.getMass())) * Math.exp(-(gamma/(2*current.getMass()))*t) -
                Math.pow(((K/current.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(current.getMass(), 2)))),0.5) *
                        Math.exp(-(gamma/(2*current.getMass()))*t) * Math.sin(Math.pow(((K/current.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(current.getMass(), 2)))),0.5) *
                        t));
        double aNext = -K * rNext - gamma * vNext;
        current.setPosX(rNext);
        current.setVelX(vNext);
        current.setAccX(aNext);
        return current;
    }


    @Override
    public String getName() {
        return "analitica";
    }
}


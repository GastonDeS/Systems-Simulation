package utils.algorithms;

import utils.Particle;

public class Analytic implements Algorithm {
    private final double k;
    private final double gamma;

    public Analytic(double k, double gamma) {
        this.k = k;
        this.gamma = gamma;
    }

    @Override
    public Particle update(Particle previous, Particle current, double deltaT) {
        if (current == null) throw new NullPointerException("No current particle to update with");
        double r = current.getPosX();
        double v = current.getVelX();
        double f = -k * r - gamma * v;
        double rNext = r * Math.exp(-gamma * deltaT / current.getMass()) * Math.cos(Math.sqrt(k / current.getMass() - Math.pow(gamma, 2) / (4 * Math.pow(current.getMass(), 2))) * deltaT);
        double vNext = -r * Math.exp(-gamma * deltaT / current.getMass()) * Math.sqrt(k / current.getMass() - Math.pow(gamma, 2) / (4 * Math.pow(current.getMass(), 2))) * Math.sin(Math.sqrt(k / current.getMass() - Math.pow(gamma, 2) / (4 * Math.pow(current.getMass(), 2))) * deltaT);
        double aNext = -k * rNext - gamma * vNext;
        return new Particle('-', current.getMass(), current.getRadius(), rNext, 0, vNext, 0, aNext, 0);
    }
//        double initialSpeed = -A*gamma/(2*m);
//        int initialPosition = A;
//        double initialForce = -K*initialPosition - gamma*initialSpeed;
//        Particle particle = new Particle('-', 1, 0, initialPosition, 0,
//                initialSpeed, 0, initialForce, 0);
//        for(double t = 0 ; t < tf ; t+=0.05){
//            double r = A*Math.exp(-gamma*t/(2*m))*Math.cos(Math.sqrt(K/m - Math.pow(gamma/(2*m), 2))*t);
//            System.out.println(r);
//        }


    @Override
    public String getName() {
        return "Analytic";
    }
}


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
    public Particle update(Particle previous, Particle current, double deltaT, double currTime){
        if (current == null) throw new NullPointerException("No current particle to update with");
        double r = current.getPosX();
        double v = current.getVelX();
        double rNext = 1 * Math.exp(-gamma * currTime / current.getMass()) *
                Math.cos(Math.sqrt(k / current.getMass() - Math.pow(gamma, 2) / (4 * Math.pow(current.getMass(), 2)))
                        * currTime);
        double vNext = -1 * Math.exp(-gamma * currTime / current.getMass()) * Math.sqrt(k / current.getMass() - Math.pow(gamma, 2) /
                (4 * Math.pow(current.getMass(), 2))) * Math.sin(Math.sqrt(k / current.getMass() - Math.pow(gamma, 2) / (4 * Math.pow(current.getMass(), 2))) * currTime);
        double aNext = -k * rNext - gamma * vNext;
        return new Particle('-', current.getMass(), current.getRadius(), rNext, 0, vNext, 0, aNext, 0);
    }

//        for(double t = 0 ; t < tf ; t+=0.05){
//            double r = A*Math.exp(-gamma*t/(2*m))*Math.cos(Math.sqrt(K/m - Math.pow(gamma/(2*m), 2))*t);
//            System.out.println(r);
//        }


    @Override
    public String getName() {
        return "Analytic";
    }
}


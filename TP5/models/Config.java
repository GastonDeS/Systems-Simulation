package models;

public class Config {
    private int N;
    private double deltaT;
    private double Vdz;
    private double tau;
    private double ApHuman;
    private double BpHuman;
    private double ApZombie;
    private double BpZombie;
    private double ApWall;
    private double BpWall;
    private double maxTime;
    private int steps;

    public Config withN(int n) {
        this.N = n;
        return this;
    }

    public Config withDeltaT(double deltaT) {
        this.deltaT = deltaT;
        return this;
    }

    public Config withVdz(double Vdz) {
        this.Vdz = Vdz;
        return this;
    }

    public Config withTau(double tau) {
        this.tau = tau;
        return this;
    }

    public Config withApHuman(double Ap) {
        this.ApHuman = Ap;
        return this;
    }

    public Config withBpHuman(double Bp) {
        this.BpHuman = Bp;
        return this;
    }

    public Config withApZombie(double Ap) {
        this.ApZombie = Ap;
        return this;
    }

    public Config withBpZombie(double Bp) {
        this.BpZombie = Bp;
        return this;
    }

    public Config withApWall(double Ap) {
        this.ApWall = Ap;
        return this;
    }

    public Config withBpWall(double Bp) {
        this.BpWall = Bp;
        return this;
    }

    public Config withMaxTime(double maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public Config withSteps(int steps) {
        this.steps = steps;
        return this;
    }

    /*
        GETTERS
     */

    public int getN() {
        return N;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public double getVdz() {
        return Vdz;
    }

    public double getTau() {
        return tau;
    }

    public double getApHuman() {
        return ApHuman;
    }

    public double getBpHuman() {
        return BpHuman;
    }

    public double getApZombie() {
        return ApZombie;
    }

    public double getBpZombie() {
        return BpZombie;
    }

    public double getApWall() {
        return ApWall;
    }

    public double getBpWall() {
        return BpWall;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public int getSteps() {
        return steps;
    }
}

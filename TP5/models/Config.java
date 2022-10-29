package models;

public class Config {
    private int N;
    private double deltaT;
    private double Vdz;

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
}

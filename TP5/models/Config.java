package models;

public class Config {

    private int N;

    public Config withN(int n) {
        this.N = n;
        return this;
    }

    public int getN() {
        return N;
    }
}

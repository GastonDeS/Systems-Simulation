public class Event {
    private final double time;
    private final int p1; //index of particle in list
    private final int p2;

    public Event(double time, int p1, int p2) {
        this.time = time;
        this.p1 = p1;
        this.p2 = p2;
    }

    public double getTime() {
        return time;
    }

    public int getP1() {
        return p1;
    }

    public int getP2() {
        return p2;
    }
}

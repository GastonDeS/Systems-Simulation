public class Event {
    private final double time;
    private final Particle p1; //index of particle in list
    private final Particle p2;

    public Event(double time, Particle p1, Particle p2) {
        this.time = time;
        this.p1 = p1;
        this.p2 = p2;
    }

    public double getTime() {
        return time;
    }

    public Particle getP1() {
        return p1;
    }

    public Particle getP2() {
        return p2;
    }
}

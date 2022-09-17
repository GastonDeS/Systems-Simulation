public class Event {
    private final double time;
    private final Particle p1;
    private final Particle p2;
    private final char direction; // '-' means collision between two particles has taken place

    public Event(double time, Particle p1, Particle p2, char direction) {
        this.time = time;
        this.p1 = p1;
        this.p2 = p2;
        this.direction = direction;
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

    public char getDirection() {
        return direction;
    }
}

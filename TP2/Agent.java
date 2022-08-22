import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Agent {
    private String label;
    private Double x,y;
    private List<Agent> nearAgents;
    private Double radius;
    private Double speed;
    private Double angle;

    public Agent(Double x, Double y, Double radius, Double speed, Double angle, String label) {
        this.x = x;
        this.y = y;
        this.nearAgents = new ArrayList<>();
        this.speed = speed;
        this.angle = angle;
        this.label = label;
        this.radius = radius;
    }

    public Double distance(Agent Agent) {
        return Math.sqrt(Math.pow(this.x - Agent.getX(), 2) + Math.pow(this.y - Agent.getY(), 2));
    }

    public Double distanceToBorder(Agent Agent) {
        return Math.sqrt(Math.pow(this.x - Agent.getX(), 2) + Math.pow(this.y - Agent.getY(), 2)) - this.radius - Agent.getRadius();
    }

    public Double circularDistance(Agent Agent, Double L) {
        Double distX = Math.abs(this.x - Agent.getX());
        Double distY = Math.abs(this.y - Agent.getY());
        return Math.sqrt(Math.pow(Math.min(distX, L - distX), 2) + Math.pow(Math.min(distY, L - distY), 2)) - this.radius - Agent.getRadius();
    }
    
    public void nextStep() {
        double eta = 0.1;
        angle = promNearAngles() + (Math.random() * eta) - eta;
        this.move(speed + Math.cos(angle), speed + Math.sin(angle));
    }

    private double promNearAngles() {
        double sinProm = nearAgents.stream().map((agent) -> Math.sin(angle)).reduce( 0., Double::sum) / nearAgents.size();
        double cosProm = nearAgents.stream().map((agent) -> Math.cos(angle)).reduce( 0., Double::sum) / nearAgents.size();
        return Math.atan2(sinProm, cosProm);
    }

    public void move(Double distX, Double distY) {
        this.x += distX;
        this.y += distY;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Agent> getNearAgents() {
        return nearAgents;
    }

    public void setNearAgents(List<Agent> nearAgents) {
        this.nearAgents = nearAgents;
    }

    public void resetNearAgents() {this.nearAgents = new ArrayList<>();}

    public void addNearAgents(List<Agent> Agents) {
        this.nearAgents.addAll(Agents);
    }

    public void addNearAgent(Agent Agent) {
        this.nearAgents.add(Agent);
    }

    public Double getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent Agent = (Agent) o;
        return Objects.equals(x, Agent.x) &&
                Objects.equals(y, Agent.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

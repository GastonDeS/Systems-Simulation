import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Particle {
    private String label;
    private Double x,y;
    private List<Particle> nearParticles;

    public Particle(Double x, Double y, String label) {
        this.x = x;
        this.y = y;
        this.nearParticles = new ArrayList<>();
        this.label = label;
    }

    public Double distance(Particle particle) {
        return Math.sqrt(Math.pow(this.x - particle.getX(), 2) + Math.pow(this.y - particle.getY(), 2));
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

    public List<Particle> getNearParticles() {
        return nearParticles;
    }

    public void setNearParticles(List<Particle> nearParticles) {
        this.nearParticles = nearParticles;
    }

    public void addNearParticle(Particle particle) {
        this.nearParticles.add(particle);
    }

    public Double getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + x +
                ", y=" + y +
                '}';
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
        Particle particle = (Particle) o;
        return Objects.equals(x, particle.x) &&
                Objects.equals(y, particle.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

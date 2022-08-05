public class Particle {
    private Double x,y;

    public Particle(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double distance(Particle particle) {
        return Math.sqrt(Math.pow(this.x - particle.getX(), 2) + Math.pow(this.y - particle.getY(), 2));
    }

    public void move(Double distX, Double distY) {
        this.x += distX;
        this.y += distY;
    }

    public Double getX() {
        return x;
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
}

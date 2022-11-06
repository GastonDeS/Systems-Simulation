package models;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Person {
    // TODO: check values
    protected final static double CONVERSION_TIME = 7.0;
    protected final static double Rmin = 0.1;
    protected final static double Rmax = 0.3;
    protected static final double beta = 0.5;

    private final String id;
    protected Point vel;
    protected Point pos;
    protected double radius;
    protected PersonState state;
    private double timeLeft;
    protected Point desiredPos;
    protected double desiredSpeed;
    protected double deltaAngle; // Limit angle of vision
    protected double limitVision;
    protected double Ap;
    protected double Bp;
    private final double tau;
    protected final double ApWall;
    protected final double BpWall;

    private boolean crashedFlag = false;

    public Person(String id, double positionX, double positionY, double speedX, double speedY, Config config) {
        this.id = id;
        this.vel = new Point(0 ,0);
        this.pos = new Point(positionX, positionY);
        this.vel = new Point(speedX, speedY);
        this.radius = Rmin;
        this.timeLeft = CONVERSION_TIME;
        this.state = PersonState.WALKING;
        this.tau = config.getTau();
        this.ApWall = config.getApWall();
        this.BpWall = config.getBpWall();
    }

    protected double getDirectionForSpeed() {
        return getDirection(vel.x, vel.y);
    }

    protected double getDirection(double x, double y) {
        if (x == 0.0) {
            return y > 0 ? Math.PI / 2 : Math.PI * 3 / 2;
        } else {
            if (y > 0 && x > 0) return Math.atan(y / x);
            if (x < 0) return Math.atan(y / x) + Math.PI;
            else return Math.atan(y / x) + 2 * Math.PI;
        }
    }
    
    protected boolean isColliding(Person person) {
        return !this.equals(person) && pos.dist(person.pos) <= radius + person.radius;
    }

    protected boolean isTouchingCircularWall(double wallRadius) {
        return Math.sqrt(Math.pow(pos.x, 2) + Math.pow(pos.y, 2)) >= wallRadius - radius;
    }

    protected void startConversion() {
        this.state = PersonState.CONVERTING;
    }

    protected void reduceTimeLeft(double deltaT) {
        if (this.timeLeft <= 0) {
            this.state = PersonState.WALKING;
            this.timeLeft = CONVERSION_TIME;
        }
        this.timeLeft -= deltaT;
    }

    protected Point getRandomPos() {
        double angle = Math.random() * 2 * Math.PI;
        return new Point(Math.cos(angle) * Math.random() * (Room.getWallRadius() - 1) , Math.sin(angle) * Math.random() * (Room.getWallRadius() - 1));
    }

    protected boolean isOnVision(Point pos2, double angle) {
        double angleBetweenEntities = getDirection((pos2.x - pos.x), (pos2.y - pos.y));
        // angle between entities is that but y need to think it more
        return (angleBetweenEntities <= angle + deltaAngle
                && angleBetweenEntities >= angle - deltaAngle)
                && (Math.sqrt(Math.pow(pos2.x - pos.x, 2) + Math.pow(pos2.y - pos.y, 2)) <= limitVision);
    }


    protected Optional<Point> getNearestWallOnSight() {
        double angle = getDirectionForSpeed(); // m
        double angleMax = angle + deltaAngle; // m + delta
        double angleMin = angle - deltaAngle; // m - delta

        Point distNormalized = pos.normalize();
        distNormalized.x *= Room.getWallRadius();
        distNormalized.y *= Room.getWallRadius();

        // Si es el mas cercano lo devuelvo
        if (isOnVision(distNormalized, angle)) return Optional.of(distNormalized);

        // Obtengo los dos puntos de vision en los bordes de la vision
        Optional<Point> minPoint = getWallPoint(angleMin);
        Optional<Point> maxPoint = getWallPoint(angleMax);

        // si los dos existen devuelvo el mas cercano
        if (minPoint.isPresent() && maxPoint.isPresent()) {
            return pos.dist(minPoint.get()) < pos.dist(maxPoint.get()) ? minPoint : maxPoint;
        }

        // si solo uno existe devuelvo ese
        if (minPoint.isPresent()) return minPoint;

        // si no existe va en Optional.nullable si no se devuelve el propio max point
        return maxPoint;
    }

    // (m^2 +1) X^2 + 2mb * X + b^2 + R^2 = 0
    private Optional<Point> getWallPoint(double angle) {
        double m = angle < Math.PI /2 ? Math.tan(angle) : Math.tan(angle - Math.PI);
        double b = pos.y - m * pos.x; // ordenada al origen
        double R = Room.getWallRadius(); // R de la circunferencia

        Point xSol = solveQuadraticEquation(Math.pow(m,2) + 1, 2 * m * b, Math.pow(b,2) - Math.pow(R,2));
        
        // De las dos soluciones devuelvo la que este a mi vista
        Point firstSol = getYOnLine(xSol.x, m, b);
        double angleFirstSol = getDirection((firstSol.x - pos.x), (firstSol.y - pos.y));
        if (angleFirstSol == angle && isOnVision(firstSol, angle)) {
            return Optional.of(firstSol);
        }
        Point secondSol = getYOnLine(xSol.y, m, b);
        double angleSecondSol = getDirection((secondSol.x - pos.x), (secondSol.y - pos.y));
        if (angleSecondSol == angle && isOnVision(secondSol, angle)) {
            return Optional.of(secondSol);
        }
        return Optional.empty();
    }

    private Point getYOnLine(double x, double m, double b) {
        return new Point(x, m * x + b);
    }

    private Point solveQuadraticEquation(double a, double b, double c) {
        return new Point((-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a),
                (-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a));
    }

    protected void updateRadius(double deltaT) {
        if (radius < Rmax) {
            radius += Rmax / (tau / deltaT);
        } else {
            radius = Rmax;
        }
    }

    protected void updateVelocityForAvoidance(Point nc) {
        Point eit = desiredPos.sub(pos).normalize();
        Point eia = nc.add(eit).normalize();
        double magnitude = desiredSpeed * Math.pow((radius - Rmin)/(Rmax - Rmin), beta);
        vel.setLocation(eia.prod(magnitude));
    }

    protected <T extends Person> T getNearestEntity(List<T> entities) {
        T nearest = entities.get(0);
        double minDist = pos.dist(nearest.pos);
        for (T entity : entities) {
            double dist = pos.dist(entity.pos);

            if (dist < minDist) {
                minDist = dist;
                nearest = entity;
            }
        }
        return nearest;
    }

    protected Point calculateEij(Point obstacle) {
        return pos.sub(obstacle).normalize();
    }

    protected Point calculateHij(Point Eij, double Ap, double Bp) {
        double dist = pos.dist(Eij);
        Point desired = desiredPos.sub(pos).normalize();
        double mul = Ap * Math.exp(-dist/Bp) * desired.dotProduct(Eij);
        return Eij.prod(mul);
    }

    protected Point handleAvoidance(List<Human> humans, List<Zombie> zombies) {
        return new Point(0,0);
    }

    public void updateRadiusAndPosition(double deltaT) {
        if (state == PersonState.CONVERTING) return;
        if (crashedFlag) {
            this.radius = Rmin;
            this.crashedFlag = false;
        }

        // Update position
        pos.x  += vel.prod(deltaT).x;
        pos.y  += vel.prod(deltaT).y;
    }

    /**
     * Update position and velocity of humans and zombies
     * @param deltaT
     * @param zombies
     * @param humans
     */
    protected void update(double deltaT, List<Zombie> zombies, List<Human> humans) {
        getDesiredPos(zombies, humans);

        // Check if there are collisions and get escape velocity
        Optional<Point> Ve;
        if (this instanceof Human) Ve = handleCollisions(humans);
        else Ve = handleCollisions(zombies);

        if (Ve.isPresent()) {
            vel.setLocation(Ve.get());
            this.crashedFlag = true;
        } else  {
            updateRadius(deltaT);
            Point nc = handleAvoidance(humans, zombies);
            updateVelocityForAvoidance(nc);
        }



    }

    /**
     * @param persons if the person is a zombie, the list has to be humans,
     *               if the person is a human, the list has to be zombies
     * @return the goal position of the person
     */
    protected abstract <T extends Person> Optional<Point> getGoalPosition(List<T> persons);

    protected <T extends Person>Optional<Point> handleCollisions(List<T> persons) {
        List<T> personsColliding = persons.stream()
                .filter(this::isColliding)
                .collect(Collectors.toList());

        Optional<Point> Ve = Optional.empty();

        if (isTouchingCircularWall(Room.getWallRadius())) {
            Point wallDir = pos.normalize();
            boolean hourlyRotation = Math.abs(Math.atan(vel.y/vel.x)) < Math.PI/4;
            double newVelAngle = hourlyRotation ? getDirection(wallDir.x, wallDir.y) + Math.PI/2 + Math.PI / 10: getDirection(-wallDir.x, -wallDir.y) - Math.PI/2 + Math.PI / 10;
            Ve = Optional.of(new Point(
                    Math.cos(newVelAngle) * desiredSpeed,
                    Math.sin(newVelAngle) * desiredSpeed
            ));
        } else if (!personsColliding.isEmpty()) {
            Point eij = getEij(personsColliding);
            Ve = Optional.of(getVeFromEij(eij));
        }
        return Ve;
    }

    protected abstract Point getVeFromEij(Point eij);

    protected <T extends Person> Point getEij(List<T> humansColliding) {
        List<Point> IJs = getEijs(humansColliding);
        Point IJ = new Point(0, 0);
        for (Point IJi : IJs) {
            IJ.x += IJi.x;
            IJ.y += IJi.y;
        }
        return IJ.normalize();
    }

    private <T extends Person> List<Point> getEijs(List<T> humansColliding) {
        return humansColliding.stream()
                .map(h -> {
                    Point eij = new Point((h.pos.x - pos.x) , (h.pos.y - pos.y));
                    double norm = Math.sqrt(Math.pow(eij.x, 2) + Math.pow(eij.y, 2));
                    eij.x /= norm;
                    eij.y /= norm;
                    return eij;
                })
                .collect(Collectors.toList());
    }

    protected abstract void getDesiredPos(List<Zombie> zombies, List<Human> humans);

    @Override
    public String toString() {
        return id + " " + pos.x + " " + pos.y + " " + radius;
    }

    /*
        GETTERS
     */

    public double getTimeLeft() {
        return timeLeft;
    }

    public PersonState getState() {
        return state;
    }

    public String getId() {
        return id;
    }
}

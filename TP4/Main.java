public class Main {

    static double m = 70.;
    static double k = Math.pow(10,4);
    static double gamma = 100.;
    static int tf = 5;
    static int A = 1;

    public static void main(String[] args) {
        System.out.println("TP4");
        double initialSpeed = -A*gamma/(2*m);
        int initialPosition = A;
        Particle particle = new Particle('-', 1, 0, initialPosition, 0, initialSpeed, 0);
        for(double t = 0 ; t < tf ; t+=0.05){
            double r = A*Math.exp(-gamma*t/(2*m))*Math.cos(Math.sqrt(k/m - Math.pow(gamma/(2*m), 2))*t);
            System.out.println(r);
        }


    }
}

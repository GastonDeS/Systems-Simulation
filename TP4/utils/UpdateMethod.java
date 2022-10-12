package utils;

public enum UpdateMethod {
    EULER("euler"),
    EULER_MODIFIED("euler-modified"),
    BEEMAN("beeman"),
    VERLET("verlet"),
    GEAR_PREDICTOR_CORRECTOR("gear-predictor"),
    ANALYTIC("analytic");

    public String name;

    UpdateMethod(String name) {
        this.name = name;
    }
}

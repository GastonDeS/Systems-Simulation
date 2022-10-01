package utils;

public enum UpdateMethods {
    EULER("euler"),
    EULER_MODIFIED("euler-modified"),
    BEEMAN("beeman"),
    VERLET("verlet"),
    GEAR_PREDICTOR_CORRECTOR("gear-predictor");

    public String name;
    UpdateMethods(String name) {
        this.name = name;
    }
}

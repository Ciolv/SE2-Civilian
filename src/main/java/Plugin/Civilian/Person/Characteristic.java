package Plugin.Civilian.Person;

public enum Characteristic {
    SUSPICIOUS(0.25),
    CONFUSED(0.50),
    HECTIC(1.50),
    SLOW(0.75),
    FAST(1.25),
    TARGET_ORIENTED(0.80);

    public double value;
    private Characteristic(double value){
        this.value=value;
    }
}



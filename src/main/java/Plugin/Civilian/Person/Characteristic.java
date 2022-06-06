package Plugin.Civilian.Person;

public enum Characteristic {
    SUSPICIOUS(0.4),
    CONFUSED(0.8),
    HECTIC(0.6),
    SLOW(0.7),
    DEFAULT(1),
    FAST(2),
    CALM(3.3),
    TARGET_ORIENTED(5),
    BUSY(2.5);

    public double value;
    Characteristic(double value){
        this.value=value;
    }

    /**
     * Get a Characteristic based on its value
     *
     * @param value The value of the Characteristic to determine
     * @return The Characteristic that matches the value, or null if there exists no matching Characteristic
     */
    public static  Characteristic fromValue(double value) {
        for (Characteristic type :
                Characteristic.values()) {
            if (value == type.value) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get a Characteristic based on its string name
     *
     * @param name The string name of the Characteristic
     * @return The Characteristic that matches the name, or null if there exists no matching Characteristic
     */
    public static Characteristic fromName(String name) {
        for (Characteristic type :
                Characteristic.values()) {
            if (name.equals(type.name())) {
                return type;
            }
        }
        return null;
    }
}



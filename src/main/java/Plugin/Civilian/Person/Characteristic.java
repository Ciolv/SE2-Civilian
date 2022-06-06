package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public enum Characteristic {
    SUSPICIOUS(0.4),
    CONFUSED(0.8),
    HECTIC(0.6),
    SLOW(0.7),
    DEFAULT(1),
    FAST(2),
    CALM(3.3),
    TARGET_ORIENTED(5),
    BUSSY(2.5);


    public double value;
    private Characteristic(double value){
        this.value=value;
    }

    public double getValue() {
        return value;
    }

    public static Characteristic fromValue(double value) {
        for (Characteristic type :
                Characteristic.values()) {
            if (value == type.value) {
                return type;
            }
        }
        return null;
    }

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



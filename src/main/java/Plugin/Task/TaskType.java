package Plugin.Task;

public enum TaskType {
    WALKING("Walk"),
    CLEANING("Clean"),
    TRANSPORTING_LUGGAGE("Transport luggage"),
    BUY_TICKET("Buy ticket"),
    SELL_TICKET("Sell ticket"),
    ASK_FOR_DIRECTION("Ask for direction"),
    TELL_DIRECTION("Tell direction");

    public String value;
    TaskType(String value){
        this.value=value;
    }

    /**
     * Get a TaskType based on its value
     *
     * @param value The value of the TaskType to determine
     * @return The TaskType that matches the value, or null if there exists no matching TaskType
     */
    public static TaskType fromValue(String value) {
        for (TaskType type :
                TaskType.values()) {
            if (value.equals(type.value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get a TaskType based on its string name
     *
     * @param name The string name of the TaskType
     * @return The TaskType that matches the name, or null if there exists no matching TaskType
     */
    public static TaskType fromName(String name) {
        for (TaskType type :
                TaskType.values()) {
            if (name.equals(type.name())) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get the counterpart TaskType
     *
     * @param requestedType The TaskType whose counterpart is to determine
     * @return The matching counterpart or null, if no counterpart exists
     */
    public static TaskType getMatchingTask(TaskType requestedType) {
        switch (requestedType) {
            case SELL_TICKET -> {
                return BUY_TICKET;
            }
            case BUY_TICKET -> {
                return SELL_TICKET;
            }
            case ASK_FOR_DIRECTION -> {
                return TELL_DIRECTION;
            }
            case TELL_DIRECTION -> {
                return ASK_FOR_DIRECTION;
            }
            case CLEANING -> {
                return CLEANING;
            }
            case WALKING -> {
                return WALKING;
            }
            case TRANSPORTING_LUGGAGE -> {
                return TRANSPORTING_LUGGAGE;
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Determine whether a TaskType is self-serving or not
     *
     * A self-serving TaskType is one, whose counterpart is the TaskType itself
     *
     * @return True if the TaskType is self-serving, false otherwise
     */
    public boolean isSelfServing() {
        return this == getMatchingTask(this);
    }
}

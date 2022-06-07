package Plugin.Task;

public enum TaskType {
    WALKING("Walk"),
    CLEANING("Clean"),
    TRANSPORTING_LUGGAGE("Transport luggage"),
    BUY_TICKET("Buy ticket"),
    SELL_TICKET("Sell ticket"),
    ASK_FOR_DIRECTION("Ask for direction"),
    TELL_DIRECTION("Tell direction"),
    REQUEST_BOARDING("Request boarding"),
    PERFORM_BOARDING("Perform boarding"),
    DROP_LUGGAGE("Drop luggage"),
    TAKE_LUGGAGE("Take luggage"),
    REQUEST_TICKET_CHECK("Request ticket check"),
    PERFORM_TICKET_CHECK("Perform ticket check"),
    REQUEST_SECURITY_CHECK("Request security check"),
    PERFORM_SECURITY_CHECK("Perform security check");

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
            case PERFORM_BOARDING -> {
                return REQUEST_BOARDING;
            }
            case REQUEST_BOARDING -> {
                return PERFORM_BOARDING;
            }
            case DROP_LUGGAGE -> {
                return TAKE_LUGGAGE;
            }
            case REQUEST_SECURITY_CHECK -> {
                return PERFORM_SECURITY_CHECK;
            }
            case PERFORM_SECURITY_CHECK -> {
                return REQUEST_SECURITY_CHECK;
            }
            case REQUEST_TICKET_CHECK -> {
                return PERFORM_TICKET_CHECK;
            }
            case PERFORM_TICKET_CHECK -> {
                return REQUEST_TICKET_CHECK;
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

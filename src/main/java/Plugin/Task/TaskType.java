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
    private TaskType(String value){
        this.value=value;
    }

    public static TaskType fromValue(String value) {
        for (TaskType type :
                TaskType.values()) {
            if (value.equals(type.value)) {
                return type;
            }
        }
        return null;
    }

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

    public boolean isSelfServing() {
        return this == getMatchingTask(this);
    }
}

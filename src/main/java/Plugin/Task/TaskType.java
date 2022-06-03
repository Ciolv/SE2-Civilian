package Plugin.Task;

public enum TaskType {
    WALKING("Walk"),
    CLEANING("Clean"),
    TRANSPORTING_LUGGAGE("Transport luggage"),
    BUY_TICKET("Buy Ticket"),
    SELL_TICKET("Sell Ticket"),
    ASK_FOR_DIRECTION("Ask for direction"),
    TELL_DIRECTION("Tell direction");

    public String value;
    private TaskType(String value){
        this.value=value;
    }
}

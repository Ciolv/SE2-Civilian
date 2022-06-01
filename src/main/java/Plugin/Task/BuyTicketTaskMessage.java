package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public class BuyTicketTaskMessage extends TaskMessage {

    static TaskType taskToComplete = TaskType.BUY_TICKET;
    static TaskType taskToPerform = TaskType.SELL_TICKET;

    public BuyTicketTaskMessage(Entity origin, Entity target) {
        super(origin, target);
    }
}

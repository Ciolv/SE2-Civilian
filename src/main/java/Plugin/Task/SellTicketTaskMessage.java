package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public class SellTicketTaskMessage extends TaskMessage {

    static TaskType taskToComplete = TaskType.SELL_TICKET;
    static TaskType taskToPerform = TaskType.BUY_TICKET;

    public SellTicketTaskMessage(Entity origin, Entity target) {
        super(origin, target);
    }
}

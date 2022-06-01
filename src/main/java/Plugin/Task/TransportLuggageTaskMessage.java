package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public class TransportLuggageTaskMessage extends TaskMessage {

    static TaskType taskToComplete = TaskType.TRANSPORTING_LUGGAGE;
    static TaskType taskToPerform = TaskType.TRANSPORTING_LUGGAGE;

    public TransportLuggageTaskMessage(Entity origin, Entity target) {
        super(origin, target);
    }
}

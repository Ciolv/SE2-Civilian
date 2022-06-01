package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public class TellDirectionTaskMessage extends TaskMessage {

    static TaskType taskToComplete = TaskType.ASK_FOR_DIRECTION;
    static TaskType taskToPerform = TaskType.TELL_DIRECTION;

    public TellDirectionTaskMessage(Entity origin, Entity target) {
        super(origin, target);
    }
}

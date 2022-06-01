package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public class AskForDirectionTaskMessage extends TaskMessage{

    static TaskType taskToComplete = TaskType.TELL_DIRECTION;
    static TaskType taskToPerform = TaskType.ASK_FOR_DIRECTION;

    public AskForDirectionTaskMessage(Entity origin, Entity target) {
        super(origin, target);
    }
}

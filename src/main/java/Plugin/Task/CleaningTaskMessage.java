package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

public class CleaningTaskMessage extends TaskMessage{

    static TaskType taskToComplete = TaskType.CLEANING;
    static TaskType taskToPerform = TaskType.CLEANING;

    public CleaningTaskMessage(Entity origin, Entity target) {
        super(origin, target);
    }
}

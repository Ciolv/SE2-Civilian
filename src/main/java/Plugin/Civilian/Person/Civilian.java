package Plugin.Civilian.Person;

import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import jdk.jshell.spi.ExecutionControl;

public class Civilian extends Person {

    protected TaskType[] taskTypes = new TaskType[] {
            TaskType.WALKING,
            TaskType.ASK_FOR_DIRECTION,
            TaskType.BUY_TICKET
    };
    boolean hasCabinLuggage;
    boolean hasLuggage;

    Entity findNextEntity(String entityID) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }
}

package Plugin.Civilian.Person;

import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

public class TerminalWorker extends Person{
    public TerminalWorker (String name, String[] tasks){
        super(name);
        taskTypes = new TaskType[] {
                TaskType.TELL_DIRECTION,
                TaskType.SELL_TICKET
        };
        addTaskList(tasks);
    }
}

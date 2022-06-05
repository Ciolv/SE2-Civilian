package Plugin.Civilian.Person;

import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

public class TerminalWorker extends Person{
    public TerminalWorker (Point position){
        super(position);
        taskTypes = new TaskType[] {
                TaskType.TELL_DIRECTION,
                TaskType.SELL_TICKET
        };

        int taskIndex = (int)Math.random()*taskTypes.length;
        tasks.add(taskTypes[taskIndex]);
    }
}

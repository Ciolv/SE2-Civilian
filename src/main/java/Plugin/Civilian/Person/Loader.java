package Plugin.Civilian.Person;

import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

public class Loader extends Person {
    public Loader(Point position) {
        super(position);

        taskTypes = new TaskType[] {
                TaskType.TRANSPORTING_LUGGAGE
        };

        generateTaskQueue(TaskType.TRANSPORTING_LUGGAGE, 20);
    }
}

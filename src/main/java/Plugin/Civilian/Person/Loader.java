package Plugin.Civilian.Person;

import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

public class Loader extends Person {
    public Loader(String name, String[] tasks) {
        super(name);

        taskTypes = new TaskType[] {
                TaskType.TRANSPORTING_LUGGAGE
        };
        addTaskList(tasks);
    }
}

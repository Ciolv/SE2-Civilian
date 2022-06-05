package Plugin.Civilian.Person;

import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class LuggageDistributor extends Person {
    public LuggageDistributor(Point position) {
        super(position);
        taskTypes = new TaskType[] {
                TaskType.TRANSPORTING_LUGGAGE
        };

        generateTaskQueue(TaskType.TRANSPORTING_LUGGAGE, 20);
    }
}

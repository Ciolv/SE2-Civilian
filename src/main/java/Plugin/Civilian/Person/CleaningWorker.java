package Plugin.Civilian.Person;

import Plugin.Task.Task;
import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleaningWorker extends Person {
    public CleaningWorker(Point position) {
        super(position);
        taskTypes = new TaskType[] {
                TaskType.CLEANING
        };

        tasks.addAll(generateTaskQueue(TaskType.CLEANING, 20));
    }
}

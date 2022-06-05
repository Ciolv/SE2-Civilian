package Plugin.Civilian.Person;

import Plugin.Task.Task;
import Plugin.Task.TaskType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleaningWorker extends Person {
    public CleaningWorker() {
        super();
        taskTypes = new TaskType[] {
                TaskType.CLEANING
        };

        tasks.addAll(generateTaskQueue(TaskType.CLEANING, 20));
    }
}

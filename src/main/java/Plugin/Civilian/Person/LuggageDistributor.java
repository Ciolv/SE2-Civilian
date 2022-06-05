package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

import java.util.ArrayList;
import java.util.List;

public class LuggageDistributor extends Person {
    public LuggageDistributor() {
        super();
        taskTypes = new TaskType[] {
                TaskType.TRANSPORTING_LUGGAGE
        };

        generateTaskQueue(TaskType.TRANSPORTING_LUGGAGE, 20);
    }
}

package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class Loader extends Person {
    public Loader() {
        super();

        taskTypes = new TaskType[] {
                TaskType.TRANSPORTING_LUGGAGE
        };

        generateTaskQueue(TaskType.TRANSPORTING_LUGGAGE, 20);
    }
}

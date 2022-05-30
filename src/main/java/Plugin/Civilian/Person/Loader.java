package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class Loader extends Person {
    private TaskType[] taskTypes = new TaskType[] {
            TaskType.WALKING,
            TaskType.TRANSPORTING_LUGGAGE
    };
}

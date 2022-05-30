package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class LuggageDistributor extends Person {
    private TaskType[] taskTypes = new TaskType[] {
            TaskType.WALKING,
            TaskType.TRANSPORTING_LUGGAGE
    };
}

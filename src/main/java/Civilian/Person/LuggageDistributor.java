package Civilian.Person;

import Task.TaskType;

public class LuggageDistributor extends Person {
    private TaskType[] taskTypes = new TaskType[] {
            TaskType.WALKING,
            TaskType.TRANSPORTING_LUGGAGE
    };
}

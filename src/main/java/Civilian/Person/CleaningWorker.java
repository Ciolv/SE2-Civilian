package Civilian.Person;

import Task.TaskType;

public class CleaningWorker extends Person {
    private TaskType[] taskTypes = new TaskType[] {
            TaskType.WALKING,
            TaskType.CLEANING
    };
}

package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class CleaningWorker extends Person {
    public CleaningWorker(String name, String[] tasks, String[] characteristics) {
        super(name, new TaskType[] {
                TaskType.CLEANING
        }, tasks, characteristics);
    }
}

package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class Loader extends Person {
    public Loader(String name, String[] tasks, String[] characteristics) {
        super(name, new TaskType[] {
                TaskType.TRANSPORTING_LUGGAGE
        }, tasks, characteristics);
    }
}

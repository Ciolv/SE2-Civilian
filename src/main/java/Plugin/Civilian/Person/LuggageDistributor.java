package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class LuggageDistributor extends Person {
    public LuggageDistributor(String name, String[] tasks, String[] characteristics) {
        super(name, new TaskType[] {
                TaskType.TRANSPORTING_LUGGAGE
        }, tasks, characteristics);
    }
}

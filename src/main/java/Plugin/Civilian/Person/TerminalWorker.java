package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class TerminalWorker extends Person{
    private TaskType[] taskTypes = new TaskType[] {
            TaskType.WALKING,
            TaskType.TELL_DIRECTION,
            TaskType.SELL_TICKET
    };
}

package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class TerminalWorker extends Person{
    public TerminalWorker (String name, String[] tasks, String[] characteristics){
        super(name, new TaskType[] {
                TaskType.TELL_DIRECTION,
                TaskType.SELL_TICKET,
                TaskType.PERFORM_BOARDING
        }, tasks, characteristics);
    }
}

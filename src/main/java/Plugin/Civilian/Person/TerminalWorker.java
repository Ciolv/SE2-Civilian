package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class TerminalWorker extends Person{
    public TerminalWorker (){
        super();
        taskTypes = new TaskType[] {
                TaskType.TELL_DIRECTION,
                TaskType.SELL_TICKET
        };

        int taskIndex = (int)Math.random()*taskTypes.length;
        tasks.add(taskTypes[taskIndex]);
    }
}

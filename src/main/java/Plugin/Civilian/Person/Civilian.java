package Plugin.Civilian.Person;

import Plugin.CivilianPlugin;
import Plugin.Task.Task;
import Plugin.Task.TaskMessage;
import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public class Civilian extends Person {

    boolean hasCabinLuggage;
    boolean hasLuggage;
    boolean hasTicket;
    String idNumber;

    public Civilian(String name, String[] tasks, String[] characteristics,
                    Boolean hasTicket, Boolean hasLuggage, String idNumber) {
        super(name, new TaskType[] {
                TaskType.ASK_FOR_DIRECTION,
                TaskType.BUY_TICKET,
                TaskType.REQUEST_BOARDING,
                TaskType.DROP_LUGGAGE
        }, tasks, characteristics);

        this.hasTicket = hasTicket;
        this.hasLuggage = hasLuggage;
        this.idNumber = idNumber;
        taskListCleaning();
//        throwLuggageDice();
    }

    public boolean hasLuggage(){
        return hasLuggage;
    }

    public boolean hasCabinLuggage(){
        return hasCabinLuggage;
    }

    /**
     * Decides randomly, whether the {@link Civilian#hasLuggage} property is true or false.
     *
     * Should only be used once, while instantiating the class.
     */
    void throwLuggageDice() {
        double rand = Math.random();
        int bool= (int) Math.round(rand);

        if(bool== 1){
            hasLuggage= true;
        }else {
            hasLuggage= false;
        }

        taskListCleaning();
    }

    /**
     * Send a {@link TaskMessage} to the given {@link Entity}
     *
     * @param target The {@link Entity} that shall be the message's target
     * @param message The message that shall be delivered.
     */
    @Override
    protected void sendTaskMessage(Task target, String message) {
        if(this.getPosition().getDistance(target.getPosition()) <= target.getRange()){
            // A civilian should always provide its id number, so that security checks can be performed
            // Furthermore, it should provide the information whether it has luggage, and a ticket or not
            TaskMessage pMessage =  new TaskMessage(this, target, message, String.format(
                    "idNumber:%s;hasLuggage:%b;hasTicket:%b", idNumber, hasLuggage, hasTicket)
            );

            getWorld().sendMessage(pMessage);
        }
    }

    /**
     * Dequeues the finished {@link Task} from the {@link Person#tasks} list
     *
     * @param taskType {@link TaskType} of {@link Task} that has been completed
     * @param executionTime Time in rounds that the execution needed
     */
    @Override
    protected void taskCompleted(TaskType taskType, int executionTime, int individualDuration) {
        // Only do anything, if there is a task left in the que and the top one matches the TaskType
        if (tasks.size() != 0 && taskType == TaskType.getMatchingTask(tasks.get(0))) {
            if (taskType.equals(TaskType.SELL_TICKET)) {
                this.hasTicket = true;
            } else if (taskType.equals(TaskType.TAKE_LUGGAGE)) {
                this.hasLuggage = false;
                taskListCleaning();
            }

            super.taskCompleted(taskType, executionTime, individualDuration);
        }
    }

    /**
     * Check if taskList contains any {@link TaskType}s that can not be performed and removes them if so
     */
    private void taskListCleaning() {
        cleanLuggageDrop();
        cleanTicketingIssur(TaskType.REQUEST_SECURITY_CHECK);
        cleanTicketingIssur(TaskType.REQUEST_BOARDING);
    }
    
    /**
     * Ensures, that the civilian first buys a ticket and performs the given {@link TaskType} afterwards
     * @param toPerformAfter The task to perform only after a ticket was bought
     */
    private void cleanTicketingIssur(TaskType toPerformAfter) {
        // Only civilians with a ticket can perform a security check
        boolean hasTaskToPerformFirst = tasks.stream().anyMatch(t -> t.equals(toPerformAfter));
        if (hasTaskToPerformFirst && !hasTicket) {
            int taskToPerformFirstIndex = tasks.indexOf(toPerformAfter);
            boolean hasBuyTicket = tasks.stream().anyMatch(t -> t.equals(TaskType.BUY_TICKET));

            // If the person will
            if (!hasBuyTicket) {
                // Let the civilian perform the next task, if it initially would request a security check
                if (taskToPerformFirstIndex == 0) {
                    isEnqueued = false;

                    // insert BUY_TICKET at position 0
                    TaskType[] newTaskTypes = new TaskType[tasks.size() + 1];
                    newTaskTypes[0] = TaskType.BUY_TICKET;
                    arraycopy(tasks, 0, newTaskTypes, 1, tasks.size());
                    tasks = Arrays.stream(newTaskTypes).toList();
                } else {
                    // insert BUY_TICKET right before REQUEST_SECURITY_CHECK
                    TaskType[] newTaskTypes = new TaskType[tasks.size() + 1];
                    arraycopy(tasks, 0, newTaskTypes, 0, taskToPerformFirstIndex);
                    newTaskTypes[taskToPerformFirstIndex] = TaskType.BUY_TICKET;
                    arraycopy(tasks, taskToPerformFirstIndex, newTaskTypes, taskToPerformFirstIndex + 1, tasks.size() - taskToPerformFirstIndex);
                    tasks = Arrays.stream(newTaskTypes).toList();
                }
            } else {
                int buyTicketIndex = tasks.indexOf(TaskType.BUY_TICKET);
                if (buyTicketIndex > taskToPerformFirstIndex) {
                    // Swap task order
                    tasks.set(taskToPerformFirstIndex, TaskType.BUY_TICKET);
                    tasks.set(buyTicketIndex, toPerformAfter);

                    // Let the person perform the next task
                    if (taskToPerformFirstIndex == 0) {
                        isEnqueued = false;
                    }
                }
            }
        }
    }

//    /**
//     * Ensures, that the civilian first buys a ticket and performs the boarding afterwards
//     */
//    private void cleanBoarding() {
//        // Only civilians with a ticket can perform a security check
//        boolean hasBoardingTask = tasks.stream().anyMatch(t -> t.equals(TaskType.REQUEST_BOARDING));
//        if (hasBoardingTask && !hasTicket) {
//            int boardingTask = tasks.indexOf(TaskType.REQUEST_BOARDING);
//            boolean hasBuyTicket = tasks.stream().anyMatch(t -> t.equals(TaskType.BUY_TICKET));
//
//            // If the person will
//            if (!hasBuyTicket) {
//                // Let the civilian perform the next task, if it initially would request a security check
//                if (boardingTask == 0) {
//                    isEnqueued = false;
//
//                    // insert BUY_TICKET at position 0
//                    TaskType[] newTaskTypes = new TaskType[tasks.size() + 1];
//                    newTaskTypes[0] = TaskType.BUY_TICKET;
//                    arraycopy(tasks, 0, newTaskTypes, 1, tasks.size());
//                    tasks = Arrays.stream(newTaskTypes).toList();
//                } else {
//                    // insert BUY_TICKET right before REQUEST_SECURITY_CHECK
//                    TaskType[] newTaskTypes = new TaskType[tasks.size() + 1];
//                    arraycopy(tasks, 0, newTaskTypes, 0, boardingTask);
//                    newTaskTypes[boardingTask] = TaskType.BUY_TICKET;
//                    arraycopy(tasks, boardingTask, newTaskTypes, boardingTask + 1, tasks.size() - boardingTask);
//                    tasks = Arrays.stream(newTaskTypes).toList();
//                }
//            } else {
//                int buyTicketIndex = tasks.indexOf(TaskType.BUY_TICKET);
//                if (buyTicketIndex > boardingTask) {
//                    // Swap task order
//                    tasks.set(boardingTask, TaskType.BUY_TICKET);
//                    tasks.set(buyTicketIndex, TaskType.REQUEST_BOARDING);
//
//                    // Let the person perform the next task
//                    if (boardingTask == 0) {
//                        isEnqueued = false;
//                    }
//                }
//            }
//        }
//    }

    /**
     * Ensures, that the {@link Civilian} never tries to drop luggage, when it does not carra any
     */
    private void cleanLuggageDrop() {
        // Remove DROP_LUGGAGE task, if the civilian does not carry luggage
        boolean hasLuggageDropTask = tasks.stream().anyMatch(t -> t.equals(TaskType.DROP_LUGGAGE));
        if (hasLuggageDropTask && !hasLuggage()) {
            if (tasks.size() > 0) {
                int taskIndex = tasks.indexOf(TaskType.DROP_LUGGAGE);

                // Let the civilian perform the next task, if it initially would drop non-existent luggage
                if (taskIndex == 0) {
                    isEnqueued = false;
                }

                // Remove DROP_LUGGAGE task
                tasks = tasks.stream().filter(t -> t.equals(TaskType.DROP_LUGGAGE)).toList();
            }
        }
    }
}

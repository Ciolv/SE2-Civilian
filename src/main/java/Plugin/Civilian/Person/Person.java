package Plugin.Civilian.Person;

import Plugin.CivilianPlugin;
import Plugin.Task.CompletionTaskMessage;
import Plugin.Task.Task;
import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Agent;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;
import jdk.jshell.spi.ExecutionControl;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Person extends Agent {

    protected int id;
    protected Point position;
    protected Characteristic[] characteristics;
    protected List<Task> tasks;
    protected TaskType[] taskTypes;
    protected Boolean isEnqueued;

    void calculateMovement()throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    /**
     * If a matching task is present and near, the method executes it.
     */
    @Override
    public void pluginUpdate() {
        if (isEnqueued) {
            return;
        }

        if (tasks.size() > 0) {
            // Select near tasks that match to first assigned task
            Stream<Task> compatibleTasks = null;
            try {
                compatibleTasks = Arrays.stream(findNearTasks()).filter(
                        t -> t.getTaskType() == tasks.get(0).getTaskType()
                );
            } catch (ExecutionControl.NotImplementedException e) {
                throw new RuntimeException(e);
            }
            // Finds the closest entity where task can be executed
            try {
                Task closestEntity = (Task)findClosestEntity(compatibleTasks.toArray((Entity[]::new)));
            } catch (ExecutionControl.NotImplementedException e) {
                throw new RuntimeException(e);
            }
//            closestEntity.enqueue(this);
            //TODO: Message verwenden
            isEnqueued = true;
        } else  {
            remove();
        }
    }

    void measureTaskTimer() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void remove() {
        kill();
    }

    void handleMessage(Message message) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void sendMessage() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    public TaskType[] getTaskTypes() {
        return taskTypes;
    }

    Task[] findNearTasks() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    Entity findClosestEntity(Entity[] entities) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    /**
     * Dequeues the finished task from the tasks list.
     *
     * @param taskType Type of task that has been completed.
     * @param executionTime Time in rounds that the execution needed.
     */
    public void taskCompleted(TaskType taskType, int executionTime) {
        // The task should only be removed, if it matches the task type at the top of the task queue.
        if (tasks.size() != 0
                && taskType == tasks.get(0).getTaskType()) {
            tasks.remove(0);
        }

        CivilianPlugin.logger.info(String.format("%s finished the task %s in %s rounds.",
                this.getClass(), taskType.name(), executionTime));
    }

    @Override
    public void receiveMessage(Message m) {
        if (m instanceof CompletionTaskMessage) {
            TaskType completedTask = ((CompletionTaskMessage) m).getCompletedTask();
            if (completedTask == tasks.get(0).getTaskType()) {
                tasks.remove(0);

                CivilianPlugin.logger.info(String.format("%s finished the task %s in %s rounds, default rounds would be %s.",
                        this.getClass(), completedTask.name(), ((CompletionTaskMessage)m).getExecutionTime(),
                        ((CompletionTaskMessage)m).getIndividualDuration()));
            }
        }
    }
    
    /**
     * Calculate the speed factor for a person.
     * @return Arithmetic mean of all characteristics.
     */
    public double getSpeedFactor() {
        double speed = 0;

        for (Characteristic characteristic:
        characteristics){
            speed += characteristic.value;
        }

        speed = speed / characteristics.length;

        return speed;
    }


}

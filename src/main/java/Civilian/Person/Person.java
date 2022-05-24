package Civilian.Person;

import Task.Task;
import Task.TaskType;
import aas.model.communication.Message;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Agent;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import jdk.jshell.spi.ExecutionControl;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
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
     *
     * @throws ExecutionControl.NotImplementedException
     */
    void runTask() throws ExecutionControl.NotImplementedException {
        if (isEnqueued) {
            return;
        }

        if (tasks.size() > 0) {
            // Select near tasks that match to first assigned task
            Stream<Task> compatibleTasks = Arrays.stream(findNearTasks()).filter(
                    t -> t.getTaskType() == tasks.get(0).getTaskType()
            );
            // Finds the closest entity where task can be executed
            Task closestEntity = (Task)findClosestEntity(compatibleTasks.toArray((Entity[]::new)));
            closestEntity.addConsumer(this);
            isEnqueued = true;
        } else  {
            remove();
        }
    }

    void measureTaskTimer() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void remove() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
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
     * @param taskType Type of task that has been completed.
     */
    public void taskCompleted(TaskType taskType) {
        // The task should only be removed, if it matches the task type at the top of the task queue.
        if (tasks.size() != 0
                && taskType == tasks.get(0).getTaskType()) {
            tasks.remove(0);
        }
    }

    @Override
    public void pluginUpdate() {
        // TODO: implement...
    }
}

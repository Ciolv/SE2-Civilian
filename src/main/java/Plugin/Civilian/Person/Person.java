package Plugin.Civilian.Person;

import Plugin.CivilianPlugin;
import Plugin.Task.Task;
import Plugin.Task.TaskType;
import aas.model.communication.Message;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Agent;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import jdk.jshell.spi.ExecutionControl;

import javax.xml.stream.events.Characters;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class Person extends Agent {

    protected int id;
    protected Point position;
    protected Characteristic[] characteristics;
    protected double speed;
    protected List<Task> tasks;
    protected TaskType[] taskTypes;
    protected Boolean isEnqueued;


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
            closestEntity.enqueue(this);
            isEnqueued = true;
        } else  {
            remove();
        }
    }


    void calculateMovement(){
        double rand = Math.random()*(0-5);
        int pRand= (int) Math.round(rand);
        double pSpeed= characteristics[pRand].getValue();
        speed = pSpeed;
    }

    double getSpeed() {
        return speed;
    }


    Entity findClosestEntity(Entity[] entities) throws ExecutionControl.NotImplementedException {
        boolean entitiesThere;
        int x = getX();
        int y = getY();
        string Name ;
        double entfernung;
        double entfRechnung;
        Entity erg;
        ArrayList<String> foundEntities= new ArrayList(getEnteties(x, y, 50));
        int p;
        p = foundEntities.size();
        if (foundEntities != null){
            for (int i=0; i<10;i++){
                //int[] [] pos = new int[] [];
                int[] pos = new int[];
                pos[i] = Arraylist.get(i).getPosition;
                int xE = pos[i] [0];
                int yE = pos[i] [1];
                //int insgX = xE - x;
                //int insgY = yE - y;
                entfRechnung=sqrt(Math.pow(x-xE, 2)+Math.pow(y-yE, 2));
                if(entfRechnung < entfernung) {
                    entfernung = entfRechnung;
                    erg = foundEntities[i].name;
                }
            }
        }
        else{
            entitiesThere = False;
            erg = null;
        }
        return erg;
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
    public void pluginUpdate() {
        // TODO: implement...
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

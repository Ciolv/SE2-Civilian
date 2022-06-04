package Plugin.Civilian.Person;

import Plugin.CivilianPlugin;
import Plugin.Task.CompletionTaskMessage;
import Plugin.Task.Task;
import Plugin.Task.TaskMessage;
import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Agent;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import jdk.jshell.spi.ExecutionControl;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Math.sqrt;

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


    void calculateMovement(){
        double rand = Math.random()*(0-5);
        int pRand= (int) Math.round(rand);
        double pSpeed= characteristics[pRand].getValue();
        speed = pSpeed;
    }

    public double getSpeedy() {
        return speed;
    }

    Entity findClosestEntityForTask(Entity[] entities, TaskType type ){
        double x = position.getX();
        int y = (int) position.getY();
        int maxDist = 0;
        int height = getWorld().getHeight();
        int width = getWorld().getWidth();
        if (width<height){
            maxDist= height;
        }
        else{
            maxDist=width;
        }
        double entfernung = maxDist;
        double entfRechnung;
        Entity erg = null;
        ArrayList<Entity> foundEntities= new ArrayList(getWorld().getEntities(x, y, maxDist));
        int p;
        p = foundEntities.size();
        if (p != 0){
            for (int i=0; i<p;i++){
                ArrayList<Point> pos = new ArrayList();
                Entity pPerson = foundEntities.get(i);
                Point pos1= pPerson.getPosition();
                int xE = pos1.getX();
                int yE = pos1.getY();

                entfRechnung=sqrt(Math.pow(x-xE, 2)+Math.pow(y-yE, 2));
                if(entfRechnung < entfernung) {
                    entfernung = entfRechnung;
                    erg = foundEntities.get(i);
                }
            }
        }
        else{
            erg = null;
        }
        return erg;
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
    public void startToPerformTask(Entity[] ent ) {
        Task p = tasks.get(0);
        if (p.getTaskType() == TaskType.CLEANING){
            Task.performSelfServingTask("Clean");

        } else if (p.getTaskType() == TaskType.ASK_FOR_DIRECTION) {
            Entity z = findClosestEntityForTask(ent, TaskType.TELL_DIRECTION);
            z.turn(z.position);
            if(this.position == z.position){
               TaskMessage pMessage =  new TaskMessage(this, z);
                Task.performNonSelfServingTask(pMessage);
            }
        }
        else if (p.getTaskType() == TaskType.BUY_TICKET){
            Entity z = findClosestEntityForTask(ent, TaskType.SELL_TICKET);
            z.turn(z.position);
            if(this.position == z.position){
                TaskMessage pMessage =  new TaskMessage(this, z);
                Task.performNonSelfServingTask(pMessage);
            }
        }
        else if (p.getTaskType() == TaskType.SELL_TICKET){
            Entity z = findClosestEntityForTask(ent, TaskType.BUY_TICKET);
            z.turn(z.position);
            if(this.position == z.position){
                TaskMessage pMessage =  new TaskMessage(this, z);
                Task.performNonSelfServingTask(pMessage);
            }
        }
        else if (p.getTaskType() == TaskType.WALKING){
            Entity z = findClosestEntityForTask(ent, TaskType.TELL_DIRECTION);
            z.turn(z.position);
            if(this.position == z.position){
                TaskMessage pMessage =  new TaskMessage(this, z);
                Task.performNonSelfServingTask(pMessage);
            }
        }
        else if (p.getTaskType() == TaskType.TELL_DIRECTION){
            Entity z = findClosestEntityForTask(ent, TaskType.WALKING);
            z.turn(z.position);
            if(this.position == z.position){
                TaskMessage pMessage =  new TaskMessage(this, z);
                Task.performNonSelfServingTask(pMessage);
            }
        }
        else if (p.getTaskType() == TaskType.TRANSPORTING_LUGGAGE){
            if (Person.Civil.getHasLuggage() ) {
                //wenn this.position = Gepäckabgbestation ist die Aufgabe erledigt
            }
        }
        else{
            //p.getTaskType() == TaskType.WALKING
        }
    }


    Person findClosestEntity(Entity[] entities) {
        double x = position.getX();
        int y = (int) position.getY();
        int maxDist = 0;
        int height = getWorld().getHeight();
        int width = getWorld().getWidth();
        if (width<height){
            maxDist= height;
        }
        else{
            maxDist=width;
        }
        double entfernung = maxDist;
        double entfRechnung;
        Person erg = null;
        ArrayList<Person> foundEntities= new ArrayList(getWorld().getEntities(x, y, maxDist));
        int p;
        p = foundEntities.size();
        if (p != 0){
            for (int i=0; i<p;i++){
                ArrayList<Point> pos = new ArrayList();
                Person pPerson = foundEntities.get(i);
                Task taskFinds = pPerson.tasks.get(0);
                TaskType s = taskFinds.getTaskType();
                if (s == type ) {
                    Point pos1 = pPerson.getPosition();
                    int xE = pos1.getX();
                    int yE = pos1.getY();
                    entfRechnung = sqrt(Math.pow(x - xE, 2) + Math.pow(y - yE, 2));
                    if (entfRechnung < entfernung) {
                        entfernung = entfRechnung;
                        erg = foundEntities.get(i);
                    }
                }
            }
        }
        else{
            erg = null;
        }

        return erg;
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

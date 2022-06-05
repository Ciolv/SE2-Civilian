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

import java.util.*;

import static java.lang.Math.sqrt;

public abstract class Person extends Agent {
    protected Point position;
    protected Characteristic[] characteristics;
    protected double individualSpeed = 1;
    protected List<TaskType> tasks = new ArrayList<>();
    protected TaskType[] taskTypes;
    protected Boolean isEnqueued;


    public Person() {
        buildCharacter();
        calculateMovementSpeed();
    }

    protected List<TaskType> generateTaskQueue(TaskType taskType, int amount) {
        int taskCount = (int)Math.random()*amount;
        List<TaskType> taskQueue = new ArrayList<>(taskCount);

        for (int i = 0; i < taskCount; i++) {
            taskQueue.add(taskType);
        }
        return taskQueue;
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
            startToPerformTask();
            isEnqueued = true;
        } else  {
            remove();
        }
    }

    void calculateMovementSpeed(){
        individualSpeed = 1 * getSpeedFactor();
        setSpeed(individualSpeed);
    }

    void buildCharacter() {
        int characteristicCount = (int) Math.random()*Characteristic.values().length;
        characteristics = new Characteristic[characteristicCount];
        List<Characteristic> chars = List.of(Characteristic.values());

        for (int i = 0; i < characteristicCount; i++) {
            int remainingCharacteristic = (int) Math.random() * chars.size();
            characteristics[i] = chars.get(remainingCharacteristic);
            chars.remove(remainingCharacteristic);
        }
    }

    /*
    In einer Liste mit Entities, sollen die Entities gefunden werden, die den angegebenen TaskType bearbeiten können.
    Es muss sich bei diesen Entities um solche vom Typ Task handeln.
    Aus allen diesen gefilterten Entities, soll diejenige gefunden werden, die der Person am nähesten ist.
     */
    Task findClosestEntityForTask(TaskType type) {
        Collection<Entity> entities = getWorld().getEntities();

        List<Entity> applicableEntities = entities.stream().filter(
                entity -> entity instanceof Task && ((Task)entity).taskIsApplicable(type)
        ).toList();

        double smallestDistance = Double.MAX_VALUE;
        Task closestTask = null;

        for (Entity entity:
             applicableEntities) {
            Point taskPosition = entity.getPosition();
            double distance = sqrt(Math.pow(taskPosition.getX()-position.getX(), 2) +
                    Math.pow(taskPosition.getX()-position.getY(), 2));

            if (smallestDistance > distance) {
                smallestDistance = distance;
                closestTask = ((Task)entity);
            }
        }

        return closestTask;
    }

    void remove() {
        kill();
    }

    private void sendTaskMessage(Entity closestEntity, String message) {
        this.turn(closestEntity.getPosition());
        if(this.position == closestEntity.getPosition()){
            TaskMessage pMessage =  new TaskMessage(this, closestEntity, message);
            getWorld().sendMessage(pMessage);
        }
    }

    public void startToPerformTask() {
        TaskType task = tasks.get(0);
        Entity closestEntity = findClosestEntityForTask(task);

        if (closestEntity != null) {
            setSpeed(0);
            sendTaskMessage(closestEntity, task.value);
        }
    }

    /**
     * Dequeues the finished task from the tasks list.
     *
     * @param taskType Type of task that has been completed.
     * @param executionTime Time in rounds that the execution needed.
     */
    public void taskCompleted(TaskType taskType, int executionTime, double individualDuration) {
        // The task should only be removed, if it matches the task type at the top of the task queue.
        if (tasks.size() != 0 && taskType == tasks.get(0)) {
            if (this instanceof Civilian) {
                tasks.remove(0);
                isEnqueued = false;
                setSpeed(individualSpeed);
            }

            CivilianPlugin.logger.info(String.format("%s finished the task %s in %d rounds, default rounds would be %f.",
                    this.getClass(), taskType.name(), executionTime, individualDuration));
        }
    }

    @Override
    public void receiveMessage(Message m) {
        if (m instanceof CompletionTaskMessage) {
            taskCompleted(((CompletionTaskMessage) m).getCompletedTask(),
                    ((CompletionTaskMessage)m).getExecutionTime(),
                    ((CompletionTaskMessage)m).getIndividualDuration());
        }
    }
    
    /**
     * Calculate the speed factor for a person.
     * @return Arithmetic mean of all characteristics.
     */
    public double getSpeedFactor() {
        double speed = 0;

        if (characteristics.length > 0) {
            for (Characteristic characteristic:
                    characteristics){
                speed += characteristic.value;
            }

            speed = speed / characteristics.length;
        }

        return speed;
    }
}

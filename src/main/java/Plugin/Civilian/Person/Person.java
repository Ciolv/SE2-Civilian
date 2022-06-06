package Plugin.Civilian.Person;

import Plugin.CivilianPlugin;
import Plugin.Task.CompletionTaskMessage;
import Plugin.Task.Task;
import Plugin.Task.TaskMessage;
import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Agent;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;

import java.util.*;

public abstract class Person extends Agent {
    private String name;
    protected Characteristic[] characteristics;
    protected double individualSpeed = 1;
    protected List<TaskType> tasks = new ArrayList<>();
    protected TaskType[] taskTypes;
    protected Boolean isEnqueued = false;
    protected Boolean isWalking = false;
    private Boolean removed = false;
    private ArrayList<Message> knownMessages = new ArrayList<>();


    public Person(String name) {
        this.name = name;
        buildCharacter();
        calculateMovementSpeed();

        CivilianPlugin.logger.info(String.format(
                "%s (%s, uid:'%d') entered the airport.",
                name,
                this.getClass().getSimpleName(),
                getUID()
        ));
    }

    public String getName() {
        return name;
    }

    protected void addTaskList(String[] tasks) {

            for (String task :
                    tasks) {
                TaskType t = TaskType.fromValue(task);
                if (t != null) {

                    if (taskIsApplicable(t)) {
                        this.tasks.add(t);
                    } else {
                        CivilianPlugin.logger.error(String.format(
                                "Invalid configuration for %s '%s'! %s can not perform '%s'!",
                                this.getClass().getSimpleName(),
                                name,
                                this.getClass().getSimpleName(),
                                task
                        ));
                    }
                } else {
                    CivilianPlugin.logger.error(String.format(
                            "Invalid configuration for %s '%s'! '%s' is not a valid TaskType!",
                            this.getClass().getSimpleName(),
                            name,
                            task
                    ));
                }
            }
    }

    /**
     * Validate whether a {@link TaskType} list contains any task type applicable for this task or not.
     * @param taskType {@link TaskType} TasktType to validate
     * @return True if the {@link TaskType} matches with the applicable TaskTypes, False otherwise.
     */
    public boolean taskIsApplicable(TaskType taskType) {
        if (!removed) {
            return Arrays.stream(this.taskTypes).anyMatch(t -> t == taskType);
        } else {
            return false;
        }
    }

    protected List<TaskType> generateTaskQueue(TaskType taskType, int amount) {
            int taskCount = (int) Math.random() * amount;
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
        if (!removed) {
            if (isEnqueued) {
                return;
            }

            if (tasks.size() > 0) {
                startToPerformTask();
            } else {
                remove();
            }
        }
    }

    void calculateMovementSpeed(){
        individualSpeed = 1 * getSpeedFactor();
        setSpeed(individualSpeed);
    }

    void buildCharacter() {
        int characteristicCount = (int)(Math.random()*Characteristic.values().length);

        if (characteristicCount == 0) {
            characteristicCount = 1;
        }

        characteristics = new Characteristic[characteristicCount];
        List<Characteristic> chars = new ArrayList<>();
        for (Characteristic c:
             Characteristic.values()) {
            chars.add(c);
        }


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
            double distance = this.getPosition().getDistance(entity.getPosition());

            if (smallestDistance > distance) {
                smallestDistance = distance;
                closestTask = ((Task)entity);
            }
        }

        return closestTask;
    }

    void remove() {
        removed = true;
    }

    private void sendTaskMessage(Entity closestEntity, String message) {
        if(this.getPosition().equals(closestEntity.getPosition())){
            TaskMessage pMessage =  new TaskMessage(this, closestEntity, message);
            getWorld().sendMessage(pMessage);
        }
    }

    private void startToPerformTask() {
        TaskType task = tasks.get(0);
        Entity closestEntity = findClosestEntityForTask(task);
        double distance = this.getPosition().getDistance(closestEntity.getPosition());

        if (closestEntity != null) {

            if (distance > closestEntity.getHeight() &&
            distance > closestEntity.getWidth()) {
                this.turn(closestEntity.getPosition());
                isWalking = true;
                return;
            }

            isWalking = false;
            setSpeed(0);
            sendTaskMessage(closestEntity, task.value);
            isEnqueued = true;

            if (!task.isSelfServing()) {
                CivilianPlugin.logger.info(String.format(
                        "%s is waiting to '%s' at (%d|%d)",
                        name,
                        task.value,
                        getPosition().getX(),
                        getPosition().getY()
                ));
            }
            else {
                CivilianPlugin.logger.debug(String.format(
                        "%s is waiting to '%s' at (%d|%d)",
                        name,
                        task.value,
                        getPosition().getX(),
                        getPosition().getY()
                ));
            }
        }
    }

    /**
     * Dequeues the finished task from the tasks list.
     *
     * @param taskType Type of task that has been completed.
     * @param executionTime Time in rounds that the execution needed.
     */
    private void taskCompleted(TaskType taskType, int executionTime, int individualDuration) {
        if (tasks.size() != 0 && taskType == TaskType.getMatchingTask(tasks.get(0))) {
            if (!taskType.isSelfServing()) {
                CivilianPlugin.logger.info(
                        String.format("%s has finished the task '%s' in %d rounds. Expected %d rounds.",
                                name, tasks.get(0).value, executionTime, individualDuration));
            } else {
                CivilianPlugin.logger.debug(
                        String.format("%s has finished the task '%s' in %d rounds. Expected %d rounds.",
                                name, tasks.get(0).value, executionTime, individualDuration));
            }

            isEnqueued = false;

            // The task should only be removed, if it matches the task type at the top of the task queue.
            if (this instanceof Civilian) {
                tasks.remove(0);
                setSpeed(individualSpeed);
            }
        }
    }

    @Override
    public void receiveMessage(Message m) {
        if (!removed && !knownMessages.contains(m)) {
            if (m instanceof CompletionTaskMessage) {
                taskCompleted(((CompletionTaskMessage) m).getCompletedTask(),
                        ((CompletionTaskMessage) m).getExecutionTime(),
                        (int)Math.ceil(((CompletionTaskMessage) m).getIndividualDuration())
                );
            }
            knownMessages.add(m);
        }
    }
    
    /**
     * Calculate the speed factor for a person.
     * @return Arithmetic mean of all characteristics.
     */
    public double getSpeedFactor() {

        double speed = 0;
        if (!removed) {
            if (characteristics.length > 0) {
                for (Characteristic characteristic :
                        characteristics) {
                    speed += characteristic.value;
                }

                speed = speed / characteristics.length;
            }
        }

        return speed;
    }
}

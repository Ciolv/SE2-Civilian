package Plugin.Civilian.Person;

import Plugin.CivilianPlugin;
import Plugin.Task.CompletionTaskMessage;
import Plugin.Task.Task;
import Plugin.Task.TaskMessage;
import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Agent;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.DirectedMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Person extends Agent {
    private String name;
    private Characteristic[] characteristics;
    private double individualSpeed = 1;
    private List<TaskType> tasks = new ArrayList<>();
    protected TaskType[] taskTypes;
    private boolean isEnqueued = false;
    private boolean removed = false;
    private ArrayList<Message> knownMessages = new ArrayList<>();

    public Person(String name, TaskType[] taskTypes, String[] taskList, String[] characteristics) {
        this.name = name;
        this.taskTypes = taskTypes;

        // Create random characteristics if none are given.
        if (characteristics.length == 0) {
            buildCharacter();
        } else {
            buildCharacter(characteristics);
        }

        calculateMovementSpeed();
        addTaskList(taskList);

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

    @Override
    public void receiveMessage(Message m) {
        // It seems as if the simulation delivers a single message multiple times
        // So we need to ensure, that we do not process the same message over and over again
        // Otherwise, tasks could be finished without ever being performed
        // Furthermore, there is no need to process messages, if the Person has no tasks left
        if (!removed && !knownMessages.contains(m)) {
            boolean isDirectedMessage =
                    Arrays.stream(m.getClass().getInterfaces()).anyMatch(i -> i == DirectedMessage.class);

            // Only messages that are directed to this Person should be processed and only of the TaskType matches
            // If so, enqueue
            if (isDirectedMessage) {
                boolean isDirectedToThisTask = ((DirectedMessage) m).getTarget().equals(this);

                // Even directed messages are delivered to every entity in range
                // We do not need to handle messages, that are not directed to the instance a Person
                // The check happens only here, because we do not know, whether targets of messages could change later on
                if (isDirectedToThisTask) {

                    // Checking the class type of the message happens at last, to ensure expandability.
                    if (m instanceof CompletionTaskMessage) {
                        taskCompleted(((CompletionTaskMessage) m).getCompletedTask(),
                                ((CompletionTaskMessage) m).getExecutionTime(),
                                (int) Math.round(((CompletionTaskMessage) m).getIndividualDuration())
                        );
                    }

                    knownMessages.add(m);
                }
            }
        }
    }

    /**
     * Set {@link Person#tasks} based on a {@link String} array of tasks
     * @param tasks Array of {@link TaskType} values
     */
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
     * Validate whether {@link Person#taskTypes} contains the given {@link TaskType}
     *
     * @param taskType {@link TaskType} TaskType to validate
     * @return True if the {@link TaskType} matches with the applicable {@link TaskType}s, false otherwise.
     */
    public boolean taskIsApplicable(TaskType taskType) {
        if (!removed) {
            return Arrays.stream(this.taskTypes).anyMatch(t -> t == taskType);
        } else {
            return false;
        }
    }

    /**
     * Calculates the movement speed for the {@link Person} and sets it as {@link Person#individualSpeed}
     */
    void calculateMovementSpeed(){
        individualSpeed = 1 * getSpeedFactor();
        setSpeed(individualSpeed);
    }

    /**
     * Set {@link Person#characteristics} based on a {@link String} array of characteristic names
     * @param characteristics Array of {@link Characteristic} names
     */
    void buildCharacter(String[] characteristics) {
        this.characteristics = new Characteristic[characteristics.length];
        for (int i = 0; i < characteristics.length; i++) {
            this.characteristics[i] = Characteristic.fromName(characteristics[i]);
        }
    }

    /**
     * Generate a random list of {@link Characteristic}s and set this list to {@link Person#characteristics}
     */
    void buildCharacter() {
        // Select the amount of characteristics to create
        // Use a minimum of 1 and a maximum of all characteristics
        int characteristicCount = (int)(Math.random() * Characteristic.values().length);
        if (characteristicCount == 0) {
            characteristicCount = 1;
        }

        // Set the characteristics array size to the amount generated
        characteristics = new Characteristic[characteristicCount];

        // Create a list containing all existing characteristics
        List<Characteristic> allCharacteristics = new ArrayList<>();
        for (Characteristic c:
             Characteristic.values()) {
            allCharacteristics.add(c);
        }

        // Select random characteristics without duplicates
        for (int i = 0; i < characteristicCount; i++) {
            int remainingCharacteristic = (int) (Math.random() * allCharacteristics.size());
            characteristics[i] = allCharacteristics.get(remainingCharacteristic);

            allCharacteristics.remove(remainingCharacteristic);
        }
    }

    /**
     * Get the closest {@link Task} in the world that matches the {@link TaskType}
     *
     * @param type The {@link TaskType} the {@link Task} should hold
     * @return The closest {@link Task} with matching {@link TaskType}, null if there is no matching {@link Task}
     */
    Task getClosestTaskForType(TaskType type) {
        Collection<Entity> entities = getWorld().getEntities();

        // Get all tasks in the world that provide the given TaskType
        List<Task> applicableEntities = entities.stream().filter(
                entity -> entity instanceof Task && ((Task)entity).taskIsApplicable(type)
        ).map(e -> (Task)e).toList();

        double smallestDistance = Double.MAX_VALUE;
        Task closestTask = null;

        // Find the Task that is closest
        for (Task task:
             applicableEntities) {
            double distance = this.getPosition().getDistance(task.getPosition()) - task.getRange();

            if (smallestDistance > distance) {
                smallestDistance = distance;
                closestTask = task;
            }
        }

        return closestTask;
    }

    /**
     * Removes the {@link Person} (disables functionality).
     */
    void remove() {
        removed = true;
        // Kill can not be used, as Messages related to the Person can exist after the removal
        // Killing the Person would result in Exceptions
    }

    /**
     * Send a {@link TaskMessage} to the given {@link Entity}
     *
     * @param target The {@link Entity} that shall be the message's target
     * @param message The message that shall be delivered.
     */
    private void sendTaskMessage(Task target, String message) {
        if(this.getPosition().getDistance(target.getPosition()) <= target.getRange()){
            TaskMessage pMessage =  new TaskMessage(this, target, message);
            getWorld().sendMessage(pMessage);
        }
    }

    /**
     * Walks to a {@link Task} where the next {@link TaskType} in {@link Person#tasks} can be performed and enqueues for it.
     */
    private void startToPerformTask() {
        TaskType task = tasks.get(0);
        Task entity = getClosestTaskForType(task);

        // Only perform actions, if a matching task exists and the Person is located at this task
        if (entity != null) {
            if (!walking(entity)) {
                // The Person should not be able to walk while it is performing a task
                setSpeed(0);

                // Enqueue for the task
                sendTaskMessage(entity, task.value);
                isEnqueued = true;

                // Logging
                if (!task.isSelfServing()) {
                    CivilianPlugin.logger.info(String.format(
                            "%s is waiting to '%s' at (%d|%d)",
                            name,
                            task.value,
                            getPosition().getX(),
                            getPosition().getY()
                    ));
                } else {
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
    }

    /**
     * Walk to the given target
     *
     * @param target The target of this instance
     * @return True if the instance is at the same position as the entity, false otherwise
     */
    private boolean walking(Task target) {
        double distance = this.getPosition().getDistance(target.getPosition()) - target.getRange() + 1;

        // We can not walk into solid objects, but into non-solid objects
        if (!target.isSolid()) {
            // Walk until the target is in range
            if (distance > 0) {
                turn(target.getPosition());
                return true;
            }
        } else {
            // Walk until the target is in range, but stop walking, if we can not get any closer to the target
            if (distance > target.getHeight() &&
                    distance > target.getWidth()) {
                turn(target.getPosition());
                return true;
            }
        }

        return false;
    }

    /**
     * Dequeues the finished {@link Task} from the {@link Person#tasks} list
     *
     * @param taskType {@link TaskType} of {@link Task} that has been completed
     * @param executionTime Time in rounds that the execution needed
     */
    private void taskCompleted(TaskType taskType, int executionTime, int individualDuration) {
        // Only do anything, if there is a task left in the que and the top one matches the TaskType
        if (tasks.size() != 0 && taskType == TaskType.getMatchingTask(tasks.get(0))) {

            // Logging needs to be performed before the task is removed. Otherwise, index exceptions could happen
            if (!taskType.isSelfServing()) {
                CivilianPlugin.logger.info(
                        String.format("%s has finished the task '%s' in %d rounds. Expected %d rounds.",
                                name, tasks.get(0).value, executionTime, individualDuration));

                // Self-serving tasks should not contribute to the efficiency score
                // They would distort the actual efficiency, due to the fact, that their efficiency always is 1
                CivilianPlugin.addTaskCompletionTime(individualDuration, executionTime);
            } else {
                CivilianPlugin.logger.debug(
                        String.format("%s has finished the task '%s' in %d rounds. Expected %d rounds.",
                                name, tasks.get(0).value, executionTime, individualDuration));
            }

            isEnqueued = false;

            // Only civilians should ever end to perform a task, so the task should only be removed for civilians
            // All worker classes have to perform their task until the simulation ends
            if (this instanceof Civilian) {
                tasks.remove(0);
                setSpeed(individualSpeed);
            }
        }
    }
    
    /**
     * Calculate the speed factor for a person based on its characteristics.
     * @return Arithmetic mean of all characteristics, limited to the range [0.8;1.2].
     */
    public double getSpeedFactor() {

        double factor = 0;
        if (!removed) {
            if (characteristics.length > 0) {

                // Calculate the average factor based on the persons characteristics
                for (Characteristic characteristic :
                        characteristics) {
                    factor += characteristic.value;
                }
                factor = factor / characteristics.length;

                // The factor needs to be limited. Otherwise, walking on the map can take ages
                if (factor < 0.8) {
                    factor = 0.8;
                } else if (factor > 1.2) {
                    factor = 1.2;
                }
            }
        }

        return factor;
    }
}

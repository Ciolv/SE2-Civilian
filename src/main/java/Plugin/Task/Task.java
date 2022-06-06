package Plugin.Task;

import Plugin.Civilian.Person.Person;
import Plugin.CivilianPlugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.DirectedMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Task extends StaticEntity {
    int duration;
    Dictionary<TaskMessage, TaskTimer> queue = new Hashtable<>();
    TaskType taskType;
    TaskType[] applicableTaskTypes;
    private ArrayList<Message> knownMessages = new ArrayList<>();
    private ArrayList<Message> usedMessages = new ArrayList<>();

    public Task(String taskType, Integer duration) {
        applicableTaskTypes = applicableTasksFromString(taskType);
        if (applicableTaskTypes.length > 0) {
            this.taskType = applicableTaskTypes[0];
        }
        this.duration = duration;
    }

    /**
     * Get the {@link Task} from the string and its counterpart.
     *
     * @param task {@link TaskType#value} that the array should be based on
     * @return The {@link Task} from the string and its counterpart.
     */
    public TaskType[] applicableTasksFromString(String task) {
        this.taskType = TaskType.fromValue(task);

        if (taskType != null) {
            return new TaskType[]{
                    taskType,
                    TaskType.getMatchingTask(taskType)
            };
        } else {
            if (Arrays.stream(TaskType.values()).toList().contains(task)) {
                CivilianPlugin.logger.warn(String.format("%s has not been implemented.", task));
            } else {
                CivilianPlugin.logger.error(String.format("%s is not a valid task!", task));
            }

            return new TaskType[]{};
        }
    }

    /**
     * Validate whether a {@link TaskType} list contains any task type applicable for this task or not.
     * @param taskTypes {@link TaskType} list that should be validated for applicability.
     * @return True if a {@link TaskType} matches with the applicable TaskTypes, False otherwise.
     */
    public boolean taskIsApplicable(TaskType[] taskTypes) {
        return Arrays.stream(applicableTaskTypes).anyMatch(tType -> Arrays.stream(
                taskTypes).anyMatch(personTaskType -> tType == personTaskType));
    }

    /**
     * Validate whether the provided {@link TaskType} is applicable to this task
     *
     * @param taskType {@link TaskType} that should be validated for applicability.
     * @return True if a the provided {@link TaskType} matches with any member of {@link Task#applicableTaskTypes}, False otherwise.
     */
    public boolean taskIsApplicable(TaskType taskType) {
        return taskIsApplicable(new TaskType[] {taskType});
    }

    public void receiveMessage(Message m) {
        boolean isDirectedMessage =
                Arrays.stream(m.getClass().getInterfaces()).anyMatch(i -> i == DirectedMessage.class);

        // It seems as if the simulation delivers a single message multiple times
        // So we need to ensure, that we do not process the same message over and over again
        // Otherwise, tasks could be finished without ever being performed
        if (!knownMessages.contains(m) &&isDirectedMessage) {

            // Only messages that are directed to this Task should be processed
            boolean isDirectedToThisTask = ((DirectedMessage)m).getTarget().equals(this);
            if (isDirectedToThisTask) {
                if (m instanceof TaskMessage) {
                    TaskType toComplete = ((TaskMessage) m).getTaskToComplete();
                    TaskType toPerform = ((TaskMessage) m).getTaskToPerform();

                    // Only if the message has a applicable TaskType it should be enqueued
                    if (toComplete.equals(taskType) || toPerform.equals(taskType)) {
                        enqueue((TaskMessage) m);

                        // Logging
                        CivilianPlugin.logger.debug(String.format(
                                "Registered task '%s' by %s '%d' looking for '%s' at Task '%d'",
                                toPerform.value,
                                m.getOrigin().getClass().getSimpleName(),
                                m.getOrigin().getUID(),
                                toComplete.value,
                                this.getUID()
                        ));
                    } else {
                        // Logging
                        CivilianPlugin.logger.error(
                                String.format("'%s' cannot be served at this task. Valid TaskTypes are %s",
                                        ((TaskMessage) m).getTaskToComplete().value,
                                        applicableTaskTypeString()
                                )
                        );
                    }
                } else {
                    // Logging
                    CivilianPlugin.logger.error(
                            String.format("'%s' can not be performed at this %s", m, this.getClass())
                    );
                }
            }

            knownMessages.add(m);
        }
    }

    /**
     * @return Comma separated string containing all applicable {@link TaskType#value}s
     */
    private String applicableTaskTypeString() {
        List<String> tt = Arrays.stream(applicableTaskTypes).map(t -> t.value).collect(Collectors.toList());

        String taskTypeString = String.join("', '", tt);

        return "'" + taskTypeString + "'";
    }

    /**
     * Enqueue for the task, so that it can be performed later on.
     * @param message
     */
    private void enqueue(TaskMessage message) {
        // Needs to be validated outside this method.
        queue.put(message, new TaskTimer(duration, ((Person)message.getOrigin()).getSpeedFactor()));
    }

    /**
     * Get the {@link TaskType} of the Task.
     *
     * Enqueued Persons with the same {@link TaskType} will be considered as persons, that can fulfill this task.
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * Process a {@link TaskMessage} that has no need for a counterpart message
     *
     * Provide messages that fulfill {@link TaskType#isSelfServing()}
     *
     * @param message The self-serving message that should be processed
     */
    private void performSelfServingTask(TaskMessage message) {
        if (!usedMessages.contains(message)) {
            TaskTimer taskTimer = queue.get(message);
            if (taskTimer != null) {
                taskTimer.decreaseDuration();
                usedMessages.add(message);

                if (taskTimer.durationExceeded()) {
                    getWorld().sendMessage(new CompletionTaskMessage(message.getOrigin(),
                            message.getOrigin(),
                            message.getTaskToPerform(),
                            taskTimer.getWaitingTime(),
                            taskTimer.getExpectedDuration()));

                    queue.remove(message);
                }
            }
        }
    }

    /**
     * Try to process a {@link TaskMessage} that needs a counterpart message
     *
     * Provide messages that do not fulfill {@link TaskType#isSelfServing()}
     *
     * @param message The message that should be processed
     */
    private void performNonSelfServingTask(TaskMessage message) {
        TaskTimer taskTimer = queue.get(message);
        if (taskTimer != null ) {
            // Only consumers should start tasks execution
            if (taskType == message.getTaskToComplete()) {

                // Find first message that matches the TaskType required to complete this message.
                List<TaskMessage> matchingMessages = Collections.list(
                        queue.keys()).stream().filter(
                        taskMessage -> message.getTaskToComplete().equals(taskMessage.getTaskToPerform())
                ).toList();
                TaskMessage partnerMessage = null;

                // Furthermore, messages can only be used once per round
                for (TaskMessage matchingMessage:
                     matchingMessages) {
                    if (!usedMessages.contains(matchingMessage)){
                        partnerMessage = matchingMessage;
                        usedMessages.add(matchingMessage);
                        break;
                    }
                }

                // If one is found, let them both complete their work
                if (partnerMessage != null) {
                    var partnerTaskTimer = queue.get(partnerMessage);
                    taskTimer.decreaseDuration();
                    partnerTaskTimer.decreaseDuration();

                    // Send completion messages, if the minimal working time is exceeded
                    if (taskTimer.durationExceeded() && partnerTaskTimer.durationExceeded()) {
                        getWorld().sendMessage(new CompletionTaskMessage(partnerMessage.getOrigin(),
                                message.getOrigin(),
                                message.getTaskToComplete(),
                                taskTimer.getWaitingTime(),
                                taskTimer.getExpectedDuration()
                        ));
                        getWorld().sendMessage(new CompletionTaskMessage(message.getOrigin(),
                                partnerMessage.getOrigin(),
                                partnerMessage.getTaskToComplete(),
                                partnerTaskTimer.getWaitingTime(),
                                partnerTaskTimer.getExpectedDuration()
                        ));

                        queue.remove(message);
                        queue.remove(partnerMessage);
                    }
                }
            }
        }
    }

    /**
     * Increases the round counter for each person in queue by one.
     */
    private void increaseRoundCounter() {
        for (TaskMessage enqueuedMessage :
                Collections.list(queue.keys())) {
            TaskTimer taskTimer = queue.get(enqueuedMessage);
            taskTimer.increaseWaitingTime();
        }
    }

    /**
     * Performs task execution for all producers and as many consumers as possible.
     *
     * As many consumers as possible means, that if the number of consumers in the queue is higher than the number
     * of producers, not all consumers can be served.
     */
    public void pluginUpdate() {
        increaseRoundCounter();
        usedMessages.clear();

        for (TaskMessage message : Collections.list(queue.keys())) {
            if (queue.get(message) != null ) {
                if (message.getTaskToComplete().isSelfServing()) {
                    performSelfServingTask(message);
                } else {
                    performNonSelfServingTask(message);
                }
            }
        }
    }
}

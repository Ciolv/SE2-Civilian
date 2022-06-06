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

    public Task(String taskType, Integer duration) {
        applicableTaskTypes = applicableTasksFromString(taskType);
        if (applicableTaskTypes.length > 0) {
            this.taskType = applicableTaskTypes[0];
        }
        this.duration = duration;
    }

    public TaskType[] applicableTasksFromString(String s) {
        this.taskType = TaskType.fromValue(s);

        if (taskType != null) {
            return new TaskType[]{
                    taskType,
                    TaskType.getMatchingTask(taskType)
            };
        } else {
            if (Arrays.stream(TaskType.values()).toList().contains(s)) {
                CivilianPlugin.logger.warn(String.format("%s has not been implemented.", s));
            } else {
                CivilianPlugin.logger.error(String.format("%s is not a valid task!", s));
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

    public boolean taskIsApplicable(TaskType taskType) {
        return taskIsApplicable(new TaskType[] {taskType});
    }

    public void receiveMessage(Message m) {
        boolean isDirectedMessage =
                Arrays.stream(m.getClass().getInterfaces()).anyMatch(i -> i == DirectedMessage.class);

        // Only messages that are directed to this task should be performed and only of the TaskType matches.
        // If so, enqueue.
        if (!knownMessages.contains(m) &&isDirectedMessage) {
            boolean isDirectedToThisTask = ((DirectedMessage)m).getTarget().equals(this);
            if (isDirectedToThisTask) {
                if (m instanceof TaskMessage) {
                    TaskType toComplete = ((TaskMessage) m).getTaskToComplete();
                    TaskType toPerform = ((TaskMessage) m).getTaskToPerform();

                    if (toComplete.equals(taskType) || toPerform.equals(taskType)) {
                        enqueue((TaskMessage) m);
                        CivilianPlugin.logger.debug(String.format(
                                "Registered task '%s' by %s '%d' looking for '%s' at Task '%d'",
                                toPerform.value,
                                m.getOrigin().getClass().getSimpleName(),
                                m.getOrigin().getUID(),
                                toComplete.value,
                                this.getUID()
                        ));
                    } else {
                        CivilianPlugin.logger.error(
                                String.format("'%s' cannot be served at this task. Valid TaskTypes are %s",
                                        ((TaskMessage) m).getTaskToComplete().value,
                                        applicableTaskTypeString()
                                )
                        );
                    }
                } else {
                    CivilianPlugin.logger.error(
                            String.format("'%s' can not be performed at this %s", m, this.getClass())
                    );
                }
            } else {
                if (m instanceof TaskMessage) {
                    CivilianPlugin.logger.debug(
                            String.format("%s '%d' is not the target of the message directed to  %s '%d'",
                                    this.getClass().getSimpleName(),
                                    this.getUID(),
                                    ((DirectedMessage) m).getTarget().getClass().getSimpleName(),
                                    ((DirectedMessage) m).getTarget().getUID()
                            ));
                } else {
                    CivilianPlugin.logger.debug(
                            String.format("'%s' can not be performed at this %s", m, this.getClass())
                    );
                }

            }
            knownMessages.add(m);
        }
    }

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
        // Only enqueue if the person has an applicable {@link TaskType}.
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

    private void performSelfServingTask(TaskMessage message) {
        TaskTimer taskTimer = queue.get(message);
        if (taskTimer != null ) {
            taskTimer.decreaseDuration();

            if (taskTimer.durationExceeded()) {
                getWorld().sendMessage(new CompletionTaskMessage(message.getOrigin(),
                        message.getOrigin(),
                        message.getTaskToPerform(),
                        taskTimer.getWaitingTime(),
                        taskTimer.getIndividualDuration()));

                queue.remove(message);
            }
        }
    }

    private void performNonSelfServingTask(TaskMessage message) {
        TaskTimer taskTimer = queue.get(message);
        if (taskTimer != null ) {
            taskTimer.decreaseDuration();

//            Iterator<TaskMessage> iterator = queue.keys().asIterator();
//            TaskMessage partnerMessage = null;
//
//            while (iterator.hasNext()) {
//                TaskMessage taskMessage = iterator.next();
//                TaskType toPerform = taskMessage.getTaskToPerform();
//                TaskType searched = message.getTaskToComplete();
//
//                if (toPerform.equals(searched)) {
//                    partnerMessage = taskMessage;
//                    break;
//                }
//            }

            // Find first message that matches the TaskType required to complete this message.
            List<TaskMessage> matchingMessages = Collections.list(
                    queue.keys()).stream().filter(
                    taskMessage -> message.getTaskToComplete().equals(taskMessage.getTaskToPerform())
            ).toList();
            TaskMessage partnerMessage = matchingMessages.size() > 0 ? matchingMessages.get(0) : null;

            // If one is found, let them both complete their work
            if (partnerMessage != null) {
                if (taskTimer.durationExceeded()) {
                    getWorld().sendMessage(new CompletionTaskMessage(partnerMessage.getOrigin(),
                            message.getOrigin(),
                            partnerMessage.getTaskToComplete(),
                            taskTimer.getWaitingTime(),
                            taskTimer.getIndividualDuration()
                    ));
                    getWorld().sendMessage(new CompletionTaskMessage(message.getOrigin(),
                            partnerMessage.getOrigin(),
                            message.getTaskToComplete(),
                            taskTimer.getWaitingTime(),
                            taskTimer.getIndividualDuration()
                    ));

                    queue.remove(message);
                    queue.remove(partnerMessage);
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

        for (TaskMessage message : Collections.list(queue.keys())) {

            if (message.getTaskToComplete() == message.getTaskToPerform()) {
                performSelfServingTask(message);
            } else {
                performNonSelfServingTask(message);
            }
        }
    }
}

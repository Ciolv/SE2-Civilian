package Plugin.Task;

import Plugin.Civilian.Person.Person;
import Plugin.CivilianPlugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.stream.Stream;

public class Task extends StaticEntity {
    String Name;
    int duration;
    Dictionary<TaskMessage, TaskTimer> queue;
    Point position;
    TaskType taskType;
    TaskType[] applicableTaskTypes;

    /**
     * Validate whether a {@link TaskType} list contains any task type applicable for this task or not.
     * @param taskTypes {@link TaskType} list that should be validated for applicability.
     * @return True if a {@link TaskType} matches with the applicable TaskTypes, False otherwise.
     */
    boolean taskIsApplicable(TaskType[] taskTypes) {
        return Arrays.stream(applicableTaskTypes).anyMatch(tType -> Arrays.stream(
                taskTypes).anyMatch(personTaskType -> tType == personTaskType));
    }

    @Override
    public void receiveMessage(Message m) {
        if (m instanceof TaskMessage) {
            if (((TaskMessage)m).getTaskToComplete() == taskType ||
                    ((TaskMessage)m).getTaskToPerform() == taskType) {
                enqueue((TaskMessage) m);
            } else {
                CivilianPlugin.logger.error(
                        String.format("%s cannot be served at this task. Valid TaskTypes are %s",
                                ((TaskMessage) m).getTaskToComplete().name(),
                                applicableTaskTypes.toString()
                        )
                );
            }
        } else {
            CivilianPlugin.logger.error(
                    String.format("'%s' can not be performed at this %s", m.toString(), this.getClass())
            );
        }
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

    /**
     * Sorts the current queue so that that it starts with all consumers and lists producers at the end.
     *
     * This sorting will allow to first calculate the demand for the producers and work on them afterwards.
     *
     * @return Sorted array beginning with consumers and ending wich producers.
     */
    private Person[] sortWorkingQueue() {
        Person[] consumer = getAllConsumers().toArray(Person[]::new);
        Person[] producer = getAllProducers().toArray(Person[]::new);

        // Merging both arrays into one array with consumers first and producers afterwards.
        Person[] sortedQueue = new Person[consumer.length + producer.length];
        System.arraycopy(consumer, 0, sortedQueue, 0,consumer.length);
        System.arraycopy(producer, 0, sortedQueue, consumer.length, producer.length);

        return sortedQueue;
    }

    /**
     * Returns a {@link Person} array, containing all elements in queue that are consumers.
     *
     * Consumers are all entities in the queue, having the same {@link TaskType} as the Task.
     */
    private Stream<Person> getAllConsumers() {
        Stream<TaskMessage> consumerMessages = Collections.list(queue.keys()).stream().filter(
                q -> Arrays.asList(q.getTaskToComplete()).equals(this.taskType)
        );

        return consumerMessages.map(taskMessage -> (Person)taskMessage.getOrigin());
    }

    /**
     * Returns a {@link Person} array, containing all elements in queue that are producers.
     *
     * Consumers are all entities in the queue, having the same {@link TaskType} as the Task.
     */
    private Stream<Person> getAllProducers() {
        Stream<TaskMessage> consumerMessages = Collections.list(queue.keys()).stream().filter(
                q -> !Arrays.asList(q.getTaskToComplete()).equals(this.taskType)
        );

        return consumerMessages.map(taskMessage -> (Person)taskMessage.getOrigin());
    }

    private void performSelfServingTask(TaskMessage message) {
        TaskTimer taskTimer = queue.get(message);
        taskTimer.decreaseDuration();

        if (taskTimer.durationExceeded()) {
            getWorld().sendMessage(new CompletionTaskMessage(message.getTarget(),
                    message.getOrigin(),
                    message.getTaskToPerform(),
                    taskTimer.getWaitingTime(),
                    taskTimer.getIndividualDuration()));

            queue.remove(message);
        }
    }

    private void performNonSelfServingTask(TaskMessage message) {
        TaskTimer taskTimer = queue.get(message);
        taskTimer.decreaseDuration();

        Stream<TaskMessage> partnerMessageStream = Collections.list(queue.keys()).stream().filter(
                taskMessage -> taskMessage.getTaskToPerform() == message.getTaskToComplete()
        ).limit(1);

        if (partnerMessageStream.count() > 0) {
            TaskMessage partnerMessage = partnerMessageStream.findFirst().get();
            if (taskTimer.durationExceeded()) {
                getWorld().sendMessage(new CompletionTaskMessage(message.getTarget(),
                        message.getOrigin(),
                        message.getTaskToPerform(),
                        taskTimer.getWaitingTime(),
                        taskTimer.getIndividualDuration()));
                getWorld().sendMessage(new CompletionTaskMessage(partnerMessage.getTarget(),
                        partnerMessage.getOrigin(),
                        partnerMessage.getTaskToPerform(),
                        taskTimer.getWaitingTime(),
                        taskTimer.getIndividualDuration()));

                queue.remove(message);
                queue.remove(partnerMessage);
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
     * Increases the round counter for each person in queue by one.
     */
    private void increaseRoundCounter() {
        for (Person waitingPerson :
                Collections.list(queue.keys())) {
            int round = queue.get(waitingPerson);
            queue.put(waitingPerson, round + 1);
        }
    }

    public void transporting_luggage(double pSpeed ){

    }

    public void walking(double pSpeed ){
    }

    public void cleaning(double pSpeed ){
    }

    public void buy_ticket(double pSpeed ){
    }

    public void sell_ticket(double pSpeed ){
    }

    public void ask_for_direction(double pSpeed ){
    }

    public void tell_direction(double pSpeed ){
    }

    /**
     * Performs task execution for all producers and as many consumers as possible.
     *
     * As many consumers as possible means, that if the number of consumers in the queue is higher than the number
     * of producers, not all consumers can be served.
     */

    @Override
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

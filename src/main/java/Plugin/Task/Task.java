package Plugin.Task;

import Plugin.Civilian.Person.Person;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.stream.Stream;

public class Task extends StaticEntity {
    String Name;
    int duration;
    Dictionary<Person, Integer> queue;
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

    /**
     * Enqueue for the task, so that it can be performed later on.
     * @param person
     */
    public void enqueue(Person person) {
        // Only enqueue if the person has an applicable {@link TaskType}.
        if (taskIsApplicable(person.getTaskTypes())) {
            queue.put(person, 0);
        } else {
            throw new RuntimeException("Person tried to enqueue for task that it is not allowed not perform");
        }
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
        return Collections.list(queue.keys()).stream().filter(
                q -> Arrays.asList(q.getTaskTypes()).contains(this.taskType)
        );
    }

    /**
     * Returns a {@link Person} array, containing all elements in queue that are producers.
     *
     * Consumers are all entities in the queue, having the same {@link TaskType} as the Task.
     */
    private Stream<Person> getAllProducers() {
        List<Person> consumers = getAllConsumers().toList();

        // All persons in the queue that are no consumers are producers.
        return Collections.list(queue.keys()).stream().filter(
                q -> !consumers.contains(q)
        );
    }

    /**
     * Performs task execution for all producers and as many consumers as possible.
     *
     * As many consumers as possible means, that if the number of consumers in the queue is higher than the number
     * of producers, not all consumers can be served.
     */
    public void run()  {
        increaseRoundCounter();

        List<Person> consumers =  getAllConsumers().toList();
        Person[] producers = getAllProducers().toArray(Person[]::new);

        for (Person producer:
             producers) {

            // Producer performs task of the consumer
            if (!consumers.isEmpty()) {
                Person consumer = consumers.get(0);

                // Task is completed, when a person has exceeded its minimum task duration
                if (consumer.getSpeedFactor() * duration <= queue.get(consumer)) {
                    consumer.taskCompleted(taskType, queue.get(consumer));
                    queue.remove(consumers.get(0));
                }
            }

            // Whether the producer can perform a task for the consumer or not, it can only perform it in this round.
            queue.remove(producer);
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

    public void luggageCheckIn(double pSpeed ){


    }

    @Override
    public void pluginUpdate() {

    }
}

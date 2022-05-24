package Task;

import Civilian.Person.Person;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;
import jdk.jshell.spi.ExecutionControl;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Task extends StaticEntity {
    String Name;
    int duration;
    List<Person> queue;
    Point position;
    TaskType taskType;
    TaskType[] applicableTaskTypes;

    boolean validateTask() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    public void addConsumer(Person person) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    public void addWorker(Person person) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

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
        return queue.stream().filter(
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
        return queue.stream().filter(
                q -> !consumers.contains(q)
        );
    }

    /**
     * Performs task execution for all producers and as many consumers as possible.
     *
     * As many consumers as possible means, that if the number of consumers in the queue is higher than the number
     * of producers, not all consumers can be served.
     *
     * @throws ExecutionControl.NotImplementedException
     */
    public void run() throws ExecutionControl.NotImplementedException {
        List<Person> consumers =  getAllConsumers().toList();
        Person[] producers = getAllProducers().toArray(Person[]::new);

        for (Person producer:
             producers) {

            // Producer performs task of the consumer
            if (!consumers.isEmpty()) {
                Person consumer = consumers.get(0);
                consumer.taskCompleted(taskType);
                queue.remove(consumers.get(0));
            }

            // Whether the producer can perform a task for the consumer or not, it can only perform it in this round.
            queue.remove(producer);
        }
    }

    @Override
    public void pluginUpdate() {

    }
}

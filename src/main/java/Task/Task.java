package Task;

import Civilian.Person.Person;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;
import jdk.jshell.spi.ExecutionControl;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Task extends StaticEntity {
    String Name;
    int duration;
    Person[] queue;
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
    /**
     * Sorts the current queue so that that it starts with all consumers and lists producers at the end.
     *
     * This sorting will allow to first calculate the demand for the producers and work on them afterwards.
     *
     * @return Sorted array beginning with consumers and ending wich producers.
     */
    private Person[] sortWorkingQueue() {
        Person[] consumer = getAllConsumers();
        Person[] producer = getAllProducers();

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
    private Person[] getAllConsumers() {
        return Arrays.stream(queue).filter(
                q -> Arrays.asList(q.getTaskTypes()).contains(this.taskType)
        ).toArray(Person[]::new);
    }

    /**
     * Returns a {@link Person} array, containing all elements in queue that are producers.
     *
     * Consumers are all entities in the queue, having the same {@link TaskType} as the Task.
     */
    private Person[] getAllProducers() {
        List<Person> consumers = Arrays.stream(getAllConsumers()).toList();

        // All persons in the queue that are no consumers are producers.
        return Arrays.stream(queue).filter(
                q -> !consumers.contains(q)
        ).toArray(Person[]::new);
    }

    @Override
    public void pluginUpdate() {

    }
}

package Task;

import Civilian.Person.Person;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.StaticEntity;
import jdk.jshell.spi.ExecutionControl;

import java.awt.*;

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
    @Override
    public void pluginUpdate() {

    }
}

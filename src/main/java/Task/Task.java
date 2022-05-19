package Task;

import Civilian.Person.Person;
import jdk.jshell.spi.ExecutionControl;

import java.awt.*;

public class Task {
    String Name;
    int duration;
    Person[] queue;
    Point position;

    boolean validateTask() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void push(Person person) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void pop() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }
}

package Civilian.Person;

import Task.Task;
import aas.model.communication.Message;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Agent;
import jdk.jshell.spi.ExecutionControl;

import java.awt.*;

public class Person extends Agent {
    int id;
    Point position;
    Characteristic[] characteristics;
    Task[] tasks;

    void calculateMovement()throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void runTask() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void measureTaskTimer() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void remove() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void handleMessage(Message message) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    void sendMessage() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }

    @Override
    public void pluginUpdate() {
        // TODO: implement...
    }
}
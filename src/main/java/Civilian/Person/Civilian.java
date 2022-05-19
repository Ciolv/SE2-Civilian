package Civilian.Person;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import jdk.jshell.spi.ExecutionControl;

public class Civilian extends Person {
    boolean hasCabinLuggage;
    boolean hasLuggage;

    Entity findNextEntity(String entityID) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");
    }
}

package Plugin.Civilian.Person;

import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import jdk.jshell.spi.ExecutionControl;

public class Civilian extends Person {

    protected TaskType[] taskTypes = new TaskType[] {
            TaskType.WALKING,
            TaskType.ASK_FOR_DIRECTION,
            TaskType.BUY_TICKET
    };
    boolean hasCabinLuggage;
    boolean hasLuggage;

    Entity findNextEntity(String entityID) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not Implemented");

    }


    boolean getHasLuggae(){
        double rand = Math.random()*(0-1);
        int bool= (int) Math.round(rand);
        if(bool== 1){
            hasLuggage= true;
        }else {
            hasLuggage= false;
        }
        return hasLuggage;
    }
}

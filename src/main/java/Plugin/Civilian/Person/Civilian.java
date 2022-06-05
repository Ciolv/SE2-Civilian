package Plugin.Civilian.Person;

import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import jdk.jshell.spi.ExecutionControl;

public class Civilian extends Person {

    boolean hasCabinLuggage;
    boolean hasLuggage;

    public Civilian(Point position) {
        super(position);
        throwLuggageDice();
        taskTypes = new TaskType[] {
                TaskType.ASK_FOR_DIRECTION,
                TaskType.BUY_TICKET
        };

        int taskIndex = (int)(Math.random() * 2 * getSpeedFactor());
        tasks.add(taskTypes[taskIndex]);
    }

    void throwLuggageDice() {
        double rand = Math.random();
        int bool= (int) Math.round(rand);
        if(bool== 1){
            hasLuggage= true;
        }else {
            hasLuggage= false;
        }
    }

    boolean hasLuggage(){
        return hasLuggage;
    }
}

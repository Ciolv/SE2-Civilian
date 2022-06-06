package Plugin.Civilian.Person;

import Plugin.Task.TaskType;

public class Civilian extends Person {

    boolean hasCabinLuggage;
    boolean hasLuggage;

    public Civilian(String name, String[] tasks, String[] characteristics) {
        super(name, new TaskType[] {
                TaskType.ASK_FOR_DIRECTION,
                TaskType.BUY_TICKET
        }, tasks, characteristics);

        throwLuggageDice();
    }

    public boolean hasLuggage(){
        return hasLuggage;
    }

    public boolean hasCabinLuggage(){
        return hasCabinLuggage;
    }

    /**
     * Decides randomly, whether the {@link Civilian#hasLuggage} property is true or false.
     *
     * Should only be used once, while instantiating the class.
     */
    void throwLuggageDice() {
        double rand = Math.random();
        int bool= (int) Math.round(rand);
        if(bool== 1){
            hasLuggage= true;
        }else {
            hasLuggage= false;
        }
    }

}

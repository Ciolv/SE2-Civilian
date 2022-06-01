package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.DirectedMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.LocalMessage;

public abstract class TaskMessage implements LocalMessage, DirectedMessage {
    private Entity origin;
    private Entity target;
    private int maxRange = 1;
    private Point originPosition;

    static TaskType taskToComplete;
    static TaskType taskToPerform;


    public TaskMessage(Entity origin, Entity target) {
        this.origin = origin;
        this.target = target;
        originPosition = new Point(origin.getPosition().getX(), origin.getPosition().getY());
    }

    public TaskType getTaskToComplete() {
        return taskToComplete;
    }
    public  TaskType getTaskToPerform() {
        return taskToPerform;
    }

    @Override
    public Entity getTarget() {
        return target;
    }

    @Override
    public int getMaxRange() {
        return maxRange;
    }

    @Override
    public Entity getOrigin() {
        return origin;
    }

    @Override
    public Point getOriginPosition() {
        return originPosition;
    }
}

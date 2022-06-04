package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.DirectedMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.LocalMessage;
import jdk.jshell.spi.ExecutionControl;

public class CompletionTaskMessage implements DirectedMessage, LocalMessage {

    public CompletionTaskMessage(Entity origin, Entity target, TaskType completedTask,
                                 int executionTime, double individualDuration) {
        this.origin = origin;
        this.target = target;
        this.completedTask = completedTask;
        this.executionTime = executionTime;
        this.individualDuration = individualDuration;
        originPosition = new Point(origin.getPosition().getX(), origin.getPosition().getY());
    }

    private Entity origin;
    private Entity target;
    private int maxRange = 1;
    private Point originPosition;

    private int executionTime;
    private double individualDuration;
    static TaskType completedTask;

    public TaskType getCompletedTask() {
        return completedTask;
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

    @Override
    public void fromString(String s) {
        // Laaa leee luuu nur der Mann im Mond schaut zu 😅
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public double getIndividualDuration() {
        return individualDuration;
    }
}

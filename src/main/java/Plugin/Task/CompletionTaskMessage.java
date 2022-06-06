package Plugin.Task;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.DirectedMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.LocalMessage;

public class CompletionTaskMessage implements DirectedMessage, LocalMessage {

    private Entity origin;
    private Entity target;
    private int maxRange = 1;
    private Point originPosition;

    private int executionTime;
    private double individualDuration;
    private TaskType completedTask;

    public CompletionTaskMessage(Entity origin, Entity target, TaskType completedTask,
                                 int executionTime, double individualDuration) {
        this.origin = origin;
        this.target = target;
        this.completedTask = completedTask;
        this.executionTime = executionTime;
        this.individualDuration = individualDuration;
        originPosition = new Point(origin.getPosition().getX(), origin.getPosition().getY());
    }


    public TaskType getCompletedTask() {
        return completedTask;
    }

    public Entity getTarget() {
        return target;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public double getIndividualDuration() {
        return individualDuration;
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

    /**
     * DO NOT USE
     *
     * There is no meaningful use case for a string {@link CompletionTaskMessage}
     */
    @Override
    public void fromString(String s) {
        // Laaa leee luuu nur der Mann im Mond schaut zu 😅
    }
}

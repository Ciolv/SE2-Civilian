package Plugin.Task;

import Plugin.CivilianPlugin;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.DirectedMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.LocalMessage;

import java.util.ArrayList;

public class TaskMessage implements LocalMessage, DirectedMessage {
    private Entity origin;
    private Entity target;
    private int maxRange = 1;
    private Point originPosition;

    private TaskType taskToComplete;
    private TaskType taskToPerform;

    public TaskMessage(Entity origin, Entity target, String messageContent) {
        this.origin = origin;
        this.target = target;
        originPosition = new Point(origin.getPosition().getX(), origin.getPosition().getY());
        fromString(messageContent);
    }

    public static ArrayList<String> getValidMessages() {
        ArrayList<String> messages = new ArrayList<>();

        for (TaskType tt:
                TaskType.values()) {
            messages.add(tt.value);
        }

        return messages;
    }

    public TaskType getTaskToComplete() {
        return taskToComplete;
    }
    public TaskType getTaskToPerform() {
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

    @Override
    public void fromString(String s) {
        taskToPerform = TaskType.fromValue(s);

        if (taskToPerform != null) {
            taskToComplete = TaskType.getMatchingTask(taskToPerform);
        }else {
            if (getValidMessages().contains(s)) {
                CivilianPlugin.logger.warn(String.format("%s has not been implemented.", s));
            } else {
                CivilianPlugin.logger.error(String.format("%s is not a valid message!", s));
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%d asks %d to register task '%s' that can be served with '%s' from (%d|%d)",
                origin.getUID(),
                target.getUID(),
                taskToComplete.value,
                taskToPerform.value,
                originPosition.getX(),
                originPosition.getY()
        );
    }
}

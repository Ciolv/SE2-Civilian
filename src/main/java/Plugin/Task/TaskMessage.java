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
    private int maxRange;
    private Point originPosition;

    private TaskType taskToComplete;
    private TaskType taskToPerform;

    public TaskMessage(Entity origin, Task target, String messageContent) {
        this.origin = origin;
        this.target = target;
        this.maxRange = target.getRange();
        originPosition = new Point(origin.getPosition().getX(), origin.getPosition().getY());
        fromString(messageContent);
    }

    public TaskType getTaskToComplete() {
        return taskToComplete;
    }
    public TaskType getTaskToPerform() {
        return taskToPerform;
    }

    public Entity getTarget() {
        return target;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public Entity getOrigin() {
        return origin;
    }

    public Point getOriginPosition() {
        return originPosition;
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

    /**
     * Set the {@link TaskMessage#taskToPerform} and {@link TaskMessage#taskToComplete} based on a {@link TaskType#value}
     *
     * The {@link TaskMessage#taskToComplete} will be set to the matching counterpart of {@link TaskMessage#taskToPerform}
     *
     * @param s The {@link TaskType#value} that should be set as {@link TaskMessage#taskToPerform}
     */
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


    /**
     * @return String list of all {@link TaskType}s
     */
    public static ArrayList<String> getValidMessages() {
        ArrayList<String> taskTypes = new ArrayList<>();

        for (TaskType tt:
                TaskType.values()) {
            taskTypes.add(tt.value);
        }

        return taskTypes;
    }
}

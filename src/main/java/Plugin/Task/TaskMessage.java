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

    static TaskType taskToComplete;
    static TaskType taskToPerform;

    public TaskMessage(Entity origin, Entity target) {
        this.origin = origin;
        this.target = target;
        originPosition = new Point(origin.getPosition().getX(), origin.getPosition().getY());
    }

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

    public static TaskType getTaskToComplete() {
        return taskToComplete;
    }
    public static TaskType getTaskToPerform() {
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
        switch (s) {
            case "Walk" -> {
                taskToPerform = TaskType.WALKING;
                taskToComplete = TaskType.WALKING;
            }
            case "Clean" -> {
                taskToPerform = TaskType.CLEANING;
                taskToComplete = TaskType.CLEANING;
            }
            case "Transport luggage" -> {
                taskToPerform = TaskType.TRANSPORTING_LUGGAGE;
                taskToComplete = TaskType.TRANSPORTING_LUGGAGE;
            }
            case "Buy Ticket" -> {
                taskToPerform = TaskType.BUY_TICKET;
                taskToComplete = TaskType.SELL_TICKET;
            }
            case "Sell Ticket" -> {
                taskToPerform = TaskType.SELL_TICKET;
                taskToComplete = TaskType.BUY_TICKET;
            }
            case "Ask for direction" -> {
                taskToPerform = TaskType.ASK_FOR_DIRECTION;
                taskToComplete = TaskType.TELL_DIRECTION;
            }
            case "Tell direction" -> {
                taskToPerform = TaskType.TELL_DIRECTION;
                taskToComplete = TaskType.ASK_FOR_DIRECTION;
            }
            default -> {
                if (getValidMessages().contains(s)) {
                    CivilianPlugin.logger.warn(String.format("%s has not been implemented.", s));
                } else {
                    CivilianPlugin.logger.error(String.format("%s is not a valid message!", s));
                }
            }
        }
    }
}

package Plugin.Civilian.Person;

import Plugin.CivilianPlugin;
import Plugin.Task.Task;
import Plugin.Task.TaskType;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleaningWorker extends Person {
    public CleaningWorker(String name, String[] tasks, String[] characteristics) {
        super(name, new TaskType[] {
                TaskType.CLEANING
        }, tasks, characteristics);
    }
}

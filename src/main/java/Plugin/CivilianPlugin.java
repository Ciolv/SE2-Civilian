package Plugin;

import Plugin.Civilian.Person.*;
import Plugin.Task.Task;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.AirportAgentSimulation;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurableAttribute;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationFormatException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.logging.PluginLogger;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;

import static dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.AirportAgentSimulation.registerEntity;

/**
 * This class implements the Plugin interface provided by
 * https://github.com/Vincent200355/AirportAgentSimulation-Base/
 */
public class CivilianPlugin implements Plugin {
    public static PluginLogger logger;
    private static int expectedRounds = 0;
    private static int actualRounds = 0;

    /**
     * Perform all steps to make this plugin usable
     */
    public void activate() {
        logger = AirportAgentSimulation.getLogger(this);
        registerPluginEntities();
    }

    /**
     * Register all entities, the simulation shall be able to use
     */
    private void registerPluginEntities() {

        registerNonPersonEntities();
        registerPersonEntity(Civilian.class);
        registerPersonEntity(CleaningWorker.class);
        registerPersonEntity(Loader.class);
        registerPersonEntity(LuggageDistributor.class);
        registerPersonEntity(TerminalWorker.class);
    }

    /**
     * Register all entities, not extending {@link Person}, the simulation shall be able to use
     */
    private void registerNonPersonEntities() {
        try {
            // Register Task
            registerEntity(this, Task.class.getSimpleName(),Task.class, new ConfigurableAttribute[] {
                    new ConfigurableAttribute("taskType", String.class),
                    new ConfigurableAttribute("duration", Integer.class),
                    new ConfigurableAttribute("range", Integer.class, 1)
            });
        } catch (ConfigurationFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Register all entities, extending {@link Person}, the simulation shall be able to use
     */
    private void registerPersonEntity(Class entityType) {
        if (entityType.getSuperclass() == Person.class) {
            try {
                registerEntity(this, entityType.getSimpleName(), entityType, new ConfigurableAttribute[] {
                        new ConfigurableAttribute("name", String.class),
                        new ConfigurableAttribute("tasks", String[].class),
                        new ConfigurableAttribute("characteristics", String[].class, new String[]{})
                });
                logger.info(String.format("Registered entity %s", entityType.getSimpleName()));
            } catch (ConfigurationFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Add information to the efficiency calculation
     *
     * @param expectedTime The amount of rounds expected to complete the task
     * @param actualTime The actual amount of rounds to complete the task
     */
    public static void addTaskCompletionTime(int expectedTime, int actualTime) {
        expectedRounds += expectedTime;
        actualRounds += actualTime;

        logEfficiency();
    }

    /**
     * Log the current efficiency score
     */
    private static void logEfficiency() {
        double efficiency = actualRounds / expectedRounds;
        logger.info(String.format("%s efficiency score: %.2f", CivilianPlugin.class.getSimpleName(), efficiency));
    }
}

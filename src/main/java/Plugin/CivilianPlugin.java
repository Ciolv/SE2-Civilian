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
 * This Class will furhter implement the Plugin interface provided by
 * https://github.com/Vincent200355/AirportAgentSimulation-Base/
 */
public class CivilianPlugin implements Plugin {
    public static PluginLogger logger;

    public void activate() {
        logger = AirportAgentSimulation.getLogger(this);
        registerPluginEntities();
    }

    public void registerPluginEntities() {

        try {
            registerEntity(this, Task.class.getSimpleName(),Task.class, new ConfigurableAttribute[] {
                    new ConfigurableAttribute("taskType", String.class),
                    new ConfigurableAttribute("duration", Integer.class)
            });
        } catch (ConfigurationFormatException e) {
            throw new RuntimeException(e);
        }

        // parameters
        registerPersonEntity(Civilian.class);
        registerPersonEntity(CleaningWorker.class);
        registerPersonEntity(Loader.class);
        registerPersonEntity(LuggageDistributor.class);
        registerPersonEntity(TerminalWorker.class);


    }

    public void registerPersonEntity(Class entityType) {
        if (entityType.getSuperclass() == Person.class) {
            try {
                registerEntity(this, entityType.getSimpleName(), entityType, new ConfigurableAttribute[] {
                        new ConfigurableAttribute("name", String.class),
                        new ConfigurableAttribute("tasks", String[].class)
                });
                logger.info(String.format("Registered entity %s", entityType.getSimpleName()));
            } catch (ConfigurationFormatException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

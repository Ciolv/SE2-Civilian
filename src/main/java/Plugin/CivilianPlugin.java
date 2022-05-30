package Plugin;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.AirportAgentSimulation;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.logging.PluginLogger;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.plugin.Plugin;

/**
 * This Class will furhter implement the Plugin interface provided by
 * https://github.com/Vincent200355/AirportAgentSimulation-Base/
 */
public class CivilianPlugin implements Plugin {
    public static PluginLogger logger;
    @Override
    public void activate() {
        logger = AirportAgentSimulation.getLogger(this);
    }

    // Maybe already deprecated? No `loadEntityTypes` function found in the simulation...
    public void loadEntityTypes() {

    }
}

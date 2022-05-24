# SE2-Civilian
Civilian agents for the SE2 airport simulation  

## Build Instruction  

To successfully build this Plugin for the [Airport Agent Simulation](https://github.com/Vincent200355/AirportAgentSimulation-Base), the simulation repository has to be cloned on the same level as this repository. That will ensure, that the path dependencies in the `.idea/modules.xml` fits. If the simulation package is located elsewhere, please adjust the path in the `.idea/modules.xml` file and **do not** include this change in any commit.  

Afterwards you need to let IntelliJ create the `AirportAgentSimulation-Base.iml` file, with the following procedure:
`File` &rarr; `Project Structure...` &rarr; `Add` (the + symbol) and add the AirportAgentSimulation-Base directory.

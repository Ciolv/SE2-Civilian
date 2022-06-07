# SE2-Civilian
Civilian agents for the SE2 airport simulation  

## Build Instruction  

**IMPORTANT:** Will only run with the [original project](https://github.com/Vincent200355/AirportAgentSimulation-Base) as soon as pull request [#37](https://github.com/Vincent200355/AirportAgentSimulation-Base/pull/37) was accepted. Until then this [Fork](https://github.com/Ciolv/AirportAgentSimulation-Base) can be used.

To successfully build this Plugin for the [Airport Agent Simulation](https://github.com/Vincent200355/AirportAgentSimulation-Base), the simulation repository has to be cloned on the same level as this repository. That will ensure, that the path dependencies in the `.idea/modules.xml` fits. If the simulation package is located elsewhere, please adjust the path in the `.idea/modules.xml` file and **do not** include this change in any commit.  

Afterwards you need to let IntelliJ create the `AirportAgentSimulation-Base.iml` file, with the following procedure:
`File` &rarr; `Project Structure...` &rarr; `Add` (the + symbol) and add the AirportAgentSimulation-Base directory.

## Configuration  
### Types
| Key    | Value options                                                                          |
|--------|----------------------------------------------------------------------------------------|
| "type" | "Civilian", "CleaningWorker", "Loader", "LuggageDistributor", "TerminalWorker", "Task" |

### Plugin attributes for Persons

*Persons are  "Civilian", "CleaningWorker", "Loader", "LuggageDistributor", "TerminalWorker"*

| Key               | Value options                                                                                                                                     |
|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| "name"            | "\<any string\>"                                                                                                                                  |
| "tasks"           | \[ <br/>"Clean", <br/>"Transport luggage", <br/>"Buy ticket", <br/>"Sell ticket", <br/>"Ask for direction", <br/>"Tell direction" <br/>\]         |
| "characteristics" | \[ <br/> "SUSPICIOUS", <br/>"CONFUSED", "HECTIC",<br/> "SLOW",<br/>"DEFAULT", <br/>"FAST",<br/>"CALM",<br/>"TARGET_ORIENTED",<br/> "BUSY" <br/>\] |


### Plugin attributes for Tasks

| Key               | Value options         |
|-------------------|-----------------------|
| "taskType"            | \[ <br/>"Clean", <br/>"Transport luggage", <br/>"Sell ticket",  <br/>"Tell direction" <br/>\]|
| "duration"           | \<any integer value\> |


### Example configuration  
````json
{
    "seed": 0,
    "width": 500,
    "height": 500,
    "placedEntities": [
        {
            "type": "Civilian",
            "position": [
                2,
                2
            ],
            "generates": [],
            "width": 0,
            "height": 0,
            "pluginAttributes": {
                "name": "Alena",
                "tasks": [
                    "Buy ticket"
                ],
                "characteristics": [
                    "SUSPICIOUS",
                    "CONFUSED",
                    "SLOW"
                ]
            }
        },
        {
            "type": "TerminalWorker",
            "position": [
                2,
                2
            ],
            "generates": [],
            "width": 0,
            "height": 0,
            "pluginAttributes": {
                "name": "Lili",
                "tasks": [
                  "Sell ticket"
                ]
            }
        },
        {
            "type": "CleaningWorker",
            "position": [
                1,
                1
            ],
            "generates": [],
            "width": 0,
            "height": 0,
            "pluginAttributes": {
                "name": "Heinrich",
                "tasks": [
                    "Clean"
                ]
            }
        },
        {
            "type": "Task",
            "position": [
                1,
                1
            ],
            "generates": [],
            "width": 0,
            "height": 0,
            "pluginAttributes": {
                "taskType": "Clean",
                "duration": 5
            }
        },
        {
            "type": "Task",
            "position": [
                2,
                2
            ],
            "generates": [],
            "width": 0,
            "height": 0,
            "pluginAttributes": {
                "taskType": "Sell ticket",
                "duration": 2
            }
        }
    ]
}
````

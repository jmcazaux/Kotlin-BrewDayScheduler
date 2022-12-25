[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Kotlin-BrewDayScheduler&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Kotlin-BrewDayScheduler) 
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Kotlin-BrewDayScheduler&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=Kotlin-BrewDayScheduler) 
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Kotlin-BrewDayScheduler&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Kotlin-BrewDayScheduler) 
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Kotlin-BrewDayScheduler&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Kotlin-BrewDayScheduler)
----
# Kotlin-BrewDayScheduler

A Kotlin experimentation... (Generating home brewing schedule from a beer recipe)

## TL;DR

This is a toy project to play and experiment with Kotlin on the server side.

The end result will be a CLI application that will generate a brew day schedule from a beer recipe (for
[home brewing](https://learn.kegerator.com/how-to-brew-beer/)).

This is work in progress and this README will be updated as soon as something works.

----

## The context

As stated above, this is a small project to play a bit with Kotlin on the server side.  
I find Kotlin a really interesting and promising language.  
If I was not afraid of waking up some trolls I'd say that it is Java done right ;).

In order to pass behind the `Hello world` stage, I picked up one of my hobbies (home brewing) and tried to find a topic
around it.

The idea is to generate the schedule for a brew day from a beer recipe and a pre-defined brewing process.

I would like to generate the schedule as a nice PDF form, but that might end up as a CSV...

As each home brewer has its own process (depending on the actual brewing technique used, the equipment, the habits of
the home brewer, etc.) there would be a configuration step (where the home brewer defines their process).  
The configuration would have to be saved "somewhere" to be reused at each run.

---

## Building and using

_TBD_

---

## The project stages

### Deserializing beer recipes

Fortunately there is an XML format for beer recipes ([BeerXML](http://www.beerxml.com/beerxml.htm),
xsd [attached here](resources/BeerXML1.xsd)).

Deserialization is done via JacksonXML into Kotlin data classes.  
I did not find an automated way to generate the data classes from XSD (one that would work), so I did it manually from
the XSD (with the help of a few regex/replace).

The deserialization is [tested](src/test/kotlin/beerxml/RecipeTest.kt) from
a [set of BeerXML files](src/test/resources/recipes)
found online or from [LittleBock](https://www.littlebock.fr/), the home brewing assistant that I use.

### Picking up a CLI framework

This will be a CLI application so I had to pick up a CLI framework.

I looked into [clikt](https://github.com/ajalt/clikt) and [picocli](https://picocli.info/).

I chose `picocli` as it looked a bit more feature rich, it comes with the ability to compile as a standalone
application (using GraalVM), though the actual added value in this context might be slim.  
As well, it is initially a Java based library, and I was eager to test the interoperability of Kotlin and Java (which is
I think one of the big strength of Kotlin).

### Modeling the brewing process

The _brewing process_ defines the tasks and their relationships for a given brewer and equipment.  
In a nutshell, some tasks can only be started after the end of others, some tasks have to be done a given amount of time
before or after others, some tasks have to finish at the same time, etc.

Home brewers use different type of equipments that are leading to different processes.

The actual detailed process (allowing to calculate the schedule) can be built from the type of brewing technique and
equipment (brew-in-a-bag, 3 vessels, HERMS) and the parameters that define the given equipment of the home brewer
(heating efficiency, time to run _"n"_ liters of water when lautering, time to mill _"n"_ kg. of malt, etc.).

#### Modeling the process

- The brewing process is a graph of tasks.  
  Tasks are the vertices of the graph.  
  Precedence relationships between tasks are the edges of the graph.
- The [process object](src/main/kotlin/brewprocess/BrewProcess.kt) contains a list of
  [Tasks](src/main/kotlin/brewprocess/Task.kt).
- Each task contains a list of [DependantTask](src/main/kotlin/brewprocess/DependentTask.kt) (that defines both the
  linked task and the type of precedence).  
  _**Note:**_ this is actually a graph, not a tree. A task can be dependant of two tasks that need to complete before
  the later can start.

#### Serialization of the brewing process

As the processes will be stored for reuse at each run, it has to be serializable (as JSON).

Serializing an actual graph is not an obvious task.  
A graph is not a tree, so when serializing dependant tasks as "children" you might serialize the same subtree twice, and
it would be deserialized as two separate subtrees.

In order to make it possible to serialize the `BrewProcess`:

- Tasks and relationships (dependencies) are serialized separately.
- All tasks are serialized as a list (from the [BrewProcess](src/main/kotlin/brewprocess/BrewProcess.kt)).
- Tasks will **not** serialize their dependant tasks.
- Relationships (dependencies) between tasks are represented in a way they can be serialized
  (`DependencyRepresentation` in [DependantTask](src/main/kotlin/brewprocess/DependentTask.kt)
  i.e. not as a link between objects, but rather using task names).
- The `dependantTasks` in [Task](src/main/kotlin/brewprocess/Task.kt) property in task is therefore "hidden" from the
  serialization.
- A specific property (`dependencies`) has been added to [BrewProcess](src/main/kotlin/brewprocess/BrewProcess.kt) to
  set and return the `DependencyRepresentations` for the process.
- This property uses [delegation](https://kotlinlang.org/docs/delegated-properties.html) in order to recreate the actual
  task-to-task (in `task.dependentTasks`) relationships.

#### Configuring the pre-defined processes

The application will come with packaged templates to make it possible for the user to configure their own process.  
They will start from a template matching their process and will set specific configuration values.

In order to define the process templates, I have decided to create a
[DSL](https://kotlinlang.org/docs/type-safe-builders.html).  
This is totally unnecessary here but that will be fun and a nice exploration of some more advanced Kotlin concepts.

### Creating the brewing schedule

_TBD_

### Brewing schedule outpout

_TBD_

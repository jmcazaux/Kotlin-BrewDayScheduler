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
If I was not afraid fo waking up some trolls I'd say that it is Java done right ;).

In order to pass behind the `Hello world` stage, I picked up one of my hobbies (home brewing) and tried to find a topic
around it.

The idea is to generate the schedule for a brew day from a beer recipe and a pre-defined brewing process.

I would like to generate the schedule as a nice PDF form, but that might end up as a CSV...

---

## Building and using

_TBD_

---

## The project stages

### Deserializing beer recipes

Fortunately there is an XML format for beer recipes ([BeerXML](http://www.beerxml.com/beerxml.htm),
xsd [attached here](resources/BeerXML1.xsd)).

Deserialization is done via JacksonXML into Kotlin data classes.  
I did not find a a working automated way to generate the data classes from XSD so I did it manually from the XSD
(with the help of a few regex/replace).

The deserialization is [tested](src/test/kotlin/beerxml/RecipeTest.kt) from
a [set of BeerXML files](src/test/resources/recipes)
found online or from [LittleBock](https://www.littlebock.fr/), the home brewing assistant that I use.

### Picking up a CLI framework

This will be a CLI application so I had to pick up a CLI framework.

I looked into [clikt](https://github.com/ajalt/clikt) and [picocli](https://picocli.info/).

I chose `picocli` as it looked a bit more feature rich, it comes with the ability to compile as a standalone
application (above GraalVM), though the actual value in this context might be slim.  
As well, it is initially a Java based library, and I was eager to test the interoperability of Kotlin and Java (which is
I think one of the big strength of Kotlin).

### Modeling the brewing process

#### Modeling the process

_TBD_

#### Configuring the pre-defined process

_TBD_

### Creating the brewing schedule

_TBD_

### Brewing schedule outpout

_TBD_

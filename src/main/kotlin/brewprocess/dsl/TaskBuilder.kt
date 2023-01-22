package brewprocess.dsl

import brewprocess.*
import kotlin.reflect.KClass

sealed class TaskBuilder {
    // All possible variables
    var name: String? = null
    var description: String? = null
    var duration: Int? = null
    var heatingPower: Int? = null
    var chillingPower: Int? = null
    var litersPerMin: Double? = null
    var use: HeatWater.For? = null

    fun <T : Task> build(taskClass: KClass<T>): Task {
        return when (taskClass) {
            Mash::class -> Mash(name)

            Boil::class -> Boil(
                name = name,
                heatingPower = heatingPower ?: throw IllegalArgumentException("heatingPower must be defined")
            )

            Lauter::class -> Lauter(
                name = name,
                litersPerMin = litersPerMin ?: throw IllegalArgumentException("litersPerMin must be defined")
            )

            DrainMash::class -> DrainMash(
                name = name,
                duration = duration ?: throw IllegalArgumentException("duration must be defined")
            )

            SimpleAction::class -> SimpleAction(
                name = name ?: throw IllegalArgumentException("name must be defined"),
                description = description
            )

            HeatWater::class -> HeatWater(
                name = name ?: throw IllegalArgumentException("name must be defined"),
                use = use ?: throw IllegalArgumentException("use must be defined when heating water"),
                heatingPower = heatingPower ?: throw IllegalArgumentException("heatingPower must be defined")
            )

            Chill::class -> Chill(
                name = name,
                chillingPower = chillingPower ?: throw IllegalArgumentException("chillingPower must be defined")
            )

            else -> throw NotImplementedError("${taskClass.simpleName} not handled in builder yet")
        }
    }
}

class MashBuilder : TaskBuilder() {
    fun build(): Mash = super.build(Mash::class) as Mash
}

class BoilBuilder : TaskBuilder() {
    fun build(): Boil = super.build(Boil::class) as Boil
}

class LauterBuilder : TaskBuilder() {
    fun build(): Lauter = super.build(Lauter::class) as Lauter
}

class DrainMashBuilder : TaskBuilder() {
    fun build(): DrainMash = super.build(DrainMash::class) as DrainMash
}

class SimpleActionBuilder : TaskBuilder() {
    fun build(): SimpleAction = super.build(SimpleAction::class) as SimpleAction
}

class HeatWaterBuilder : TaskBuilder() {
    fun build(): HeatWater = super.build(HeatWater::class) as HeatWater
}

class ChillBuilder : TaskBuilder() {
    fun build(): Chill = super.build(Chill::class) as Chill
}

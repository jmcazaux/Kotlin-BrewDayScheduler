package brewprocess.dsl

import brewprocess.*
import kotlin.reflect.KClass


class TaskBuilder {
    // All possible variables
    var name: String? = null
    var description: String? = null
    var heatingPower: Int? = null
    var litersPerMin: Double? = null
    var use: HeatWater.For? = null


    fun <T : KClass<*>> build(taskClass: T): Task {
        return when (taskClass) {
            Mash::class -> Mash()
            Boil::class -> Boil(heatingPower ?: throw IllegalArgumentException("heatingPower must be defined"))
            Lauter::class -> Lauter(
                litersPerMin ?: throw IllegalArgumentException("litersPerMin must be defined")
            )
            SimpleAction::class -> SimpleAction(
                description ?: throw IllegalArgumentException("description must be defined")
            )
            HeatWater::class -> HeatWater(
                use ?: throw IllegalArgumentException("use must be defined when heating water"),
                heatingPower ?: throw IllegalArgumentException("heatingPower must be defined")
            )
            else -> throw NotImplementedError("${taskClass.simpleName} not handled in builder yet")
        }
    }
}

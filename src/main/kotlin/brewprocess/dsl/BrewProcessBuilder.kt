package brewprocess.dsl

import brewprocess.*
import brewprocess.HeatWater.For

class BrewProcessBuilder {
    var name: String? = null
    private val tasks: List<Task> = ArrayList()
    private val dependencies: List<DependencyRepresentation> = ArrayList()

    fun build(): BrewProcess {
        val brewProcess = BrewProcess(name ?: throw IllegalArgumentException("process name must be defined"))

        for (task in tasks) {
            brewProcess.addTask(task)
        }

        for (dependency in dependencies) {
            val from = brewProcess.tasks.getValue(dependency.fromTask)
            val to = brewProcess.tasks.getValue(dependency.toTask)

            from.addDependentTask(
                DependentTask(
                    to,
                    dependency.type,
                    dependency.delay
                )
            )
        }

        return brewProcess
    }
}

fun brewProcess(brewProcess: BrewProcessBuilder.() -> Unit): BrewProcess =
    BrewProcessBuilder().apply(brewProcess).build()

fun mash(mash: TaskBuilder.() -> Unit): Task =
    TaskBuilder().apply(mash).build(Mash::class)

fun boil(boil: TaskBuilder.() -> Unit): Task =
    TaskBuilder().apply(boil).build(Boil::class)

fun lauter(lauter: TaskBuilder.() -> Unit): Task =
    TaskBuilder().apply(lauter).build(Lauter::class)

fun action(action: TaskBuilder.() -> Unit): Task =
    TaskBuilder().apply(action).build(SimpleAction::class)

fun heatWater(heatWater: TaskBuilder.() -> Unit): Task =
    TaskBuilder().apply(heatWater).build(HeatWater::class)

val mash = mash {}
val boil = boil {
    heatingPower = 1000
}
val lauter = lauter {
    litersPerMin = 1.0
}
val action = action {
    description = "This is a simple action"
}
val heatWater = heatWater {
    use = For.MASH
    heatingPower = 1000
}


val threeVessels = brewProcess {
    name = "threeVessels"
//    tasks = [
//        task {
//
//        }
//    ]
}

package brewprocess.dsl

import brewprocess.*

class BrewProcessBuilder {
    var name: String? = null
    var description: String? = null
    var tasks: List<Task> = ArrayList()
    var dependencies: List<DependencyRepresentation> = ArrayList()

    fun build(): BrewProcess {
        val brewProcess = BrewProcess(
            name ?: throw IllegalArgumentException("process name must be defined"),
            description
        )

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
                    dependency.delay,
                    dependency.parametrizeDelay
                )
            )
        }

        return brewProcess
    }
}

fun brewProcess(brewProcess: BrewProcessBuilder.() -> Unit): BrewProcess =
    BrewProcessBuilder().apply(brewProcess).build()

class DependencyBuilder {
    lateinit var fromTask: String
    lateinit var toTask: String
    lateinit var type: DependencyType
    var delay: Int = 0
    var parametrizedDelay: Boolean = false

    fun build(): DependencyRepresentation {
        return DependencyRepresentation(
            fromTask = fromTask,
            toTask = toTask,
            type = type,
            delay = delay,
            parametrizeDelay = parametrizedDelay
        )
    }
}

fun mash(mash: MashBuilder.() -> Unit): Mash = MashBuilder().apply(mash).build()

fun boil(boil: TaskBuilder.() -> Unit): Boil = BoilBuilder().apply(boil).build()

fun lauter(lauter: TaskBuilder.() -> Unit): Lauter = LauterBuilder().apply(lauter).build()

fun action(action: TaskBuilder.() -> Unit): SimpleAction = SimpleActionBuilder().apply(action).build()

fun heatWater(heatWater: TaskBuilder.() -> Unit): HeatWater = HeatWaterBuilder().apply(heatWater).build()

fun chill(chill: TaskBuilder.() -> Unit): Chill = ChillBuilder().apply(chill).build()

fun dependency(dependency: DependencyBuilder.() -> Unit): DependencyRepresentation =
    DependencyBuilder().apply(dependency).build()

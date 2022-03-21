package brewprocess.dsl

import brewprocess.BrewProcess
import brewprocess.DependencyRepresentation
import brewprocess.DependentTask
import brewprocess.Task

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

val threeVessels = brewProcess {
    name = "threeVessels"
}

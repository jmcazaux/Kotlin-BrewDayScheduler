package brewprocess

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Model a brew process
 */
class BrewProcess(
    val name: String,
    val tasks: MutableMap<String, Task> = HashMap() // K= task.name, V = task
) {
    // Only used for serialization
    var dependencies: List<DependencyRepresentation> by DependenciesDelegate()

    /**
     * Add a task to the process
     * @param task: the task that will be added
     * @param relativeTo: the name of the task that will constrain the start of the new task
     * @param constraint: the type of constraint for starting the new task
     * @param delay: the delay associated with the constraint
     */
    fun addTask(
        task: Task,
        relativeTo: String? = null,
        constraint: DependencyType = DependencyType.STARTS_AFTER_END,
        delay: Int = 0,
        parametrizeDelay: Boolean = false

    ) {
        if (relativeTo != null) {
            val from = tasks.getValue(relativeTo)

            from.addDependentTask(
                DependentTask(
                    to = task,
                    type = constraint,
                    delay = delay,
                    parametrizeDelay = parametrizeDelay
                )
            )
        }
        tasks[task.name] = task
    }

    fun writeToFile(file: File) {
        val mapper = jacksonObjectMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)

        mapper.writeValue(file, this)
    }

    companion object {
        /**
         * Return a brew process from a file (where one has been serialized)
         */
        fun fromFile(file: File): BrewProcess {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(file.readText())
        }
    }
}

/**
 * Delegate class that handles serialization and deserialization of dependencies once tasks have been created
 */
private class DependenciesDelegate : ReadWriteProperty<BrewProcess, List<DependencyRepresentation>> {
    override fun getValue(
        thisRef: BrewProcess,
        property: KProperty<*>
    ): List<DependencyRepresentation> {
        val dependencies = ArrayList<DependencyRepresentation>()

        for (task in thisRef.tasks.values) {
            dependencies.addAll(task.getDependenciesRepresentations())
        }

        return dependencies
    }

    override fun setValue(
        thisRef: BrewProcess,
        property: KProperty<*>,
        value: List<DependencyRepresentation>
    ) {
        for (representation in value) {
            val from = thisRef.tasks.getValue(representation.fromTask)
            val to = thisRef.tasks.getValue(representation.toTask)

            from.addDependentTask(
                DependentTask(
                    to = to,
                    type = representation.type,
                    delay = representation.delay,
                    parametrizeDelay = representation.parametrizeDelay
                )
            )
        }
    }
}

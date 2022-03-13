package brewprocess

import com.fasterxml.jackson.annotation.JsonIgnore

enum class TaskType {
    HOP,
    HEAT_WATER,
    WAIT,
}

class Task(val name: String, val taskType: TaskType, var duration: Long = 0) {

    /* The below is not serialized as is
       As the BrewProcess is a graph we serialize vertices (tasks) and edges (relationships) as two lists
     */
    @JsonIgnore
    var dependentTasks: MutableList<DependentTask> = ArrayList()
        private set

    fun addDependentTask(dependentTask: DependentTask) {
        dependentTasks.add(dependentTask)
    }

    /**
     * Return the list of relationships to other tasks using a serializable representation
     * (instead of object-to-objects links)
     */
    @JsonIgnore
    fun getDependenciesRepresentations(): List<DependencyRepresentation> {
        val representations = ArrayList<DependencyRepresentation>()

        for (relationship in dependentTasks) {
            representations.add(
                DependencyRepresentation(
                    fromTask = this.name,
                    toTask = relationship.to.name,
                    type = relationship.type,
                    delay = relationship.delay
                )
            )
        }
        return representations
    }
}

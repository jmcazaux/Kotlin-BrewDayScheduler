package brewprocess

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    value = [
        Type(value = HeatWater::class, name = "heat_water"),
        Type(value = Mash::class, name = "mash"),
        Type(value = Lauter::class, name = "lauter"),
        Type(value = Boil::class, name = "boil"),
        Type(value = SimpleAction::class, name = "action"),
    ]
)
sealed class Task(val name: String) {

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

class Mash(name: String) : Task(name)

class Boil(name: String) : Task(name)

class Lauter(
    name: String,
    litersPerMin: Double = 1.0, // How fast you are lautering your mash
) : Task(name)

class SimpleAction(name: String, description: String) : Task(name)

class HeatWater(
    name: String,
    usage: For,
    heatingPower: Int, // Power available for the heat source
) : Task(name) {

    enum class For {
        MASH,
        SPARGE,
    }
}

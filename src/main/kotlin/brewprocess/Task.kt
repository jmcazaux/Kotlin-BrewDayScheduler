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
        Type(value = HeatWater::class, name = Task.HEAT_WATER),
        Type(value = Mash::class, name = Task.MASH),
        Type(value = Lauter::class, name = Task.LAUTER),
        Type(value = Boil::class, name = Task.BOIL),
        Type(value = SimpleAction::class, name = Task.ACTION),
    ]
)
sealed class Task(val name: String) {
    
    companion object {
        const val HEAT_WATER = "heat_water"
        const val MASH = "mash"
        const val LAUTER = "lauter"
        const val BOIL = "boil"
        const val ACTION = "action"
    }


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


class Mash : Task(MASH)


class Boil(
    heatingPower: Int = 1000, // Power available from the heat source
) : Task(BOIL)


class Lauter(
    litersPerMin: Double = 1.0, // How fast you are lautering your mash
) : Task(LAUTER)


class SimpleAction(description: String) : Task(ACTION)


class HeatWater(
    use: For = For.MASH,
    heatingPower: Int = 1000, // Power available from the heat source
) : Task(HEAT_WATER) {

    enum class For {
        MASH,
        SPARGE,
    }
}

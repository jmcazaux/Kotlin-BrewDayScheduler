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
        Type(value = DrainMash::class, name = Task.DRAIN_MASH),
        Type(value = Boil::class, name = Task.BOIL),
        Type(value = SimpleAction::class, name = Task.ACTION),
        Type(value = Chill::class, name = Task.CHILL)
    ]
)
sealed class Task(val name: String, var order: Int? = null) {

    companion object {
        const val HEAT_WATER = "heat_water"
        const val MASH = "mash"
        const val LAUTER = "lauter"
        const val DRAIN_MASH = "drain_mash"
        const val BOIL = "boil"
        const val ACTION = "action"
        const val CHILL = "chill"
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
                    delay = relationship.delay,
                    parametrizeDelay = relationship.parametrizeDelay
                )
            )
        }
        return representations
    }

    @JsonIgnore
    open fun getTaskParameters(): List<ProcessParameter<*>> {
        return this.dependentTasks.mapNotNull { parameterForDependantTask(it) }
    }

    private fun parameterForDependantTask(dependantTask: DependentTask): ProcessParameter<*>? {
        val name: String
        val prompt: String
        val description: String? = null

        when (dependantTask.type) {
            DependencyType.STARTS_AFTER_START -> {
                name = "delay to start \"${dependantTask.to.name.lowercase()}\""
                prompt =
                    "How long after the beginning of \"${this.name.lowercase()}\" will you start \"${dependantTask.to.name.lowercase()}\" (in mn)"
            }

            DependencyType.STARTS_AFTER_END -> {
                name = "delay to start \"${dependantTask.to.name.lowercase()}\""
                prompt =
                    "How long after the end of \"${this.name.lowercase()}\" will you start \"${dependantTask.to.name.lowercase()}\" (in mn)"
            }

            DependencyType.STARTS_BEFORE_END -> {
                name = "delay to start \"${dependantTask.to.name.lowercase()}\""
                prompt =
                    "How long before the end of \"${this.name.lowercase()}\" will you start \"${dependantTask.to.name.lowercase()}\" (in mn)"
            }

            DependencyType.FINISH_BEFORE_END -> {
                name = "delay to finish \"${dependantTask.to.name.lowercase()}\""
                prompt =
                    "How long before the end of \"${this.name.lowercase()}\" will you finish \"${dependantTask.to.name.lowercase()}\" (in mn)"
            }
        }

        return ProcessParameter(
            name = name,
            prompt = prompt,
            description = description,
            setter = dependantTask::setDelayInMin,
            current = dependantTask.delayInMin
        )
    }
}

class Mash(name: String? = MASH) : Task(name ?: MASH)

class Boil(
    name: String? = BOIL,
    var heatingPower: Int = 1000 // Power available from the heat source
) : Task(name ?: BOIL) {

    fun setHeatingPower(heatingPower: Int): Boolean {
        this.heatingPower = heatingPower
        return true
    }

    override fun getTaskParameters(): List<ProcessParameter<*>> {
        val parameters: MutableList<ProcessParameter<*>> = mutableListOf(
            ProcessParameter(
                name = "heating power for boil",
                prompt = "Define the actual heating power available during the boil operation",
                description = "The actual heating power (in Watts) that is actually transmitted to the wort during boil.\n" +
                    "This is not the power of the heating source as it should account for efficiency.\n" +
                    "When heating 'l' litters of water from 'T1'° to 'T2'° in 't' seconds, the actual heating power " +
                    "is calculated as:\n  (4185 x (T2 - T1) x l) / t.\n" +
                    "Must be greater than 0 (no decimals).",
                setter = this::setHeatingPower,
                current = this.heatingPower
            )
        )

        parameters.addAll(super.getTaskParameters())
        return parameters
    }
}

class Lauter(
    name: String? = LAUTER,
    var litersPerMin: Double = 1.0 // How fast you lauter your mash
) : Task(name ?: LAUTER) {

    fun setLitersPerMin(litersPerMin: Double): Boolean {
        this.litersPerMin = litersPerMin
        return true
    }

    override fun getTaskParameters(): List<ProcessParameter<*>> {
        val parameters: MutableList<ProcessParameter<*>> = mutableListOf(
            ProcessParameter(
                type = ProcessParameterType.DOUBLE,
                name = "lautering throughput",
                prompt = "Define the throughput when lautering",
                description = "This will allow to calculate the time you need to lauter your mash.\n" +
                    "It will be derived from the water throughput expressed as liters per minute.\n" +
                    "Must be greater than 0 (decimals allowed).",
                setter = this::setLitersPerMin,
                current = this.litersPerMin
            )
        )

        parameters.addAll(super.getTaskParameters())
        return parameters
    }
}

class DrainMash(
    name: String? = DRAIN_MASH,
    var duration: Int = 30 // When using BIAB or other single vessel process, the time it takes to drain the mash
) : Task(name ?: LAUTER) {

    fun setDuration(duration: Int): Boolean {
        this.duration = duration
        return true
    }

    override fun getTaskParameters(): List<ProcessParameter<*>> {
        val parameters: MutableList<ProcessParameter<*>> = mutableListOf(
            ProcessParameter(
                type = ProcessParameterType.INT,
                name = "duration of the mash drain",
                prompt = "Duration to drain the mash (in minutes)",
                description = "When using BIAB or other single vessel process, this will define the time you will " +
                    "time it takes to drain the mash before the boil stage.\n" +
                    "Must be greater than 0 (in minutes, no decimals).",
                setter = this::setDuration,
                current = this.duration
            )
        )

        parameters.addAll(super.getTaskParameters())
        return parameters
    }
}

class SimpleAction(name: String, val description: String? = null) : Task(name)

class HeatWater(
    name: String,
    val use: For = For.MASH,
    var heatingPower: Int = 2000 // Power available from the heat source
) : Task(name) {

    enum class For {
        MASH,
        SPARGE
    }

    fun setHeatingPower(heatingPower: Int): Boolean {
        this.heatingPower = heatingPower
        return true
    }

    override fun getTaskParameters(): List<ProcessParameter<*>> {
        val parameters: MutableList<ProcessParameter<*>> = mutableListOf(
            ProcessParameter(
                name = "power when heating water for ${use.name.lowercase()}",
                prompt = "Define the actual heating power available when heating water for ${use.name.lowercase()}",
                description = "The actual heating power (in Watts) that is actually transmitted to the water when heating (for mash or for sparge).\n" +
                    "This is not the power of the heating source as it should account for efficiency.\n" +
                    "When heating 'l' litters of water from 'T1'° to 'T2'° in 't' seconds, the actual heating power " +
                    "is calculated as:\n  (4185 x (T2 - T1) x l) / t.\n" +
                    "Must be greater than 0 (no decimals).",
                setter = this::setHeatingPower,
                current = this.heatingPower
            )
        )

        parameters.addAll(super.getTaskParameters())
        return parameters
    }
}

class Chill(
    name: String? = null,
    var chillingPower: Int = 500
) : Task(name = name ?: CHILL) {

    fun setChillingPower(chillingPower: Int): Boolean {
        this.chillingPower = chillingPower
        return true
    }

    override fun getTaskParameters(): List<ProcessParameter<*>> {
        val parameters: MutableList<ProcessParameter<*>> = mutableListOf(
            ProcessParameter(
                name = "chilling capability",
                prompt = "Define the actual chilling capability",
                description = "The actual chilling capability (in Watts) that is actually available to cool the wort after boil.\n" +
                    "This can be calculated with the method below.\n" +
                    "When cooling 'l' litters of water from 'T1'° down to 'T2'° in 't' seconds, the cooling capability is calculated as:\n" +
                    "  (4185 x (T2 - T1) x l) / t.\n" +
                    "Must be greater than 0 (no decimals).",
                setter = this::setChillingPower,
                current = this.chillingPower
            )
        )

        parameters.addAll(super.getTaskParameters())
        return parameters
    }
}

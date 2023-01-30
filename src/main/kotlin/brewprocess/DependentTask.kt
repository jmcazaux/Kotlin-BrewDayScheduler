package brewprocess

enum class DependencyType {
    STARTS_AFTER_START,
    STARTS_AFTER_END,
    STARTS_BEFORE_END,
    FINISH_BEFORE_END
}

class DependentTask(
    val to: Task,
    val type: DependencyType,
    var delay: Int,
    val parametrizeDelay: Boolean = false
) {

    val delayInMin: Int
        get() = delay / 60

    fun setDelayInMin(delayInMin: Int): Boolean {
        this.delay = delayInMin * 60
        return true
    }
}

/**
 * External representation of task dependencies.
 * Used to store relationships in JSON (etc.) aside from tasks
 */
class DependencyRepresentation(
    val fromTask: String,
    val toTask: String,
    val type: DependencyType,
    val delay: Int,
    val parametrizeDelay: Boolean = false
)

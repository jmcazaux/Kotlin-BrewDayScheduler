package brewprocess

enum class DependencyType {
    STARTS_AFTER_START,
    STARTS_AFTER_END,
    FINISH_BEFORE_END
}

class DependentTask(
    val to: Task,
    val type: DependencyType,
    val delay: Int,
    val parametrizeDelay: Boolean = false
)

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

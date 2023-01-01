package brewprocess

enum class ProcessParameterType(val type: Class<*>) {
    INT(Int::class.java),
    DOUBLE(Double::class.java)
}

data class ProcessParameter<T>(
    val name: String,
    val type: ProcessParameterType = ProcessParameterType.INT,
    val prompt: String,
    val description: String?,
    val setter: (T) -> Boolean,
    val current: T?
)

package brewprocess

import kotlin.reflect.KClass

enum class ProcessParameterType(val type: KClass<*>) {
    INT(Int::class),
    DOUBLE(Double::class)
}

data class ProcessParameter<T>(
    val name: String,
    val type: ProcessParameterType = ProcessParameterType.INT,
    val prompt: String,
    val description: String?,
    val unit: String? = null,
    val setter: (T) -> Boolean,
    val current: T?,
    val min: T? = null,
    val max: T? = null
)

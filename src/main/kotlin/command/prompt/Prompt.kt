package command.prompt

import kotlin.reflect.KClass

class Prompt<T : Any>(
    private val valueName: String, // What this prompt collects
    private val valueType: KClass<T>, // Type of the input we will collect
    private val question: String, // The question we will ask the user
    private val help: String? = null, // A help context that can be displayed to the user
    private val default: T, // Default value
    private val min: T? = null, // Min value that shall be captured
    private val max: T? = null // Max value that shall be captured
) {

    init {
        if (!ALLOWED_TYPES.contains(valueType)) {
            throw IllegalArgumentException(
                "Only ${ALLOWED_TYPES.joinToString()} " +
                    "are allowed as Prompt.valueType."
            )
        }
    }

    private companion object {
        private val ALLOWED_TYPES: List<KClass<*>> = listOf<KClass<*>>(String::class, Int::class, Double::class)
    }

    fun prompt(): T? {
        TODO("Not implemented")
    }
}

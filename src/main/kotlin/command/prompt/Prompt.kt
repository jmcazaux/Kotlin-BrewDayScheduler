package command.prompt

import kotlin.reflect.KClass

class Prompt<T : Comparable<T>>(
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
                "Only ${ALLOWED_TYPES.joinToString()} " + "are allowed as Prompt.valueType."
            )
        }
    }

    private companion object {
        private val ALLOWED_TYPES: List<KClass<*>> =
            listOf<KClass<*>>(String::class, Int::class, Double::class, Boolean::class)
    }

    fun prompt(): T? {
        var value: T? = null

        while (value == null) {
            print(buildFullPrompt())

            val input = readln()

            if (input.isEmpty()) {
                return default
            }

            if (input == "?") {
                println(help)
                continue
            }

            value = extractValue(input)

            if (value == null) {
                println(buildInvalidValueMessage(input))
                continue
            }

            val valueIsBelowMin = (min != null) && (value < min)
            val valueIsAboveMax = (max != null) && (value > max)
            if (valueIsBelowMin || valueIsAboveMax) {
                println(buildOutOfBoundariesMessage(input))
                value = null
                continue
            }
        }

        return value
    }

    private fun buildFullPrompt(): String {
        var fullPrompt = "$question [" + "$default".bold + "]"

        if (help != null) {
            fullPrompt += " ('${"?".italic}' for help)"
        }

        fullPrompt += ": "

        return fullPrompt
    }

    private fun buildInvalidValueMessage(input: String): String {
        return "Please provide a '${valueType.simpleName?.lowercase()}'. '$input' is not a valid value."
    }

    private fun buildOutOfBoundariesMessage(value: String): String {
        return when {
            min != null && max != null -> "Value for $valueName must be between '$min' and '$max' (unlike '$value')."
            min != null -> "Value for $valueName must be greater than '$min' (unlike '$value')."
            else -> "Value for $valueName must be smaller than '$max' (unlike '$value')." // max != null
        }
    }

    private fun extractValue(input: String): T? {
        when (valueType) {
            String::class -> return input as T

            Int::class ->
                return try {
                    input.toInt() as T
                } catch (e: Exception) {
                    null
                }

            Double::class ->
                return try {
                    input.toDouble() as T
                } catch (e: Exception) {
                    null
                }

            Boolean::class -> {
                when {
                    listOf("y", "yes", "true").contains(input.lowercase()) -> return true as T
                    listOf("n", "no", "false").contains(input.lowercase()) -> return false as T
                    else -> return null
                }
            }
        }

        return null
    }

    /*
     Adding a bit of helpers for formatting the output in the ANSI terminal
     */

    enum class Style(val code: String) {
        BOLD("\u001B[1m"),
        ITALIC("\u001B[3m"),
        UNDERLINE("\u001B[4m"),
        RESET("\u001b[0m")
    }

    private fun String.applyAnsiStyle(style: Style): String {
        return style.code + this + Style.RESET.code
    }

    private val String.bold: String
        get() = this.applyAnsiStyle(Style.BOLD)

    private val String.italic: String
        get() = this.applyAnsiStyle(Style.ITALIC)

    private val String.underline: String
        get() = this.applyAnsiStyle(Style.UNDERLINE)
}

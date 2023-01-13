package command.prompt

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test

internal class PromptTest {

    @Test
    fun initWithUnsupportedTypeThrows() {
        assertThrows<IllegalArgumentException> {
            Prompt(
                valueType = Byte::class,
                valueName = "ValueName",
                question = "QuestionToTheUser",
                default = 4
            )
        }
    }

    @Test
    fun initWithSupportedTypeDoesNotThrow() {
        assertDoesNotThrow {
            Prompt(
                valueType = Int::class,
                valueName = "ValueName",
                question = "QuestionToTheUser",
                default = 4
            )

            Prompt(
                valueType = Double::class,
                valueName = "ValueName",
                question = "QuestionToTheUser",
                default = 4.0
            )

            Prompt(
                valueType = String::class,
                valueName = "ValueName",
                question = "QuestionToTheUser",
                default = "string"
            )
        }
    }
}

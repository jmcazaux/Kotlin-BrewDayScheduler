package command.prompt

import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized
import com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

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

            Prompt(
                valueType = Boolean::class,
                valueName = "ValueName",
                question = "QuestionToTheUser",
                default = true
            )
        }
    }

    @Test
    fun promptAsksExpectedQuestionWhenNoHelp() {
        val intPrompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4
        )

        var expectedQuestion = "QuestionToTheUser [" + Prompt.Style.BOLD.code + "4" + Prompt.Style.RESET.code + "]: "
        assertPromptOutput(intPrompt, expectedOutput = expectedQuestion, inputs = arrayOf("4"))

        val doublePrompt = Prompt(
            valueType = Double::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4.0
        )

        expectedQuestion = "QuestionToTheUser [" + Prompt.Style.BOLD.code + "4.0" + Prompt.Style.RESET.code + "]: "
        assertPromptOutput(doublePrompt, expectedOutput = expectedQuestion, inputs = arrayOf("5.0"))

        val stringPrompt = Prompt(
            valueType = String::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = "default"
        )

        expectedQuestion = "QuestionToTheUser [" + Prompt.Style.BOLD.code + "default" + Prompt.Style.RESET.code + "]: "
        assertPromptOutput(stringPrompt, expectedOutput = expectedQuestion, inputs = arrayOf("a string"))

        val booleanPrompt = Prompt(
            valueType = Boolean::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = true
        )

        expectedQuestion = "QuestionToTheUser [" + Prompt.Style.BOLD.code + "true" + Prompt.Style.RESET.code + "]: "
        assertPromptOutput(booleanPrompt, expectedOutput = expectedQuestion, inputs = arrayOf("yes"))
    }

    @Test
    fun promptAsksExpectedQuestionWithHelp() {
        val prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            help = "SomeHelp",
            default = 4
        )

        val expectedQuestion =
            "QuestionToTheUser [" + Prompt.Style.BOLD.code + "4" + Prompt.Style.RESET.code + "] " +
                "('" + Prompt.Style.ITALIC.code + "?" + Prompt.Style.RESET.code + "' for help): "
        assertPromptOutput(prompt, expectedOutput = expectedQuestion, inputs = arrayOf("4"))
    }

    @Test
    fun promptWithHelpDisplayHelpOnQuestionMark() {
        val prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            help = "SomeHelp",
            default = 4
        )

        val expectedQuestion =
            "QuestionToTheUser [" + Prompt.Style.BOLD.code + "4" + Prompt.Style.RESET.code + "] " +
                "('" + Prompt.Style.ITALIC.code + "?" + Prompt.Style.RESET.code + "' for help): "

        val expectedOutput =
            expectedQuestion + // The initial question
                "SomeHelp" + // The help displayed after the '?' input
                "\n" +
                expectedQuestion // We display the question again after help

        assertPromptOutput(
            prompt,
            expectedOutput,
            inputs = arrayOf("?", "4")
        )
    }

    @Test
    fun promptHandlesInvalidValueAndDisplayAppropriateMessage() {
        val prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4
        )

        val expectedQuestion =
            "QuestionToTheUser [" + Prompt.Style.BOLD.code + "4" + Prompt.Style.RESET.code + "]: "

        val expectedOutput =
            expectedQuestion + // The initial question
                // The message we display as 'abc' is indeed not an int
                "Please provide a 'int'. 'abc' is not a valid value." +
                "\n" +
                expectedQuestion // We display the question again after displaying the invalid value message

        assertPromptOutput(
            prompt,
            expectedOutput,
            inputs = arrayOf("abc", "4")
        )
    }

    @Test
    fun promptHandlesValueAboveMaxAndDisplayAppropriateMessage() {
        var prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4,
            max = 10
        )

        val expectedQuestion =
            "QuestionToTheUser [" + Prompt.Style.BOLD.code + "4" + Prompt.Style.RESET.code + "]: "

        var expectedOutput =
            expectedQuestion + // The initial question
                // The message we display when only max is provided
                "Value for ValueName must be smaller than '10' (unlike '12')." +
                "\n" +
                expectedQuestion // We display the question again

        assertPromptOutput(
            prompt,
            expectedOutput,
            inputs = arrayOf("12", "4")
        )

        // The message with only max or max & min is different
        prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4,
            min = 2,
            max = 10
        )

        expectedOutput =
            expectedQuestion + // The initial question
            // The message we display when  both min and max are provided
            "Value for ValueName must be between '2' and '10' (unlike '12')." +
            "\n" +
            expectedQuestion // We display the question again

        assertPromptOutput(
            prompt,
            expectedOutput,
            inputs = arrayOf("12", "4")
        )
    }

    @Test
    fun promptHandlesValueBelowMinAndDisplayAppropriateMessage() {
        var prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4,
            min = 2
        )

        val expectedQuestion =
            "QuestionToTheUser [" + Prompt.Style.BOLD.code + "4" + Prompt.Style.RESET.code + "]: "

        var expectedOutput =
            expectedQuestion + // The initial question
                // The message we display when there only min is provided
                "Value for ValueName must be greater than '2' (unlike '1')." +
                "\n" +
                expectedQuestion // We display the question again

        assertPromptOutput(
            prompt,
            expectedOutput,
            inputs = arrayOf("1", "4")
        )

        // The message with only max or max & min is different
        prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4,
            min = 2,
            max = 10
        )

        expectedOutput =
            expectedQuestion + // The initial question
            // The message we display when both min and max are provided
            "Value for ValueName must be between '2' and '10' (unlike '1')." +
            "\n" +
            expectedQuestion // We display the question again

        assertPromptOutput(
            prompt,
            expectedOutput,
            inputs = arrayOf("1", "4")
        )
    }

    @Test
    fun promptReturnsValueEnteredByTheUser() {
        val prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4
        )

        withTextFromSystemIn("16").execute {
            assertEquals(16, prompt.prompt(), "Prompt<Int> should return 16 when 16 is entered on the command line")
        }
    }

    @Test
    fun promptReturnsDefaultWhenNothingEnteredByTheUser() {
        val prompt = Prompt(
            valueType = Int::class,
            valueName = "ValueName",
            question = "QuestionToTheUser",
            default = 4
        )

        withTextFromSystemIn("").execute {
            assertEquals(
                4,
                prompt.prompt(),
                "Prompt<Int> should return the default value when nothing is entered on the command line"
            )
        }
    }

    private fun assertPromptOutput(prompt: Prompt<*>, expectedOutput: String, inputs: Array<String>) {
        withTextFromSystemIn(*inputs).execute {
            var question = tapSystemOutNormalized {
                prompt.prompt()
            }

            assertEquals(
                expectedOutput,
                question
            )
        }
    }
}

package brewprocess

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class TaskTest {

    @ParameterizedTest(name = "{index}: task \"{0}\" should have parameter -> {2}")
    @MethodSource("argumentsForTaskParametersTests")
    fun testTasksHaveCorrectParameters(
        taskName: String,
        task: Task,
        shouldHaveParameter: Boolean,
        expectedPrompt: String?
    ) {
        if (!shouldHaveParameter) {
            Assertions.assertEquals(
                0,
                task.getTaskParameters().size,
                "Task ${task.name} should not have parameters (found ${task.getTaskParameters().size})"
            )
            return
        }

        Assertions.assertTrue(
            task.getTaskParameters()[0].prompt.startsWith(expectedPrompt ?: return),
            "Prompt for task ${task.name} should start with \"$expectedPrompt\""
        )
    }

    companion object {
        @JvmStatic
        fun argumentsForTaskParametersTests(): Stream<Arguments> {
            return Stream.of(
                arguments("mash", Mash(), false, null),
                arguments("boil", Boil(), true, "Define the actual heating power available during the boil operation"),
                arguments("lauter", Lauter(), true, "Define the throughput when lautering"),
                arguments(
                    "heat water for sparge",
                    HeatWater(name = "xxxx", use = HeatWater.For.SPARGE),
                    true,
                    "Define the actual heating power available when heating water for sparge"
                ),
                arguments(
                    "heat water for mash",
                    HeatWater(name = "xxxx", use = HeatWater.For.MASH),
                    true,
                    "Define the actual heating power available when heating water for mash"
                ),
                arguments(
                    "chill",
                    Chill(),
                    true,
                    "Define the actual chilling capability"
                ),
                arguments("simple action", SimpleAction(name = "a_simple_action"), false, null),
                arguments("drain the mash", DrainMash(), true, "Duration to drain the mash (in minutes)")
            )
        }
    }
}

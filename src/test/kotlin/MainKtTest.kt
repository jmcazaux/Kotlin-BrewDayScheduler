import TestHelper.Companion.TEST_EMPTY_DIRECTORY
import TestHelper.Companion.TEST_HOME_DIRECTORY
import com.github.stefanbirkner.systemlambda.SystemLambda
import com.github.stefanbirkner.systemlambda.SystemLambda.restoreSystemProperties
import com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn
import command.config.ConfigCommand
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import picocli.CommandLine

internal class MainKtTest {

    @BeforeEach
    internal fun setUp() {
        TestHelper.resetTestEmptyDirectory()
    }

    @Test
    fun runWithAConfigShouldNotAskForConfig() {
        restoreSystemProperties {
            System.setProperty("user.home", TEST_HOME_DIRECTORY.absolutePath)

            withTextFromSystemIn("n").execute {
                val question = SystemLambda.tapSystemOutNormalized {
                    main(args = emptyArray())
                }

                assertNotNull(question, "bds always produces an output")
                assertTrue(question.startsWith("Brew Day Scheduler: all systems going..."))
            }
        }
    }

    @Test
    fun runWithoutAConfigShouldTriggerConfigSetupWhenUserAgrees() {
        val configCommand = ConfigCommand(TEST_EMPTY_DIRECTORY)
        val spiedCommand = spy(configCommand)
        doReturn(CommandLine.ExitCode.OK).`when`(spiedCommand).setUpAndSaveConfig()

        val app = BdsApp(arrayOf(spiedCommand))

        restoreSystemProperties {
            System.setProperty("user.home", TEST_EMPTY_DIRECTORY.absolutePath)

            val userInputs = mutableListOf("")
            userInputs.addAll((0..30).map { "" })
            val inputs = userInputs.toTypedArray()

            withTextFromSystemIn(*inputs).execute {
                val question = SystemLambda.tapSystemOutNormalized {
                    app.run(args = emptyArray())
                }

                assertNotNull(question, "bds always produces an output")
                Mockito.verify(spiedCommand).setUpAndSaveConfig()
            }
        }
    }

    @Test
    fun runWithoutAConfigShouldAskForConfig() {
        restoreSystemProperties {
            System.setProperty("user.home", TEST_EMPTY_DIRECTORY.absolutePath)

            // The below is a hack to go threw the configuration ('enter' for each question)

            withTextFromSystemIn("n").execute {
                val question = SystemLambda.tapSystemOutNormalized {
                    main(args = emptyArray())
                }

                assertNotNull(question, "Should ask to set up the configuration when there is none")
                assertTrue(question.startsWith("Would you like to define your brew process now?"))
            }
        }
    }
}

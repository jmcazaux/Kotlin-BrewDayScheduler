import TestHelper.Companion.TEST_EMPTY_DIRECTORY
import TestHelper.Companion.TEST_HOME_DIRECTORY
import brewprocess.BrewProcess
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.stefanbirkner.systemlambda.SystemLambda
import com.github.stefanbirkner.systemlambda.SystemLambda.restoreSystemProperties
import com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn
import command.config.ConfigCommand
import command.config.DefaultProcesses
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import picocli.CommandLine
import kotlin.test.assertEquals

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

        withTextFromSystemIn("").execute {
            val app = BdsApp(arrayOf(spiedCommand))
            val question = SystemLambda.tapSystemOutNormalized {
                app.run(args = emptyArray())
            }

            assertNotNull(question, "bds always produces an output")
            Mockito.verify(spiedCommand).setUpAndSaveConfig()
        }
    }

    @Test
    fun runWithoutAConfigShouldTriggerConfigSetupWhenUserAgreesAndSaveConfig() {
        val configCommand = ConfigCommand(TEST_EMPTY_DIRECTORY)
        assertTrue(TEST_EMPTY_DIRECTORY.exists(), "Test empty directory should exists")
        assertTrue(TEST_EMPTY_DIRECTORY.list().isEmpty(), "Test empty directory should be empty")

        // The below is a hack to generate enough "default" user inputs to go threw the full config set up.
        val inputs = Array(100) { "" }
        withTextFromSystemIn(*inputs).execute {
            BdsApp(arrayOf(configCommand))
        }

        // We should have created a config file
        assertEquals(TEST_EMPTY_DIRECTORY.list().size, 1, "We shall have created one file in the config directory")
        val configFile = TEST_EMPTY_DIRECTORY.listFiles()[0]
        assertEquals(configFile.name, "config", "The generated config file shall be named \"config\"")
        assertTrue(configFile.isFile, "The generated config file shall be a file")

        // The config file shall contain the config for the default process
        val mapper = jacksonObjectMapper()
        val config = mapper.readValue<JsonNode>(configFile.readText())
        val process = mapper.treeToValue(config["process"], BrewProcess::class.java)

        assertEquals(
            process.name,
            DefaultProcesses.defaultProcess.name,
            "The configured process should be the default process"
        )
    }

    @Test
    fun runWithoutAConfigShouldAskForConfig() {
        restoreSystemProperties {
            System.setProperty("user.home", TEST_EMPTY_DIRECTORY.absolutePath)

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

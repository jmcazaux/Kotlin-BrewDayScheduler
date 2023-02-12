package command.config

import BdsApp
import TestHelper
import brewprocess.BrewProcess
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.stefanbirkner.systemlambda.SystemLambda
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.spy
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class ConfigCommandTest {

    @BeforeEach
    internal fun setUp() {
        TestHelper.resetTestEmptyDirectory()
    }

    @Test
    fun initWithFileIsNotDefaultConfig() {
        ConfigCommand(TestHelper.TEST_BDS_DIRECTORY)

        Assertions.assertFalse(AppConfig.isDefault, "AppConfig with existing config file should not be default")

        Assertions.assertEquals("test", AppConfig.process.name)
        Assertions.assertNotEquals(DefaultProcesses.THREE_VESSELS, AppConfig.process)
    }

    @Test
    fun initWithoutFileIsDefaultConfig() {
        ConfigCommand(TestHelper.TEST_EMPTY_DIRECTORY)
        Assertions.assertTrue(AppConfig.isDefault, "AppConfig with non existing config file should be default")

        Assertions.assertEquals(DefaultProcesses.THREE_VESSELS, AppConfig.process)
    }

    @Test
    fun setUpAndSaveConfigShouldGenerateAValidConfigFile() {
        Assertions.assertTrue(TestHelper.TEST_EMPTY_DIRECTORY.exists(), "Test empty directory should exists")
        Assertions.assertTrue(TestHelper.TEST_EMPTY_DIRECTORY.list()?.isEmpty() ?: false, "Test empty directory should be empty")

        val configCommand = ConfigCommand(TestHelper.TEST_EMPTY_DIRECTORY)
        val spiedCommand = spy(configCommand)

        // The below is a hack to generate enough "default" user inputs to go through the full config set up.
        val inputs = Array(100) { "" }
        SystemLambda.withTextFromSystemIn(*inputs).execute {
            BdsApp(arrayOf(spiedCommand))
        }

        // We should have called setUpAndSaveConfig
        Mockito.verify(spiedCommand).setUpAndSaveConfig()

        // We should have created a valid config file
        assertEquals(
            TestHelper.TEST_EMPTY_DIRECTORY.list()?.size ?: 0,
            1,
            "We shall have created one file in the config directory"
        )
        val configFile = TestHelper.TEST_EMPTY_DIRECTORY.listFiles()?.get(0)
        assertNotNull(configFile, "A config file should exists")
        assertEquals(configFile.name, "config", "The generated config file shall be named \"config\"")
        Assertions.assertTrue(configFile.isFile, "The generated config file shall be a file")

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
}

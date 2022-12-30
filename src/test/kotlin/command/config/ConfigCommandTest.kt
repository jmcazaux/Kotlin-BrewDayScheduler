package command.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

internal class ConfigCommandTest {

    @Test
    fun initWithFile() {
        val sampleFile = File(this.javaClass.classLoader.getResource("sample_config.json").file)
        ConfigCommand(sampleFile)

        Assertions.assertFalse(AppConfig.isDefault, "AppConfig with existing config file should not be default")

        Assertions.assertEquals("test", AppConfig.process.name)
        Assertions.assertNotEquals(DefaultProcesses.THREE_VESSELS, AppConfig.process)
    }

    @Test
    fun initWithoutFile() {
        val nonExistingFile = File("that_does_not_exists")
        Assertions.assertFalse(
            nonExistingFile.exists(),
            "Needs a non existing file to test ConfigCommand.init without a file"
        )

        ConfigCommand(nonExistingFile)
        Assertions.assertTrue(AppConfig.isDefault, "AppConfig with non existing config file should be default")

        Assertions.assertEquals(DefaultProcesses.THREE_VESSELS, AppConfig.process)
    }
}

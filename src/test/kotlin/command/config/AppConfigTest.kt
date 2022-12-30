package command.config

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.File

internal class AppConfigTest {

    @Test
    fun appConfigReadBeforeInitThrows() {
        assertThrows<IllegalStateException> {
            AppConfig.process
        }

        assertThrows<IllegalStateException> {
            AppConfig.isDefault
        }
    }

    @Test
    fun appConfigReadAfterInit() {
        val sampleFile = File(this.javaClass.classLoader.getResource("sample_config.json").file)
        ConfigCommand(sampleFile)

        assertDoesNotThrow {
            AppConfig.process
            AppConfig.isDefault
        }
    }
}

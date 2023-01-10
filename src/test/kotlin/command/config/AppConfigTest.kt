package command.config

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.File

internal class AppConfigTest {

    @Test
    fun readAppConfigBeforeInitThrows() {
        assertThrows<IllegalStateException> {
            AppConfig.process
        }

        assertThrows<IllegalStateException> {
            AppConfig.isDefault
        }
    }

    @Test
    fun readAppConfigAfterInitDoesNotThrow() {
        val sampleFile = File(this.javaClass.classLoader.getResource("sample_config.json").file)
        ConfigCommand(sampleFile)

        assertDoesNotThrow {
            AppConfig.process
            AppConfig.isDefault
        }
    }
}

package command.config

import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import java.io.File

@TestMethodOrder(OrderAnnotation::class)
internal class AppConfigTest {

    @Test
    @Order(1)
    fun readAppConfigBeforeInitThrows() {
        assertThrows<IllegalStateException> {
            AppConfig.process
        }

        assertThrows<IllegalStateException> {
            AppConfig.isDefault
        }
    }

    @Test
    @Order(2)
    fun readAppConfigAfterInitDoesNotThrow() {
        val sampleFile = File(this.javaClass.classLoader.getResource("sample_config.json").file)
        ConfigCommand(sampleFile)

        assertDoesNotThrow {
            AppConfig.process
            AppConfig.isDefault
        }
    }
}

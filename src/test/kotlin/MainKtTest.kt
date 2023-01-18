import com.github.stefanbirkner.systemlambda.SystemLambda
import com.github.stefanbirkner.systemlambda.SystemLambda.restoreSystemProperties
import com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class MainKtTest {

    @Test
    fun runWithoutAConfigShouldAskForConfig() {
        val testHomeDirectory = File(this.javaClass.classLoader.getResource("test_home_directory").file)

        restoreSystemProperties {
            System.setProperty("user.home", testHomeDirectory.absolutePath)
            withTextFromSystemIn("n").execute {
                var question = SystemLambda.tapSystemOutNormalized {
                    main(args = emptyArray())
                }

                assertNotNull(question != null, "bds always produces an output")
                assertTrue(question.startsWith("Brew Day Scheduler: all systems going..."))
            }
        }
    }

    @Test
    fun runWithAConfigShouldNotAskForConfig() {
        val testHomeDirectory = File(this.javaClass.classLoader.getResource("test_home_directory").file)
        restoreSystemProperties {
            System.setProperty("user.home", testHomeDirectory.absolutePath + "/non_existent_directory")
            withTextFromSystemIn("n").execute {
                var question = SystemLambda.tapSystemOutNormalized {
                    main(args = emptyArray())
                }

                assertNotNull(question != null, "Should ask to set up the configuration when there is none")
                assertTrue(question.startsWith("Would you like to define your brew process now?"))
            }
        }
    }
}

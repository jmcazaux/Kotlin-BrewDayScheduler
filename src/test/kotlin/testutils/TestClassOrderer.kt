package testutils

import org.junit.jupiter.api.ClassOrderer
import org.junit.jupiter.api.ClassOrdererContext
import java.util.Collections

class TestClassOrderer : ClassOrderer {
    /**
     We use a custom test orderer as AppConfigTest needs to always run first
     (as it tests access to AppConfig before any initialization of the ConfigCommand class)
     */

    override fun orderClasses(context: ClassOrdererContext?) {
        val testClasses = context?.classDescriptors ?: emptyList()

        val appConfigTestClass = testClasses.find {
            it.displayName == "AppConfigTest"
        }
        if (appConfigTestClass != null) {
            Collections.swap(testClasses, 0, testClasses.indexOf(appConfigTestClass))
        }
    }
}

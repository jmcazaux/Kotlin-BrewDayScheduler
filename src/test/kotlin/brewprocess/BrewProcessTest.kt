package brewprocess

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

internal class BrewProcessTest {

    @Test
    fun fromFile() {
        val testFile = File(this.javaClass.classLoader.getResource("test_process.json").file)

        val process = BrewProcess.fromFile(testFile)

        Assertions.assertEquals(4, process.tasks.size)

        val mash = process.tasks["mash"]
        val lauter = process.tasks["lauter"]
        val boil = process.tasks["boil"]
        val chill = process.tasks["chill"]

        Assertions.assertEquals(1, mash?.dependentTasks?.size ?: -1, "\"mash\" should have one dependent task")
        Assertions.assertEquals(1, lauter?.dependentTasks?.size ?: -1, "\"lauter\" should have one dependent task")
        Assertions.assertEquals(1, boil?.dependentTasks?.size ?: -1, "\"chill\" should have one dependent task")
        Assertions.assertEquals(0, chill?.dependentTasks?.size ?: -1, "\"boil\" should NOT have any dependent task")
        Assertions.assertEquals(lauter, mash?.dependentTasks?.get(0)?.to, "\"lauter\" depends on \"mash\"")
        Assertions.assertEquals(boil, lauter?.dependentTasks?.get(0)?.to, "\"boil\" depends on \"lauter\"")
        Assertions.assertEquals(chill, boil?.dependentTasks?.get(0)?.to, "\"chill\" depends on \"boil\"")
    }
}

package brewprocess.dsl

import brewprocess.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class BrewProcessBuilderTest {

    @Test
    fun testMash() {
        val mash = mash {}
        Assertions.assertEquals(mash::class, Mash::class)
        Assertions.assertEquals(mash.name, Task.MASH)
    }

    @Test
    fun testBoil() {
        val boil = boil {
            name = "setName"
            heatingPower = 10
        }

        Assertions.assertEquals(boil::class, Boil::class)
        Assertions.assertEquals(boil.name, "setName")
        Assertions.assertEquals(boil.heatingPower, 10)

        // Test assertions are thrown when not providing heatingPower
        val thrown = assertThrows<IllegalArgumentException> {
            boil {}
        }
        assertEquals(thrown.message, "heatingPower must be defined")
    }

    @Test
    fun testLauter() {
        val lauter = lauter {
            name = "setName"
            litersPerMin = 0.3
        }
        Assertions.assertEquals(lauter::class, Lauter::class)
        Assertions.assertEquals(lauter.name, "setName")
        Assertions.assertEquals(lauter.litersPerMin, 0.3)

        // Test assertions are thrown when not providing litersPerMin
        val thrown = assertThrows<IllegalArgumentException> {
            lauter {}
        }
        assertEquals(thrown.message, "litersPerMin must be defined")
    }

    @Test
    fun testAction() {
        val action = action {
            name = "anAction"
            description = "aDescription"
        }

        Assertions.assertEquals(action::class, SimpleAction::class)
        Assertions.assertEquals(action.name, "anAction")
        Assertions.assertEquals(action.description, "aDescription")

        // Test assertions are thrown when not providing name
        val thrown = assertThrows<IllegalArgumentException> {
            action {}
        }
        assertEquals(thrown.message, "name must be defined")
    }

    @Test
    fun testHeatWater() {
        val heatMashWater = heatWater {
            name = "heatMashWater"
            use = HeatWater.For.MASH
            heatingPower = 10
        }

        Assertions.assertEquals(heatMashWater::class, HeatWater::class)
        Assertions.assertEquals(heatMashWater.name, "heatMashWater")
        Assertions.assertEquals(heatMashWater.use, HeatWater.For.MASH)
        Assertions.assertEquals(heatMashWater.heatingPower, 10)

        // Test assertions are thrown when not providing name
        var thrown = assertThrows<IllegalArgumentException> {
            heatWater {}
        }
        assertEquals(thrown.message, "name must be defined")

        // Test assertions are thrown when not providing use
        thrown = assertThrows {
            heatWater {
                name = "name"
            }
        }
        assertEquals(thrown.message, "use must be defined when heating water")

        // Test assertions are thrown when not providing heatingPower
        thrown = assertThrows {
            heatWater {
                name = "name"
                use = HeatWater.For.MASH
            }
        }
        assertEquals(thrown.message, "heatingPower must be defined")
    }

    @Test
    fun testChill() {
        val chill = chill {
            chillingPower = 10
        }

        Assertions.assertEquals(chill::class, Chill::class)
        Assertions.assertEquals(chill.name, "chill")
        Assertions.assertEquals(chill.chillingPower, 10)

        // Test assertions are thrown when not providing heatingPower
        var thrown = assertThrows<IllegalArgumentException> {
            chill {}
        }
        assertEquals(thrown.message, "chillingPower must be defined")
    }


    @Test
    fun testBrewProcess() {
    }
}
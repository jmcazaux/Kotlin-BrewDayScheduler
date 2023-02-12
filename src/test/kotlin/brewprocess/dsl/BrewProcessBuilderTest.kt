package brewprocess.dsl

import brewprocess.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class BrewProcessBuilderTest {

    @Test
    fun canBuildMash() {
        val mash = mash {}
        assertEquals(Mash::class, mash::class)
        assertEquals(Task.MASH, mash.name)
    }

    @Test
    fun canBuildBoil() {
        val boil = boil {
            name = "setName"
            heatingPower = 10
        }

        assertEquals(Boil::class, boil::class)
        assertEquals("setName", boil.name)
        assertEquals(10, boil.heatingPower)

        // Test assertions are thrown when not providing heatingPower
        val thrown = assertThrows<IllegalArgumentException> {
            boil {}
        }
        assertEquals(thrown.message, "heatingPower must be defined")
    }

    @Test
    fun canBuildLauter() {
        val lauter = lauter {
            name = "setName"
            litersPerMin = 0.3
        }
        assertEquals(Lauter::class, lauter::class)
        assertEquals("setName", lauter.name)
        assertEquals(0.3, lauter.litersPerMin)

        // Test assertions are thrown when not providing litersPerMin
        val thrown = assertThrows<IllegalArgumentException> {
            lauter {}
        }
        assertEquals(thrown.message, "litersPerMin must be defined")
    }

    @Test
    fun canBuildDrainMash() {
        val drainMash = drainMash {
            name = "setName"
            duration = 20
        }
        assertEquals(DrainMash::class, drainMash::class)
        assertEquals("setName", drainMash.name)
        assertEquals(20, drainMash.duration)

        // Test assertions are thrown when not providing litersPerMin
        val thrown = assertThrows<IllegalArgumentException> {
            drainMash {}
        }
        assertEquals(thrown.message, "duration must be defined")
    }

    @Test
    fun canBuildAction() {
        val action = action {
            name = "anAction"
            description = "aDescription"
        }

        assertEquals(SimpleAction::class, action::class)
        assertEquals("anAction", action.name)
        assertEquals("aDescription", action.description)

        // Test assertions are thrown when not providing name
        val thrown = assertThrows<IllegalArgumentException> {
            action {}
        }
        assertEquals(thrown.message, "name must be defined")
    }

    @Test
    fun canBuildHeatWater() {
        val heatMashWater = heatWater {
            name = "heatMashWater"
            use = HeatWater.For.MASH
            heatingPower = 10
        }

        assertEquals(HeatWater::class, heatMashWater::class)
        assertEquals("heatMashWater", heatMashWater.name)
        assertEquals(HeatWater.For.MASH, heatMashWater.use)
        assertEquals(10, heatMashWater.heatingPower)

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
    fun canBuildChill() {
        val chill = chill {
            chillingPower = 10
        }

        assertEquals(Chill::class, chill::class)
        assertEquals("chill", chill.name)
        assertEquals(10, chill.chillingPower)

        // Test assertions are thrown when not providing heatingPower
        val thrown = assertThrows<IllegalArgumentException> {
            chill {}
        }
        assertEquals(thrown.message, "chillingPower must be defined")
    }

    @Test
    fun canBuildBrewProcess() {
        val thrown = assertThrows<IllegalArgumentException> {
            brewProcess {}
        }
        assertEquals(thrown.message, "process name must be defined")

        val process = brewProcess {
            name = "threeVessels"
            description = "process description for 3 vessels"

            tasks = listOf(
                heatWater {
                    name = "Heat Mash Water"
                    use = HeatWater.For.MASH
                    heatingPower = 500
                },
                mash {},
                lauter { litersPerMin = 1.0 },
                boil { heatingPower = 1000 },
                action {
                    name = "Sanitize Chiller"
                    description = "Sanitize Chiller in/with boiling wort"
                },
                chill { chillingPower = 500 }
            )

            dependencies = listOf(
                dependency {
                    fromTask = "Heat Mash Water"
                    toTask = "mash"
                    type = DependencyType.STARTS_AFTER_END
                },
                dependency {
                    fromTask = "mash"
                    toTask = "lauter"
                    type = DependencyType.STARTS_AFTER_END
                },
                dependency {
                    fromTask = "lauter"
                    toTask = "boil"
                    type = DependencyType.STARTS_AFTER_END
                },
                dependency {
                    fromTask = "boil"
                    toTask = "Sanitize Chiller"
                    type = DependencyType.FINISH_BEFORE_END
                    delay = 600
                    parametrizedDelay = true
                },
                dependency {
                    fromTask = "boil"
                    toTask = "chill"
                    type = DependencyType.STARTS_AFTER_END
                }
            )
        }

        assertEquals(BrewProcess::class, process::class)
        assertEquals("threeVessels", process.name)
        assertEquals("process description for 3 vessels", process.description)
        assertEquals(6, process.tasks.size)
        assertEquals(5, process.dependencies.size)

        val mash = process.tasks["mash"] as Mash
        val lauter = process.tasks["lauter"] as Lauter
        val sanitizeChiller = process.tasks["Sanitize Chiller"] as SimpleAction
        val boil = process.tasks["boil"] as Boil

        assertEquals(1, mash.dependentTasks.size)
        assertEquals(mash.dependentTasks[0].to, lauter)
        assertEquals(mash.dependentTasks[0].type, DependencyType.STARTS_AFTER_END)

        assertEquals(2, boil.dependentTasks.size)
        assertEquals(boil.dependentTasks[0].to, sanitizeChiller)
        assertEquals(boil.dependentTasks[0].type, DependencyType.FINISH_BEFORE_END)
        assertEquals(boil.dependentTasks[0].delay, 600)
        assertTrue(boil.dependentTasks[0].parametrizeDelay)
    }
}

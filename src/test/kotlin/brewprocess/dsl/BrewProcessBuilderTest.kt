package brewprocess.dsl

import brewprocess.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class BrewProcessBuilderTest {

    @Test
    fun canBuildMash() {
        val mash = mash {}
        Assertions.assertEquals(Mash::class, mash::class)
        Assertions.assertEquals(Task.MASH, mash.name)
    }

    @Test
    fun canBuildBoil() {
        val boil = boil {
            name = "setName"
            heatingPower = 10
        }

        Assertions.assertEquals(Boil::class, boil::class)
        Assertions.assertEquals("setName", boil.name)
        Assertions.assertEquals(10, boil.heatingPower)

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
        Assertions.assertEquals(Lauter::class, lauter::class)
        Assertions.assertEquals("setName", lauter.name)
        Assertions.assertEquals(0.3, lauter.litersPerMin)

        // Test assertions are thrown when not providing litersPerMin
        val thrown = assertThrows<IllegalArgumentException> {
            lauter {}
        }
        assertEquals(thrown.message, "litersPerMin must be defined")
    }

    @Test
    fun canBuildAction() {
        val action = action {
            name = "anAction"
            description = "aDescription"
        }

        Assertions.assertEquals(SimpleAction::class, action::class)
        Assertions.assertEquals("anAction", action.name)
        Assertions.assertEquals("aDescription", action.description)

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

        Assertions.assertEquals(HeatWater::class, heatMashWater::class)
        Assertions.assertEquals("heatMashWater", heatMashWater.name)
        Assertions.assertEquals(HeatWater.For.MASH, heatMashWater.use)
        Assertions.assertEquals(10, heatMashWater.heatingPower)

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

        Assertions.assertEquals(Chill::class, chill::class)
        Assertions.assertEquals("chill", chill.name)
        Assertions.assertEquals(10, chill.chillingPower)

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

        Assertions.assertEquals(BrewProcess::class, process::class)
        Assertions.assertEquals("threeVessels", process.name)
        Assertions.assertEquals(6, process.tasks.size)
        Assertions.assertEquals(5, process.dependencies.size)

        val mash = process.tasks["mash"] as Mash
        val lauter = process.tasks["lauter"] as Lauter
        val sanitizeChiller = process.tasks["Sanitize Chiller"] as SimpleAction
        val boil = process.tasks["boil"] as Boil

        Assertions.assertEquals(1, mash.dependentTasks.size)
        Assertions.assertEquals(mash.dependentTasks[0].to, lauter)
        Assertions.assertEquals(mash.dependentTasks[0].type, DependencyType.STARTS_AFTER_END)

        Assertions.assertEquals(2, boil.dependentTasks.size)
        Assertions.assertEquals(boil.dependentTasks[0].to, sanitizeChiller)
        Assertions.assertEquals(boil.dependentTasks[0].type, DependencyType.FINISH_BEFORE_END)
        Assertions.assertEquals(boil.dependentTasks[0].delay, 600)
        Assertions.assertTrue(boil.dependentTasks[0].parametrizeDelay)
    }
}

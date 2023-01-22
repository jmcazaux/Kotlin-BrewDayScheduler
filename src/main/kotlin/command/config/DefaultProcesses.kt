package command.config

import brewprocess.DependencyType
import brewprocess.HeatWater
import brewprocess.dsl.*

class DefaultProcesses {
    companion object {
        val THREE_VESSELS = brewProcess {
            name = "Three Vessels"

            tasks = listOf(
                heatWater {
                    name = "Heat Mash Water"
                    use = HeatWater.For.MASH
                    heatingPower = 2000
                },
                mash {},
                heatWater {
                    name = "Heat Sparge Water"
                    use = HeatWater.For.MASH
                    heatingPower = 2000
                },
                lauter { litersPerMin = 1.0 },
                boil { heatingPower = 2000 },
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
                    toTask = "Heat Sparge Water"
                    type = DependencyType.FINISH_BEFORE_END
                    delay = 180
                },
                dependency {
                    fromTask = "lauter"
                    toTask = "boil"
                    type = DependencyType.STARTS_AFTER_END
                },
                dependency {
                    fromTask = "boil"
                    toTask = "Sanitize Chiller"
                    type = DependencyType.STARTS_BEFORE_END
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
    }
}

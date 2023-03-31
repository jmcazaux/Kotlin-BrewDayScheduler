package command.config

import brewprocess.BrewProcess
import brewprocess.DependencyType
import brewprocess.HeatWater
import brewprocess.dsl.*

class DefaultProcesses {
    companion object {
        val THREE_VESSELS = brewProcess {
            name = "Two/Three Vessels"
            description =
                "Three vessels systems are set up with a Hot Liquor Tank, a Mash Tun, and a Boil Kettle.\n" +
                "The hot liquor tank has the least intuitive name: brewers refer to water as liquor.\n" +
                "The only thing you use the Hot Liquor Tank (HLT) for is heating up water " +
                "to use at various points in the brewing process (namely mash & sparge).\n" +
                "Two vessels systems uses essentially the same process except that the Hot Liquor Tank " +
                "does not have a heating source per se.\n" +
                "The water is heat up in the boil kettle, then transferred " +
                "to the mash tun or a buffer bucket (before sparge)."

            tasks = listOf(
                heatWater {
                    name = "Heat Mash Water"
                    use = HeatWater.For.MASH
                    heatingPower = 2000
                },
                mash {},
                heatWater {
                    name = "Heat Sparge Water"
                    use = HeatWater.For.SPARGE
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

        val SINGLE_VESSEL = brewProcess {
            name = "Single Vessel / Brew in Bag"
            description =
                "The Brew In A Bag method – BIAB – uses just one vessel for the entire “hot side” of the brew, " +
                "both mash and boil.\n" +
                "There is no sparge step. Instead, the full volume of liquor required for the brew is added at the start.\n" +
                "A nylon mesh bag or an inner filtering shell in the mash kettle keeps the grain together and " +
                "makes it easier to remove and drain at the end of the mash. \n" +
                "From there, the process is identical to other all grain methods (boil, chill, etc.)."

            tasks = listOf(
                heatWater {
                    name = "Heat Mash Water"
                    use = HeatWater.For.MASH
                    heatingPower = 2000
                },
                mash {},
                drainMash {
                    name = "Drain the mash"
                    duration = 30
                },
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
                    toTask = "Drain the mash"
                    type = DependencyType.STARTS_AFTER_END
                },
                dependency {
                    fromTask = "Drain the mash"
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

        fun all(): List<BrewProcess> = listOf(THREE_VESSELS, SINGLE_VESSEL)

        fun getByName(name: String): BrewProcess? {
            return all().find { it.name == name }
        }

        val defaultProcess: BrewProcess
            get() = THREE_VESSELS
    }
}

import command.BrewDayScheduler
import command.config.BdsConfig
import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>): Unit = exitProcess(CommandLine(BrewDayScheduler())
                                                    .addSubcommand(BdsConfig())
                                                    .execute(*args))

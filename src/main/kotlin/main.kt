import command.BrewDayScheduler
import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>): Unit = exitProcess(CommandLine(BrewDayScheduler()).execute(*args))

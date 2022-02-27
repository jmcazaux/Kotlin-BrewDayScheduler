import command.BrewDayScheduler
import picocli.CommandLine
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println("Ignition sequence started...")
    exitProcess(CommandLine(BrewDayScheduler()).execute(*args))
}

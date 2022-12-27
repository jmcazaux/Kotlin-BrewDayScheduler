import command.BrewDayScheduler
import command.config.ConfigCommand
import picocli.CommandLine
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>): Unit {

    val userHomeDir = System.getProperty("user.home")
    val configFilePath = userHomeDir+AppConstants.CONFIG_FILE_REL_PATH
    val configFile = File(configFilePath)

    exitProcess(
        CommandLine(BrewDayScheduler())
            .addSubcommand(ConfigCommand(configFile))
            .execute(*args)
    )
}

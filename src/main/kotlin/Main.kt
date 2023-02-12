import command.BrewDayScheduler
import command.config.AppConfig
import command.config.ConfigCommand
import command.prompt.Prompt
import picocli.CommandLine
import java.io.File
import java.io.InvalidObjectException
import java.util.concurrent.Callable

fun main(args: Array<String>) {
    val userHomeDir = System.getProperty("user.home")
    val bdsDirectory = File(userHomeDir, AppConstants.APP_DIRECTORY) // Normally: ~/.brewdaysheduler

    if (!bdsDirectory.exists()) {
        bdsDirectory.mkdirs()
    }
    val configCommand = ConfigCommand(bdsDirectory)

    val app = BdsApp(arrayOf(configCommand))
    app.run(args)
}

class BdsApp(val subCommands: Array<Callable<Int>>) {
    init {
        try {
            val configCommand = subCommands.filterIsInstance<ConfigCommand>().first()

            val setupConfigPrompt = Prompt(
                valueName = "setup",
                valueType = Boolean::class,
                question = "Would you like to define your brew process now?",
                help = "Defining the parameters of your brew process is necessary to plan your brew schedule accurately.\n" +
                    "We will just ask you a few questions about your equipment and process.",
                default = true
            )

            if (AppConfig.isDefault && setupConfigPrompt.prompt() == true) {
                configCommand.setUpAndSaveConfig()
            }
        } catch (_: NoSuchElementException) {
            throw InvalidObjectException("No config sub-command provided. The world will go pear-shaped...")
        }
    }

    fun run(args: Array<String>) {
        var command = CommandLine(BrewDayScheduler())
        for (subCommand in subCommands) {
            command = command.addSubcommand(subCommand)
        }
        command.execute(*args)
    }
}

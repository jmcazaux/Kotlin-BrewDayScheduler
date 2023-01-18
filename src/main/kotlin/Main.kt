import command.BrewDayScheduler
import command.config.AppConfig
import command.config.ConfigCommand
import command.prompt.Prompt
import picocli.CommandLine
import java.io.File

fun main(args: Array<String>) {
    val userHomeDir = System.getProperty("user.home")
    val configFilePath = userHomeDir + AppConstants.CONFIG_FILE_REL_PATH
    val configFile = File(configFilePath)

    val configCommand = ConfigCommand(configFile)

    val setupConfigPrompt = Prompt(
        valueName = "setup",
        valueType = Boolean::class,
        question = "Would you like to define your brew process now?",
        help = "Defining the parameters of your brew process is necessary to plan your brew schedule accurately.\n" +
            "It id just a few questions about your equipment and process.",
        default = true
    )

    if (AppConfig.isDefault && setupConfigPrompt.prompt() == true) {
        configCommand.setUpAndSaveConfig()
    }

    CommandLine(BrewDayScheduler())
        .addSubcommand(configCommand)
        .execute(*args)
}

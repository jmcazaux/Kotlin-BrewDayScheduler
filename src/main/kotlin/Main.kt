import command.prompt.Prompt
import java.io.File

fun main(args: Array<String>) {
    val userHomeDir = System.getProperty("user.home")
    val configFilePath = userHomeDir + AppConstants.CONFIG_FILE_REL_PATH
    val configFile = File(configFilePath)

    val r = Prompt(
        valueType = Int::class,
        valueName = "ValueName",
        question = "QuestionToTheUser",
        help = "ThisShouldHelpYou",
        default = 4,
        min = 3,
        max = 8
    ).prompt()

    println("We got $r")

//    exitProcess(
//        CommandLine(BrewDayScheduler())
//            .addSubcommand(ConfigCommand(configFile))
//            .execute(*args)
//    )
}

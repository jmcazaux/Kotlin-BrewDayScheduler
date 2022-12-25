package command

import command.config.BdsConfig
import picocli.CommandLine
import java.util.concurrent.Callable

/**
 * Main brew day scheduler command
 */
@CommandLine.Command(
    name = "bds", mixinStandardHelpOptions = true, version = ["1.0 alpha"],
    description = ["Your brew day scheduler in a command line!"]
)
class BrewDayScheduler : Callable<Int> {

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display a help message"])
    private var helpRequested: Boolean = false;

    override fun call(): Int {
        println(CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Brew Day Scheduler: all systems going...|@"))
        println(CommandLine.Help.Ansi.AUTO.string("@|yellow Brew Day Scheduler: all done...|@"))
        return 0
    }
}

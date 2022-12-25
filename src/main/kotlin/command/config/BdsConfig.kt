package command.config

import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "config", mixinStandardHelpOptions = true,
    description = ["Configure brew day scheduler according to your process"]
)
class BdsConfig : Callable<Int> {

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display help for this command"])
    private var helpRequested: Boolean = false;

    @CommandLine.Option(names = ["-l", "--list"], description = ["list the current configuration"])
    private var listRequested: Boolean = false;

    @CommandLine.Option(names = ["-s", "--set-up"], description = ["interactively set up a configuration"])
    private var setUpRequested: Boolean = false;

    override fun call(): Int {
        TODO("Not yet implemented")
    }
}
package command.config

import picocli.CommandLine
import picocli.CommandLine.ExitCode
import java.io.File
import java.util.concurrent.Callable

object AppConfig {
    var brewProcess = DefaultProcesses.THREE_VESSELS
    var isDefault = true // Whether this configuration is a default one or it has been set by the user
}

@CommandLine.Command(
    name = "config",
    mixinStandardHelpOptions = true,
    description = ["Configure brew day scheduler according to your process"]
)
class ConfigCommand(private val configFile: File) : Callable<Int> {

    init {
        if (configFile.exists() && configFile.length() > 0) {
            this.loadConfigFromFile()
        }
    }

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display help for this command"])
    private var helpRequested: Boolean = false

    @CommandLine.Option(names = ["-l", "--list"], description = ["list the current configuration"])
    private var listRequested: Boolean = false

    @CommandLine.Option(names = ["-s", "--set-up"], description = ["interactively set up a configuration"])
    private var setUpRequested: Boolean = false

    override fun call(): Int {
        if (this.listRequested) {
            this.listConfig()
            return ExitCode.OK
        }

        if (this.setUpRequested) {
            return this.setUpAndSaveConfig()
        }

        return ExitCode.SOFTWARE
    }

    fun setUpAndSaveConfig(): Int {
        TODO("Not yet implemented")
    }

    fun listConfig() {
        TODO("Not yet implemented")
    }

    private fun loadConfigFromFile() {
        TODO("Not yet implemented")
    }
}

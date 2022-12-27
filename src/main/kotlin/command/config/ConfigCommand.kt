package command.config

import brewprocess.BrewProcess
import picocli.CommandLine
import picocli.CommandLine.ExitCode
import java.io.File
import java.util.concurrent.Callable


class AppConfig {
    lateinit var brewProcess: BrewProcess
    internal set

    var isDefault: Boolean = false  //Whether this configuration is a default one or it has been set by the user
    internal set
}


@CommandLine.Command(
    name = "config", mixinStandardHelpOptions = true,
    description = ["Configure brew day scheduler according to your process"]
)
class ConfigCommand(val configFile: File) : Callable<Int> {

    var configuration: AppConfig
    private set

    init {
        this.configuration = AppConfig()
        this.configuration.brewProcess = DefaultProcesses.THREE_VESSELS
    }

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display help for this command"])
    private var helpRequested: Boolean = false;

    @CommandLine.Option(names = ["-l", "--list"], description = ["list the current configuration"])
    private var listRequested: Boolean = false;

    @CommandLine.Option(names = ["-s", "--set-up"], description = ["interactively set up a configuration"])
    private var setUpRequested: Boolean = false;

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

    fun hasConfig(): Boolean = this.configFile.exists() && this.configFile.length() > 0

    fun setUpAndSaveConfig(): Int {
        TODO("Not yet implemented")
    }

    fun listConfig() {
        TODO("Not yet implemented")
    }

    private fun loadConfigOrDefault(): BrewProcess {
        TODO("Not yet implemented")
    }

}
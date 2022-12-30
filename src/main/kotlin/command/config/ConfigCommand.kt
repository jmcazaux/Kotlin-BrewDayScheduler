package command.config

import brewprocess.BrewProcess
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import picocli.CommandLine
import picocli.CommandLine.ExitCode
import java.io.File
import java.util.concurrent.Callable
import kotlin.properties.Delegates

@JsonIgnoreProperties(value = ["isDefault"])
object AppConfig {
    var process by Delegates.notNull<BrewProcess>()

    var isDefault by Delegates.notNull<Boolean>() // Whether this configuration is a default one or it has been set by the user
}

@CommandLine.Command(
    name = "config",
    mixinStandardHelpOptions = true,
    description = ["Configure brew day scheduler according to your process"]
)
class ConfigCommand(private val configFile: File) : Callable<Int> {

    init {
        AppConfig.process = DefaultProcesses.THREE_VESSELS
        AppConfig.isDefault = true

        if (configFile.exists() && configFile.length() > 0) {
            this.loadConfigFromFile()
            AppConfig.isDefault = false
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
        val mapper = jacksonObjectMapper()
        val config = mapper.readValue<JsonNode>(this.configFile.readText())
        AppConfig.process = mapper.treeToValue(config["process"], BrewProcess::class.java)
    }
}

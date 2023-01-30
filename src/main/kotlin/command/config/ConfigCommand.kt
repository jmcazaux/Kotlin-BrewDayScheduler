package command.config

import brewprocess.BrewProcess
import brewprocess.ProcessParameter
import brewprocess.ProcessParameterType
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import command.prompt.Prompt
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
        val currentProcess = AppConfig.process

        for (processParameter: ProcessParameter<*> in currentProcess.parameters) {
            when (processParameter.type) {
                ProcessParameterType.INT -> {
                    val processParameter = processParameter as ProcessParameter<Int>
                    val newValue = Prompt(
                        valueName = processParameter.name,
                        question = processParameter.prompt,
                        help = processParameter.description,
                        default = processParameter.current as Int,
                        valueType = Int::class
                    ).prompt()

                    if (newValue != null) {
                        processParameter.setter(newValue)
                    }
                }

                ProcessParameterType.DOUBLE -> {
                    val processParameter = processParameter as ProcessParameter<Double>
                    val newValue = Prompt(
                        valueName = processParameter.name,
                        question = processParameter.prompt,
                        help = processParameter.description,
                        default = processParameter.current as Double,
                        valueType = Double::class
                    ).prompt()

                    if (newValue != null) {
                        processParameter.setter(newValue)
                    }
                }
            }
        }
        return 0
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

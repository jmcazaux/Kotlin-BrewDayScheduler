package command.config

import brewprocess.BrewProcess
import brewprocess.ProcessParameter
import brewprocess.ProcessParameterType
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
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
        val selectedProcess = selectBaseProcess(default = AppConfig.process)

        for (processParameter: ProcessParameter<*> in selectedProcess.getParameters()) {
            when (processParameter.type) {
                ProcessParameterType.INT -> {
                    val parameter = processParameter as ProcessParameter<Int>
                    val newValue = Prompt(
                        valueName = parameter.name,
                        question = parameter.prompt,
                        help = parameter.description,
                        default = parameter.current as Int,
                        valueType = Int::class,
                        min = 0 // TODO: Should rather be an attribute of the ProcessParameter
                    ).prompt()

                    if (newValue != null) {
                        parameter.setter(newValue)
                    }
                }

                ProcessParameterType.DOUBLE -> {
                    val parameter = processParameter as ProcessParameter<Double>
                    val newValue = Prompt(
                        valueName = parameter.name,
                        question = parameter.prompt,
                        help = parameter.description,
                        default = parameter.current as Double,
                        valueType = Double::class,
                        min = 0.0 // TODO: Should rather be an attribute of the ProcessParameter
                    ).prompt()

                    if (newValue != null) {
                        parameter.setter(newValue)
                    }
                }
            }
            println()
        }

        AppConfig.isDefault = false
        AppConfig.process = selectedProcess
        saveConfigToFile()
        return 0
    }

    private fun selectBaseProcess(default: BrewProcess): BrewProcess {
        val allProcesses = DefaultProcesses.all().sortedBy { it.name }

        var selected: Int? = null
        while (selected == null) {
            promptForDefaultBaseProcess(allProcesses, default)

            val input = readln()

            if (input.isEmpty()) {
                return default
            }

            try {
                selected = input.toInt()
            } catch (_: Exception) {
                println("Please enter a valid number between 1 and ${allProcesses.size}.")
                continue
            }

            if (selected < 1 || selected > allProcesses.size) {
                println("Please enter a valid number between 1 and ${allProcesses.size}.")
                continue
            }
        }

        return allProcesses[selected - 1]
    }

    private fun promptForDefaultBaseProcess(
        processes: List<BrewProcess>,
        default: BrewProcess
    ) {
        val defaultIndex = processes.indexOfFirst { it.name == default.name } + 1

        println(
            "Please select your process in the list below (current process is " +
                "${Prompt.Style.BOLD.code}\"${default.name}\"${Prompt.Style.RESET.code}):"
        )
        var idx = 1
        for (process: BrewProcess in processes) {
            println("  - $idx: ${process.name} ${if (idx == defaultIndex) "(*)" else ""}")

            idx++
        }
        println()
        println(
            "Please type ${(1..processes.size).joinToString(separator = ",")} or " +
                "${Prompt.Style.ITALIC.code}'enter'${Prompt.Style.RESET.code} to keep the current process:"
        )
    }

    fun listConfig() {
        TODO("Not yet implemented")
    }

    private fun loadConfigFromFile() {
        val mapper = jacksonObjectMapper()
        val config = mapper.readValue<JsonNode>(this.configFile.readText())
        AppConfig.process = mapper.treeToValue(config["process"], BrewProcess::class.java)
    }

    private fun saveConfigToFile() {
        val mapper = jacksonObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)

        mapper.writeValue(this.configFile, AppConfig.process)
    }
}

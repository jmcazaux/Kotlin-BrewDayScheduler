package command.config

import AppConstants
import brewprocess.BrewProcess
import brewprocess.ProcessParameter
import brewprocess.ProcessParameterType
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import command.prompt.Prompt
import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.WordUtils
import picocli.CommandLine
import picocli.CommandLine.ExitCode
import java.io.File
import java.util.concurrent.Callable
import kotlin.properties.Delegates

@JsonIgnoreProperties(value = ["default"])
object AppConfig {
    var process by Delegates.notNull<BrewProcess>()
    var isDefault by Delegates.notNull<Boolean>() // Whether this configuration is a default one or it has been set by the user
}

@CommandLine.Command(
    name = "config",
    mixinStandardHelpOptions = true,
    description = ["Configure brew day scheduler according to your process"]
)
class ConfigCommand(bdsDirectory: File) : Callable<Int> {

    private val configFile = File(bdsDirectory, AppConstants.CONFIG_FILE_NAME)

    init {
        AppConfig.process = DefaultProcesses.defaultProcess
        AppConfig.isDefault = true

        if (configFile.exists() && configFile.length() > 0) {
            this.loadConfigFromFile()
            AppConfig.isDefault = false
        }
    }

    companion object {
        const val MAX_OUTPUT_LINE_LENGTH = 80
    }

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["display help for this command"])
    private var helpRequested: Boolean = false

    @CommandLine.Option(names = ["-p", "--print"], description = ["print the current configuration"])
    private var listRequested: Boolean = false

    @CommandLine.Option(names = ["-s", "--set-up"], description = ["interactively set up a configuration"])
    private var setUpRequested: Boolean = false

    override fun call(): Int {
        if (this.listRequested) {
            this.printConfig()
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
                        min = parameter.min,
                        max = parameter.max
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
                        min = parameter.min,
                        max = parameter.max
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
        return ExitCode.OK
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
            val formattedName =
                if (idx == defaultIndex) {
                    "${Prompt.Style.BOLD.code}${process.name}${Prompt.Style.RESET.code}"
                } else {
                    process.name
                }
            println("  - $idx > $formattedName")
            idx++
        }
        println(
            "\nPlease type ${(1..processes.size).joinToString(separator = ", ")} or " +
                "${Prompt.Style.ITALIC.code}'enter'${Prompt.Style.RESET.code} to keep the current process:"
        )
    }

    private fun printConfig() {
        val outputWidth = ConfigCommand.MAX_OUTPUT_LINE_LENGTH
        printConfigHeader(outputWidth)
        println()
        printProcess(outputWidth)
        println()
        printProcessParameters(outputWidth)
    }

    private fun printProcess(outputWidth: Int) {
        println("Brewing process: ${AppConfig.process.name}")
        println(StringUtils.repeat('-', outputWidth))

        AppConfig.process.description
            ?.split("\n")
            ?.forEach {
                println(WordUtils.wrap(it, outputWidth))
            }

        println(StringUtils.repeat('-', outputWidth))
    }

    private fun printProcessParameters(outputWidth: Int) {
        val maxLabelLength = AppConfig.process.getParameters().maxOf { it.name.length }

        for (parameter in AppConfig.process.getParameters()) {
            val paddedLabel = StringUtils.rightPad(parameter.name, maxLabelLength + 5, '.')
            val parameterWithUnit = StringUtils.leftPad("${parameter.current} ${parameter.unit}", 10)
            println("$paddedLabel $parameterWithUnit")
        }
    }

    private fun printConfigHeader(outputWidth: Int) {
        println(StringUtils.repeat('*', outputWidth))
        println("*" + StringUtils.center("BrewDayScheduler Configuration", outputWidth - 2) + "*")
        println(StringUtils.repeat('*', outputWidth))
    }

    private fun loadConfigFromFile() {
        val mapper = jacksonObjectMapper()
        val config = mapper.readValue<JsonNode>(this.configFile.readText())
        AppConfig.process = mapper.treeToValue(config["process"], BrewProcess::class.java)
    }

    private fun saveConfigToFile() {
        val mapper = jacksonObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)

        mapper.writeValue(this.configFile, AppConfig)
    }
}

package command

import picocli.CommandLine
import java.util.concurrent.Callable

/**
 * Main brew day scheduler command
 */
@CommandLine.Command(
    name = "bds", mixinStandardHelpOptions = true, version = ["1.0 alpha"],
    description = ["Your brew day scheduler"]
)
class BrewDayScheduler : Callable<Int> {

//    @Parameters(index = "0", description = ["The file whose checksum to calculate."])
//    lateinit var file: File
//
//    @Option(names = ["-a", "--algorithm"], description = ["MD5, SHA-1, SHA-256, ..."])
//    var algorithm = "SHA-256"

    override fun call(): Int {
        println("bds was called")
        return 0
    }
}

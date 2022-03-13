import brewprocess.BrewProcess
import brewprocess.Task
import brewprocess.TaskType
import java.io.File

val p = BrewProcess("test")

val mash = Task("mash", TaskType.WAIT)
p.addTask(mash)

val boil = Task("boil", TaskType.HEAT_WATER)
p.addTask(boil, "mash")

val out = File("/tmp/test.json")
p.writeToFile(out)

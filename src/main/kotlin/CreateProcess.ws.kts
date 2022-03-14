import brewprocess.Boil
import brewprocess.BrewProcess
import brewprocess.Mash
import java.io.File

val p = BrewProcess("test")

val mash = Mash("mash")
p.addTask(mash)

val boil = Boil("boil")
p.addTask(boil, "mash")

val out = File("/tmp/test.json")
p.writeToFile(out)

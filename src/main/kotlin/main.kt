import beerxml.Recipe
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

fun main(args: Array<String>) {
    println("Ignition sequence started!")
    val mapper = XmlMapper().registerModule(KotlinModule.Builder().build())
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    val recipeFile = Recipe::class.java.getResource("/test_recipe.xml")
    val recipes = mapper.readValue<List<Recipe>>(recipeFile.readText())
    println(recipes)
}
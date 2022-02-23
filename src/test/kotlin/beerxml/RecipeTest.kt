package beerxml

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test

class RecipeTest {

    @Test
    fun testDeserialization() {
        val mapper = XmlMapper().registerModule(KotlinModule.Builder().build())
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        val recipeFile = this::class.java.getResource("/recipes/test_recipe.xml")
        val recipes = mapper.readValue<List<Recipe>>(recipeFile.readText())
        println(recipes)
    }
}
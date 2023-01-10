package beerxml

import org.junit.jupiter.api.Test
import java.io.File

class RecipeTest {

    @Test
    fun testCanDeserializeFromSampleBeerXMLFiles() {
        val testRecipesFolder = File(this.javaClass.classLoader.getResource("recipes").file)
        for (recipeFile in testRecipesFolder.listFiles()) {
            val recipes = Recipe.fromFile(recipeFile)
            println(recipes)
        }
    }
}

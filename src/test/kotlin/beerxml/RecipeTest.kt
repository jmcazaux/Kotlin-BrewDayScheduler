package beerxml

import org.junit.jupiter.api.*
import java.io.File

@TestClassOrder(ClassOrderer.OrderAnnotation::class)
@Order(2)
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

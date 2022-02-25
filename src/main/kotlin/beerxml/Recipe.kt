package beerxml

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

@JacksonXmlRootElement(localName = "RECIPE")
data class Recipe(
    @JacksonXmlProperty(localName = "NAME") val name: String, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int, // VERSION
    @JacksonXmlProperty(localName = "BREWER") val brewer: String?, // BREWER
    @JacksonXmlProperty(localName = "ASST_BREWER") val assistantBrewer: String?, // ASST_BREWER
    @JacksonXmlProperty(localName = "BATCH_SIZE") val batchSize: Double, // BATCH_SIZE
    @JacksonXmlProperty(localName = "BOIL_SIZE") val boilSize: Double, // BOIL_SIZE
    @JacksonXmlProperty(localName = "BOIL_TIME") val boilTime: Int, // BOIL_TIME
    @JacksonXmlProperty(localName = "EFFICIENCY") val efficiency: Double, // EFFICIENCY 
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "TASTE_NOTES") val taste_notes: String?, // TASTE_NOTES
    @JacksonXmlProperty(localName = "TASTE_RATING") val taste_rating: Int?, // TASTE_RATING
    @JacksonXmlProperty(localName = "OG") val og: Double?, // OG
    @JacksonXmlProperty(localName = "FG") val fg: Double?, // FG
    @JacksonXmlProperty(localName = "CARBONATION") val carbonation: Double?, // CARBONATION
    @JacksonXmlProperty(localName = "FERMENTATION_STAGES") val fermentationStages: Int?, // FERMENTATION_STAGES
    @JacksonXmlProperty(localName = "PRIMARY_AGE") val primaryAge: Int?, // PRIMARY_AGE
    @JacksonXmlProperty(localName = "PRIMARY_TEMP") val primaryTemp: Double?, // PRIMARY_TEMP
    @JacksonXmlProperty(localName = "SECONDARY_AGE") val secondaryAge: Int?, // SECONDARY_AGE
    @JacksonXmlProperty(localName = "SECONDARY_TEMP") val secondaryTemp: Double?, // SECONDARY_TEMP
    @JacksonXmlProperty(localName = "TERTIARY_AGE") val tertiaryAge: Int?, // TERTIARY_AGE
    @JacksonXmlProperty(localName = "AGE") val age: Int?, // AGE
    @JacksonXmlProperty(localName = "AGE_TEMP") val ageTemp: Double?, // AGE_TEMP
    @JacksonXmlProperty(localName = "CARBONATION_USED") val carbonationUsed: String?, // CARBONATION_USED
    @JacksonXmlProperty(localName = "DATE") val date: String?, // DATE
    @JacksonXmlProperty(localName = "EST_OG") val estimatedOriginalGravity: String?, // EST_OG
    @JacksonXmlProperty(localName = "EST_FG") val estimatedFinalGravity: String?, // EST_FG
    @JacksonXmlProperty(localName = "EST_COLOR") val estimatedColor: Double?, // EST_COLOR
    @JacksonXmlProperty(localName = "IBU") val ibu: Double?, // IBU
    @JacksonXmlProperty(localName = "IBU_METHOD") val ibuMethod: String?, // IBU_METHOD
    @JacksonXmlProperty(localName = "EST_ABV") val estimatedAbv: Double?, // EST_ABV
    @JacksonXmlProperty(localName = "ABV") val abv: Double?, // ABV
    @JacksonXmlProperty(localName = "ACTUAL_EFFICIENCY") val actualEfficiency: Double?, // ACTUAL_EFFICIENCY
    @JacksonXmlProperty(localName = "CALORIES") val calories: Double?, // CALORIES
    @JacksonXmlProperty(localName = "DISPLAY_BATCH_SIZE") val displayBatchSize: String?, // DISPLAY_BATCH_SIZE
    @JacksonXmlProperty(localName = "DISPLAY_BOIL_SIZE") val displayBoilSize: String?, // DISPLAY_BOIL_SIZE
    @JacksonXmlProperty(localName = "DISPLAY_OG") val displayOriginalGravity: String?, // DISPLAY_OG
    @JacksonXmlProperty(localName = "DISPLAY_FG") val displayFinalGravity: String?, // DISPLAY_FG
    @JacksonXmlProperty(localName = "DISPLAY_PRIMARY_TEMP") val displayPrimaryTemp: String?, // DISPLAY_PRIMARY_TEMP
    @JacksonXmlProperty(localName = "DISPLAY_SECONDARY_TEMP") val displaySecondaryTemp: String?, // DISPLAY_SECONDARY_TEMP
    @JacksonXmlProperty(localName = "DISPLAY_TERTIARY_TEMP") val displayTertiaryTemp: String?, // DISPLAY_TERTIARY_TEMP
    @JacksonXmlProperty(localName = "DISPLAY_AGE_TEMP") val displayAgeTemp: String?, // DISPLAY_AGE_TEMP

    @JacksonXmlProperty(localName = "FERMENTABLES") val fermentables: List<Fermentable> = ArrayList(),
    @JacksonXmlProperty(localName = "HOPS") val hops: List<Hop> = ArrayList(),
    @JacksonXmlProperty(localName = "YEASTS") val yeasts: List<Yeast> = ArrayList(),
    @JacksonXmlProperty(localName = "WATERS") val waters: List<Water> = ArrayList(),
    @JacksonXmlProperty(localName = "MISCS") val miscIngredients: List<MiscIngredient> = ArrayList(),
    @JacksonXmlProperty(localName = "STYLE") val style: Style?,
    @JacksonXmlProperty(localName = "EQUIPMENT") val equipment: Equipment?,
    @JacksonXmlProperty(localName = "MASH") val mash: Mash?,
) {
    companion object {
        /**
         * Return the list of recipes from a BeerXML file
         */
        fun fromFile(file: File): List<Recipe> {
            val mapper = XmlMapper().registerModule(KotlinModule.Builder().build())
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            return mapper.readValue(file.readText())
        }
    }
}
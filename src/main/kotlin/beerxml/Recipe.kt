package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "RECIPE")
data class Recipe(
    @JacksonXmlProperty(localName = "NAME") val name: String, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int, // VERSION
    @JacksonXmlProperty(localName = "BREWER") val brewer: String?, // BREWER
    @JacksonXmlProperty(localName = "ASST_BREWER") val assistantBrewer: String?, // ASST_BREWER
    @JacksonXmlProperty(localName = "BATCH_SIZE") val batchSize: Double, // BATCH_SIZE
    @JacksonXmlProperty(localName = "BOIL_SIZE") val boilSize: Double, // BOIL_SIZE
    @JacksonXmlProperty(localName = "BOIL_TIME") val boilTime: Int, // BOIL_TIME
    @JacksonXmlProperty(localName = "EFFICIENCY") val efficiency: Double // EFFICIENCY
)
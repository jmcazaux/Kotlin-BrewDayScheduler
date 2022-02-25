package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Fermentable(
    @JacksonXmlProperty(localName = "NAME") val name: String?, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int?, // VERSION
    @JacksonXmlProperty(localName = "TYPE") val type: String?, // TYPE
    @JacksonXmlProperty(localName = "AMOUNT") val amount: Double?, // AMOUNT
    @JacksonXmlProperty(localName = "YIELD") val yield: Double?, // YIELD
    @JacksonXmlProperty(localName = "COLOR") val color: Double?, // COLOR
    @JacksonXmlProperty(localName = "ADD_AFTER_BOIL") val addAfterBoil: String?, // ADD_AFTER_BOIL
    @JacksonXmlProperty(localName = "ORIGIN") val origin: String?, // ORIGIN
    @JacksonXmlProperty(localName = "SUPPLIER") val supplier: String?, // SUPPLIER
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "COARSE_FINE_DIFF") val coarseFineDifference: Double?, // COARSE_FINE_DIFF
    @JacksonXmlProperty(localName = "MOISTURE") val moisture: Int?, // MOISTURE
    @JacksonXmlProperty(localName = "DIASTATIC_POWER") val diastaticPower: Int?, // DIASTATIC_POWER
    @JacksonXmlProperty(localName = "PROTEIN") val protein: Double?, // PROTEIN
    @JacksonXmlProperty(localName = "MAX_IN_BATCH") val maxInBatch: Int?, // MAX_IN_BATCH
    @JacksonXmlProperty(localName = "RECOMMEND_MASH") val recommendMash: String?, // RECOMMEND_MASH
    @JacksonXmlProperty(localName = "IBU_GAL_PER_LB") val ibuGalPerLb: Int?, // IBU_GAL_PER_LB
    @JacksonXmlProperty(localName = "DISPLAY_AMOUNT") val displayAmount: String?, // DISPLAY_AMOUNT
    @JacksonXmlProperty(localName = "INVENTORY") val inventory: String?, // INVENTORY
    @JacksonXmlProperty(localName = "POTENTIAL") val potential: Double?, // POTENTIAL
    @JacksonXmlProperty(localName = "DISPLAY_COLOR") val displayColor: String?, // DISPLAY_COLOR
)

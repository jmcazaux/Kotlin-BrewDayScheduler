package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Water(
    @JacksonXmlProperty(localName = "NAME") val name: String?, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int?, // VERSION
    @JacksonXmlProperty(localName = "AMOUNT") val amount: Double?, // AMOUNT
    @JacksonXmlProperty(localName = "CALCIUM") val calcium: Double?, // CALCIUM
    @JacksonXmlProperty(localName = "BICARBONATE") val bicarbonate: Double?, // BICARBONATE
    @JacksonXmlProperty(localName = "SULFATE") val sulfate: Double?, // SULFATE
    @JacksonXmlProperty(localName = "CHLORIDE") val chloride: Double?, // CHLORIDE
    @JacksonXmlProperty(localName = "SODIUM") val sodium: Double?, // SODIUM
    @JacksonXmlProperty(localName = "MAGNESIUM") val magnesium: Double?, // MAGNESIUM
    @JacksonXmlProperty(localName = "PH") val ph: Double?, // PH
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "DISPLAY_AMOUNT") val displayAmount: String?, // DISPLAY_AMOUNT
)

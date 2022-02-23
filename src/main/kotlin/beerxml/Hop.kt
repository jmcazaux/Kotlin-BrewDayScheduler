package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Hop(
    @JacksonXmlProperty(localName = "NAME") val name: String?, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int?, // VERSION
    @JacksonXmlProperty(localName = "ORIGIN") val origin: String?, // ORIGIN
    @JacksonXmlProperty(localName = "ALPHA") val alpha: Double?, // ALPHA
    @JacksonXmlProperty(localName = "AMOUNT") val amount: Double?, // AMOUNT
    @JacksonXmlProperty(localName = "USE") val use: String?, // USE
    @JacksonXmlProperty(localName = "TIME") val time: Int?, // TIME
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "TYPE") val type: String?, // TYPE
    @JacksonXmlProperty(localName = "FORM") val form: String?, // FORM
    @JacksonXmlProperty(localName = "BETA") val beta: Double?, // BETA
    @JacksonXmlProperty(localName = "HSI") val hsi: Int?, // HSI
    @JacksonXmlProperty(localName = "DISPLAY_AMOUNT") val display_aAmount: String?, // DISPLAY_AMOUNT
    @JacksonXmlProperty(localName = "INVENTORY") val inventory: String?, // INVENTORY
    @JacksonXmlProperty(localName = "DISPLAY_TIME") val displayTime: String?, // DISPLAY_TIME
)

package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class MiscIngredient(
    @JacksonXmlProperty(localName = "NAME") val name: String?, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int?, // VERSION
    @JacksonXmlProperty(localName = "TYPE") val type: String?, // TYPE
    @JacksonXmlProperty(localName = "USE") val use: String?, // USE
    @JacksonXmlProperty(localName = "AMOUNT") val amount: Double?, // AMOUNT
    @JacksonXmlProperty(localName = "TIME") val time: Int?, // TIME
    @JacksonXmlProperty(localName = "AMOUNT_IS_WEIGHT") val amountIsWeight: String?, // AMOUNT_IS_WEIGHT
    @JacksonXmlProperty(localName = "USE_FOR") val useFor: String?, // USE_FOR
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "DISPLAY_AMOUNT") val displayAmount: String?, // DISPLAY_AMOUNT
    @JacksonXmlProperty(localName = "INVENTORY") val inventory: String?, // INVENTORY
    @JacksonXmlProperty(localName = "DISPLAY_TIME") val displayTime: String?, // DISPLAY_TIME
    @JacksonXmlProperty(localName = "BATCH_SIZE") val batchSize: Double?, // BATCH_SIZE

)
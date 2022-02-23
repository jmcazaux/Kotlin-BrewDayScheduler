package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Yeast(
    @JacksonXmlProperty(localName = "NAME") val name: String?, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int?, // VERSION
    @JacksonXmlProperty(localName = "TYPE") val type: String?, // TYPE
    @JacksonXmlProperty(localName = "FORM") val form: String?, // FORM
    @JacksonXmlProperty(localName = "AMOUNT") val amount: Double?, // AMOUNT
    @JacksonXmlProperty(localName = "AMOUNT_IS_WEIGHT") val amountIsWeight: String?, // AMOUNT_IS_WEIGHT
    @JacksonXmlProperty(localName = "LABORATORY") val laboratory: String?, // LABORATORY
    @JacksonXmlProperty(localName = "PRODUCT_ID") val productId: String?, // PRODUCT_ID
    @JacksonXmlProperty(localName = "MIN_TEMPERATURE") val minTemperature: Double?, // MIN_TEMPERATURE
    @JacksonXmlProperty(localName = "MAX_TEMPERATURE") val maxTemperature: Double?, // MAX_TEMPERATURE
    @JacksonXmlProperty(localName = "FLOCCULATION") val flocculation: String?, // FLOCCULATION
    @JacksonXmlProperty(localName = "ATTENUATION") val attenuation: Double?, // ATTENUATION
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "BEST_FOR") val bestFor: String?, // BEST_FOR
    @JacksonXmlProperty(localName = "MAX_REUSE") val maxReuse: Int?, // MAX_REUSE
    @JacksonXmlProperty(localName = "TIMES_CULTURED") val timesCultured: Int?, // TIMES_CULTURED
    @JacksonXmlProperty(localName = "ADD_TO_SECONDARY") val addToSecondary: String?, // ADD_TO_SECONDARY
    @JacksonXmlProperty(localName = "DISPLAY_AMOUNT") val displayAmount: String?, // DISPLAY_AMOUNT
    @JacksonXmlProperty(localName = "DISP_MIN_TEMP") val displayMinTemp: String?, // DISP_MIN_TEMP
    @JacksonXmlProperty(localName = "DISP_MAX_TEMP") val displayMaxTemp: String?, // DISP_MAX_TEMP
    @JacksonXmlProperty(localName = "INVENTORY") val inventory: String?, // INVENTORY
    @JacksonXmlProperty(localName = "CULTURE_DATE") val cultureDate: String?, // CULTURE_DATE

)

package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class MashStep(
    @JacksonXmlProperty(localName = "NAME") val name: String?, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int?, // VERSION
    @JacksonXmlProperty(localName = "TYPE") val type: String?, // TYPE
    @JacksonXmlProperty(localName = "INFUSE_AMOUNT") val infuseAmount: Double?, // INFUSE_AMOUNT
    @JacksonXmlProperty(localName = "STEP_TIME") val stepTime: Int?, // STEP_TIME
    @JacksonXmlProperty(localName = "STEP_TEMP") val stepTemp: Double?, // STEP_TEMP
    @JacksonXmlProperty(localName = "RAMP_TIME") val rampTime: Int?, // RAMP_TIME
    @JacksonXmlProperty(localName = "END_TEMP") val endTemp: Double?, // END_TEMP
    @JacksonXmlProperty(localName = "DESCRIPTION") val description: String?, // DESCRIPTION
    @JacksonXmlProperty(localName = "WATER_GRAIN_RATIO") val waterGrainRatio: Double?, // WATER_GRAIN_RATIO
    @JacksonXmlProperty(localName = "DECOCTION_AMT") val decoctionAmount: Double?, // DECOCTION_AMT
    @JacksonXmlProperty(localName = "INFUSE_TEMP") val infuseTemp: Double?, // INFUSE_TEMP
    @JacksonXmlProperty(localName = "DISPLAY_STEP_TEMP") val displayStepTemp: String?, // DISPLAY_STEP_TEMP
    @JacksonXmlProperty(localName = "DISPLAY_INFUSE_AMT") val displayInfuseAmount: String?, // DISPLAY_INFUSE_AMT
)

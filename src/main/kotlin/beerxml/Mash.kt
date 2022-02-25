package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Mash(
    @JacksonXmlProperty(localName = "NAME") val name: String?, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int?, // VERSION
    @JacksonXmlProperty(localName = "GRAIN_TEMP") val grainTemp: Double?, // GRAIN_TEMP
    @JacksonXmlProperty(localName = "TUN_TEMP") val tunTemp: Double?, // TUN_TEMP
    @JacksonXmlProperty(localName = "SPARGE_TEMP") val spargeTemp: Double?, // SPARGE_TEMP
    @JacksonXmlProperty(localName = "PH") val ph: Double?, // PH
    @JacksonXmlProperty(localName = "TUN_WEIGHT") val tunWeight: Double?, // TUN_WEIGHT
    @JacksonXmlProperty(localName = "TUN_SPECIFIC_HEAT") val tunSpecificHeat: Double?, // TUN_SPECIFIC_HEAT
    @JacksonXmlProperty(localName = "EQUIP_ADJUST") val equipAdjust: String?, // EQUIP_ADJUST
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "DISPLAY_GRAIN_TEMP") val displayGrainTemp: String?, // DISPLAY_GRAIN_TEMP
    @JacksonXmlProperty(localName = "DISPLAY_TUN_TEMP") val displayTunTemp: Int?, // DISPLAY_TUN_TEMP
    @JacksonXmlProperty(localName = "DISPLAY_SPARGE_TEMP") val displaySpargeTemp: String?, // DISPLAY_SPARGE_TEMP
    @JacksonXmlProperty(localName = "DISPLAY_TUN_WEIGHT") val displayTunWeight: String?, // DISPLAY_TUN_WEIGHT

    @JacksonXmlProperty(localName = "MASH_STEPS") val mash: List<MashStep>,


    )

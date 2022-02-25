package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Equipment(
    @JacksonXmlProperty(localName = "NAME") val name: String?, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int?, // VERSION
    @JacksonXmlProperty(localName = "BOIL_SIZE") val boilSize: Double?, // BOIL_SIZE
    @JacksonXmlProperty(localName = "BATCH_SIZE") val batchSize: Double?, // BATCH_SIZE
    @JacksonXmlProperty(localName = "TUN_VOLUME") val tunVolume: Double?, // TUN_VOLUME
    @JacksonXmlProperty(localName = "TUN_WEIGHT") val tunWeight: Double?, // TUN_WEIGHT
    @JacksonXmlProperty(localName = "TUN_SPECIFIC_HEAT") val tunSpecificHeat: Double?, // TUN_SPECIFIC_HEAT
    @JacksonXmlProperty(localName = "TOP_UP_WATER") val topUpWater: Int?, // TOP_UP_WATER
    @JacksonXmlProperty(localName = "TRUB_CHILLER_LOSS") val trubChillerLoss: Double?, // TRUB_CHILLER_LOSS
    @JacksonXmlProperty(localName = "EVAP_RATE") val evaportionRate: Int?, // EVAP_RATE
    @JacksonXmlProperty(localName = "BOIL_TIME") val boilTime: Int?, // BOIL_TIME
    @JacksonXmlProperty(localName = "CALC_BOIL_VOLUME") val calculatedBoilVolume: String?, // CALC_BOIL_VOLUME
    @JacksonXmlProperty(localName = "LAUTER_DEADSPACE") val lauterDeadspace: Double?, // LAUTER_DEADSPACE
    @JacksonXmlProperty(localName = "TOP_UP_KETTLE") val topUpKettle: Int?, // TOP_UP_KETTLE
    @JacksonXmlProperty(localName = "HOP_UTILIZATION") val hopUtilization: Int?, // HOP_UTILIZATION
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "DISPLAY_BOIL_SIZE") val displayBoilSize: String?, // DISPLAY_BOIL_SIZE
    @JacksonXmlProperty(localName = "DISPLAY_BATCH_SIZE") val displayBatchSize: String?, // DISPLAY_BATCH_SIZE
    @JacksonXmlProperty(localName = "DISPLAY_TUN_VOLUME") val display_tun_volume: String?, // DISPLAY_TUN_VOLUME
    @JacksonXmlProperty(localName = "DISPLAY_TUN_WEIGHT") val display_tun_weight: String?, // DISPLAY_TUN_WEIGHT
    @JacksonXmlProperty(localName = "DISPLAY_TOP_UP_WATER") val displayTopUpWater: String?, // DISPLAY_TOP_UP_WATER
    @JacksonXmlProperty(localName = "DISPLAY_TRUB_CHILLER_LOSS") val displayTrubChillerLoss: String?, // DISPLAY_TRUB_CHILLER_LOSS
    @JacksonXmlProperty(localName = "DISPLAY_LAUTER_DEADSPACE") val displayLauterDeadspace: String?, // DISPLAY_LAUTER_DEADSPACE
    @JacksonXmlProperty(localName = "DISPLAY_TOP_UP_KETTLE") val displayTopUpKettle: String?, // DISPLAY_TOP_UP_KETTLE
)

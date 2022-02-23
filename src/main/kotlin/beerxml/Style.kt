package beerxml

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Style(
    @JacksonXmlProperty(localName = "NAME") val name: String, // NAME
    @JacksonXmlProperty(localName = "VERSION") val version: Int, // VERSION
    @JacksonXmlProperty(localName = "CATEGORY") val category: String?, // CATEGORY
    @JacksonXmlProperty(localName = "CATEGORY_NUMBER") val categoryNumber: Int?, // CATEGORY_NUMBER
    @JacksonXmlProperty(localName = "STYLE_LETTER") val styleLetter: String?, // STYLE_LETTER
    @JacksonXmlProperty(localName = "STYLE_GUIDE") val styleGuide: String?, // STYLE_GUIDE
    @JacksonXmlProperty(localName = "TYPE") val type: String?, // TYPE
    @JacksonXmlProperty(localName = "OG_MIN") val minOriginalGravity: Double?, // OG_MIN
    @JacksonXmlProperty(localName = "OG_MAX") val maxOriginalGravity: Double?, // OG_MAX
    @JacksonXmlProperty(localName = "FG_MIN") val minFinalGravity: Double?, // FG_MIN
    @JacksonXmlProperty(localName = "FG_MAX") val maxFinalGravity: Double?, // FG_MAX
    @JacksonXmlProperty(localName = "IBU_MIN") val minIbu: Int?, // IBU_MIN
    @JacksonXmlProperty(localName = "IBU_MAX") val maxIbu: Int?, // IBU_MAX
    @JacksonXmlProperty(localName = "COLOR_MIN") val minColor: Int?, // COLOR_MIN
    @JacksonXmlProperty(localName = "COLOR_MAX") val maxColor: Int?, // COLOR_MAX
    @JacksonXmlProperty(localName = "CARB_MIN") val minCarbonation: Double?, // CARB_MIN
    @JacksonXmlProperty(localName = "CARB_MAX") val maxCarbonation: Double?, // CARB_MAX
    @JacksonXmlProperty(localName = "ABV_MAX") val maxAbv: Double?, // ABV_MAX
    @JacksonXmlProperty(localName = "ABV_MIN") val minAbv: Double?, // ABV_MIN
    @JacksonXmlProperty(localName = "NOTES") val notes: String?, // NOTES
    @JacksonXmlProperty(localName = "PROFILE") val profile: String?, // PROFILE
    @JacksonXmlProperty(localName = "INGREDIENTS") val ingredients: String?, // INGREDIENTS
    @JacksonXmlProperty(localName = "EXAMPLES") val examples: String?, // EXAMPLES
    @JacksonXmlProperty(localName = "DISPLAY_OG_MIN") val minDisplayOriginalGravity: String?, // DISPLAY_OG_MIN
    @JacksonXmlProperty(localName = "DISPLAY_OG_MAX") val maxDisplayOriginalGravity: String?, // DISPLAY_OG_MAX
    @JacksonXmlProperty(localName = "DISPLAY_FG_MIN") val minDisplayFinalGravity: String?, // DISPLAY_FG_MIN
    @JacksonXmlProperty(localName = "DISPLAY_FG_MAX") val maxDisplayFinalGravity: String?, // DISPLAY_FG_MAX
    @JacksonXmlProperty(localName = "DISPLAY_COLOR_MIN") val minDisplayColor: String?, // DISPLAY_COLOR_MIN
    @JacksonXmlProperty(localName = "DISPLAY_COLOR_MAX") val maxDisplayColor: String?, // DISPLAY_COLOR_MAX
    @JacksonXmlProperty(localName = "OG_RANGE") val originalGravityRange: String?, // OG_RANGE
    @JacksonXmlProperty(localName = "FG_RANGE") val finalGravityRange: String?, // FG_RANGE
    @JacksonXmlProperty(localName = "IBU_RANGE") val ibuRange: String?, // IBU_RANGE
    @JacksonXmlProperty(localName = "CARB_RANGE") val carbonationRange: String?, // CARB_RANGE
    @JacksonXmlProperty(localName = "COLOR_RANGE") val colorRange: String?, // COLOR_RANGE
    @JacksonXmlProperty(localName = "ABV_RANGE") val abvRange: String?, // ABV_RANGE


)

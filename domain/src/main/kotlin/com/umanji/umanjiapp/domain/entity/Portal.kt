package com.umanji.umanjiapp.domain.entity

/**
 * Data class for representing a Portal.
 */
data class Portal(
        val id: String = "",
        val politicalType: PoliticalType = PoliticalType.LOCALITY,
        val portalName: String = "",
        val location: Location? = null,
        val adminDistrict: AdminDistrict? = null,
        val address: Address? = null
) : Entity

enum class PoliticalType(val value: String) {
    WORLD("world"),
    COUNTRY("country"),
    LOCALITY("locality"),
    SUB_LOCALITY_1("sublocality1"),
    SUB_LOCALITY_2("sublocality2")
}

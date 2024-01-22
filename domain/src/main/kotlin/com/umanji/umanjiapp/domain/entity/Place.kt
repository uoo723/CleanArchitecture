package com.umanji.umanjiapp.domain.entity

/**
 * data class for representing Place
 */
data class Place(
        val id: String = "",
        val googleId: String = "",
        val name: String = "",
        val type: PlaceType = PlaceType.SPACE,
        val location: Location = Location(0.0, 0.0)
) : Entity

enum class PlaceType(val value: String) {
    SPACE("space"),
    BUILDING("building"),
    MULTI("multi")
}

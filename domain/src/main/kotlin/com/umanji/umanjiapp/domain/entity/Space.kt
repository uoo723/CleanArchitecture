package com.umanji.umanjiapp.domain.entity

/**
 * The data class for representing Space.
 */
data class Space(
        val id: String = "",
        val name: String = "",
        val description: String = "",
        val location: Location = Location(0.0, 0.0),
        val type: SpaceType = SpaceType.INFO_CENTER,
        val images: List<Image> = listOf()
) : Entity

enum class SpaceType {
    INFO_CENTER, GENERAL_BUILDING
}

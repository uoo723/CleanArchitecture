package com.umanji.umanjiapp.domain.entity

/**
 * The data class for representing Channel.
 */
data class Channel(
        val id: String = "",
        val name: String = "",
        val upperChannel: Channel? = null,
        val lowerChannel: Channel? = null,
        val dateTime: EntityDateTime = EntityDateTime(),
        val locations: List<Location> = listOf(),
        val images: List<Image> = listOf(),
        val communities: List<Community> = listOf()
) : Entity

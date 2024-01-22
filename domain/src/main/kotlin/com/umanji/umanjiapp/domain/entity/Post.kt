package com.umanji.umanjiapp.domain.entity

/**
 * Data class for representing a Post.
 */
data class Post(
        val id: String = "",
        val content: String = "",
        val user: User? = null,
        val adminDistrict: AdminDistrict? = null,
        val address: Address? = null,
        val space: Space? = null,
        val place: Place? = null,
        val portal: Portal? = null,
        val viewLevel: Int = 0,
        val createType: String = "",
        val portalLevel: String = "",
        val dateTime: EntityDateTime = EntityDateTime(),
        val channels: List<Channel> = listOf(),
        val images: List<Image> = listOf()
) : Entity

package com.umanji.umanjiapp.domain.entity

/**
 * Data class for representing a User.
 */
data class User(
        val id: String = "",
        val email: String = "",
        val name: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val phone: String = "",
        val money: Double = 0.0,
        val location: Location? = null,
        val dateTime: EntityDateTime = EntityDateTime(),
        val posts: List<Post> = listOf(),
        val communities: List<Community> = listOf(),
        val images: List<Image> = listOf()
) : Entity

package com.umanji.umanjiapp.domain.entity

/**
 * The data class for representing Community.
 */
data class Community(
        val id: String = "",
        val dateTime: EntityDateTime = EntityDateTime(),
        val users: List<User> = listOf(),
        val posts: List<Post> = listOf(),
        val images: List<Image> = listOf(),
        val channels: List<Channel> = listOf()
) : Entity

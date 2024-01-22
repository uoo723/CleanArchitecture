package com.umanji.umanjiapp.domain.entity

/**
 * The data class for representing Image.
 */
data class Image(
        val id: String = "",
        val uri: String = "",
        val dateTime: EntityDateTime = EntityDateTime()
) : Entity

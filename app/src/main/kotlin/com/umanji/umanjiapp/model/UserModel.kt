package com.umanji.umanjiapp.model

import com.umanji.umanjiapp.domain.entity.Entity

/**
 * Data class that represents User model.
 */
data class UserModel(
        val id: String,
        val email: String,
        val userName: String,
        val money: Double,
        val images: List<String>
) : Entity

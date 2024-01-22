package com.umanji.umanjiapp.data.entity

import com.google.gson.annotations.SerializedName
import com.umanji.umanjiapp.domain.entity.Entity
import java.util.*

/**
 * UserEntity used in the data layer.
 */
data class UserEntity(
        val id: String = "",
        val email: String = "",
        val phone: String = "",
        @SerializedName("user_name") val userName: String = "",
        val money: Double = 0.0,
        val photos: List<String> = listOf(),
        val createdAt: Date = Calendar.getInstance().time,
        val updatedAt: Date = Calendar.getInstance().time
) : Entity

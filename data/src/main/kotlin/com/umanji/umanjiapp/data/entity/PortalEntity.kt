package com.umanji.umanjiapp.data.entity

import com.google.gson.annotations.SerializedName
import com.umanji.umanjiapp.domain.entity.Entity

/**
 * PortalEntity used in data layer.
 */
data class PortalEntity(
        val id: String = "",
        @SerializedName("political_type") val politicalType: String = "",
        @SerializedName("portal_name") val portalName: String = "",
        val country: String = "",
        val locality: String = "",
        @SerializedName("sublocality_level_1") val subLocality1: String = "",
        @SerializedName("sublocality_level_2") val subLocality2: String = "",
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
) : Entity

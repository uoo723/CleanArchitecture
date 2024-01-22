package com.umanji.umanjiapp.data.entity

import com.google.gson.annotations.SerializedName
import com.umanji.umanjiapp.domain.entity.Entity
import java.util.*

/**
 * PostEntity used in data layer.
 */
data class PostEntity(
        val id: String = "",
        val content: String = "",
        val country: String = "",
        val countryCode: String = "",
        val locality: String = "",
        val location: LocationEntity = LocationEntity(),
        val premise: String = "",
        @SerializedName("photos") val images: List<String> = listOf(),
        @SerializedName("sublocality_level_1") val subLocalityLevel1: String = "",
        @SerializedName("sublocality_level_2") val subLocalityLevel2: String = "",
        @SerializedName("channel") val channels: List<String> = listOf(),
        @SerializedName("owner_id") val ownerId: String = "",
        @SerializedName("owner_name") val ownerName: String = "",
        @SerializedName("view_level") val viewLevel: String = "",
        @SerializedName("place_name") val placeName: String = "",
        @SerializedName("place_type") val placeType: String = "",
        @SerializedName("place_rid") val placeId: String = "",
        @SerializedName("place_id") val placeGoogleId: String = "",
        @SerializedName("portal_rid") val portalId: String = "",
        @SerializedName("create_type") val createType: String = "",
        @SerializedName("portal_level") val portalLevel: String = "",
        val createdAt: Date = Calendar.getInstance().time,
        val updatedAt: Date = Calendar.getInstance().time
) : Entity

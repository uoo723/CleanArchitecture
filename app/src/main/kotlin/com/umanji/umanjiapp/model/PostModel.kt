package com.umanji.umanjiapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.umanji.umanjiapp.domain.entity.Entity
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Data class that represents Post model.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class PostModel(
        val id: String,
        val content: String,
        val locality: String,
        val subLocality1: String,
        val subLocality2: String,
        val premise: String,
        val placeType: String,
        val createType: String,
        val channels: List<String>,
        val userName: String,
        val userId: String,
        val placeName: String,
        val placeId: String,
        val placeGoogleId: String,
        val portalId: String,
        val latitude: Double,
        val longitude: Double,
        val viewLevel: Int,
        val images: List<String>,
        val createdAt: Date,
        val updatedAt: Date
) : Entity, Parcelable

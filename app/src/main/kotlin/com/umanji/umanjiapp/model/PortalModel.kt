package com.umanji.umanjiapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.umanji.umanjiapp.domain.entity.Entity
import kotlinx.android.parcel.Parcelize

/**
 * Data class that represents Portal model.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class PortalModel(
        val id: String,
        val portalName: String,
        val country: String,
        val locality: String,
        val subLocality1: String,
        val subLocality2: String,
        val latitude: Double,
        val longitude: Double
) : Entity, Parcelable

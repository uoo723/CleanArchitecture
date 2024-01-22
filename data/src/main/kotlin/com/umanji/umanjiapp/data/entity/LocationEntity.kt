package com.umanji.umanjiapp.data.entity

import com.google.gson.annotations.SerializedName
import com.umanji.umanjiapp.domain.entity.Entity


/**
 * LocationEntity used in data layer.
 */
data class LocationEntity(
        @SerializedName("@class") val `class`: String = "OPoint",
        val coordinates: List<Double> = listOf()
) : Entity

package com.umanji.umanjiapp.domain.entity

/**
 * Data class for representing a Location with WGS84 (EPSG:4326).
 *
 * WGS84: [https://en.wikipedia.org/wiki/World_Geodetic_System#A_new_World_Geodetic_System:_WGS_84]
 */
data class Location(
        val latitude: Double,
        val longitude: Double
) : Entity

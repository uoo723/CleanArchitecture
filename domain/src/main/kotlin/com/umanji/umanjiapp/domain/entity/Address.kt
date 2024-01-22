package com.umanji.umanjiapp.domain.entity

/**
 * The data class for representing address (지번) and/or road address (도로명).
 *
 * It must be set at least either address or roadAddress.
 */
data class Address(
        val address: String?,
        val roadAddress: String?
) : Entity

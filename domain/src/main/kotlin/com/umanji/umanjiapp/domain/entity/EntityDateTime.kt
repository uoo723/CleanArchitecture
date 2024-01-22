package com.umanji.umanjiapp.domain.entity

import java.util.*

/**
 * The date class that represents entity created time and updated time.
 */
data class EntityDateTime(
        val createdAt: Date = Calendar.getInstance().time,
        val updatedAt: Date = Calendar.getInstance().time
) : Entity

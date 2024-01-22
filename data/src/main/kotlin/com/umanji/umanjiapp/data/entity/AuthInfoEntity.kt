package com.umanji.umanjiapp.data.entity

import com.umanji.umanjiapp.domain.entity.Entity

/**
 * AuthInfoEntity used in the data layer.
 */
data class AuthInfoEntity(
        @Transient val id: String? = null,
        @Transient val password: String? = null,
        val token: String? = null,
        val user: UserEntity? = null
) : Entity

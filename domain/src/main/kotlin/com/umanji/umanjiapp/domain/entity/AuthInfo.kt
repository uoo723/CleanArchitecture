package com.umanji.umanjiapp.domain.entity

/**
 * Data class for representing authentication info
 */
data class AuthInfo(
        val id: String? = null,             // input
        val password: String? = null,       // input
        val token: String? = null,          // input/output
        val user: User? = null
) : Entity

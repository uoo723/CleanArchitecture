package com.umanji.umanjiapp.data.network

/**
 * Data class that represents json object retrieved from API server.
 */
data class ApiResponse<out T>(
        val data: T,
        val code: String? = null,
        val message: String? = null,
        val parent: String? = null
)

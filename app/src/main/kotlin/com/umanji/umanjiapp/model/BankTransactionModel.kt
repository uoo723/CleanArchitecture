package com.umanji.umanjiapp.model

import com.umanji.umanjiapp.domain.entity.Entity
import java.util.*

/**
 * Data class that represents BankTransaction model.
 */
data class BankTransactionModel(
        val id: String,
        val senderId: String,
        val senderName: String,
        val senderPhoto: String,
        val receiverId: String,
        val receiverName: String,
        val receiverPhoto: String,
        val description: String?,
        val amount: Double,
        val createdAt: Date,
        val updatedAt: Date
) : Entity

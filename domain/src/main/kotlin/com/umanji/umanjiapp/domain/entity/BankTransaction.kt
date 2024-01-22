package com.umanji.umanjiapp.domain.entity

/**
 * The data class for representing bank transaction.
 */
data class BankTransaction(
        val id: String = "",
        val senderId: String = "",
        val senderName: String = "",
        val senderPhoto: Image = Image(),
        val receiverId: String = "",
        val receiverName: String = "",
        val receiverPhoto: Image = Image(),
        val description: String? = null,
        val amount: Double = 0.0,
        val dateTime: EntityDateTime = EntityDateTime()
) : Entity

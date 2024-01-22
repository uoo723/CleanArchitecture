package com.umanji.umanjiapp.data.entity

import com.google.gson.annotations.SerializedName
import com.umanji.umanjiapp.domain.entity.Entity
import java.util.*

/**
 * BankTransactionEntity used in the data layer.
 */
data class BankTransactionEntity(
        val id: String = "",
        @SerializedName("send_user_id") val senderId: String = "",
        @SerializedName("send_user_name") val senderName: String = "",
        @SerializedName("send_user_photo") val senderPhoto: String = "",
        @SerializedName("receive_user_id") val receiverId: String = "",
        @SerializedName("receive_user_name") val receiverName: String = "",
        @SerializedName("receive_user_photo") val receiverPhoto: String = "",
        val description: String = "",
        val amount: Double = 0.0,
        val createdAt: Date = Calendar.getInstance().time,
        val updatedAt: Date = Calendar.getInstance().time
) : Entity

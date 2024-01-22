package com.umanji.umanjiapp.model.mapper

import com.umanji.umanjiapp.domain.entity.BankTransaction
import com.umanji.umanjiapp.model.BankTransactionModel

/**
 * Mapper for [BankTransactionModel] and [BankTransaction].
 */
object BankTransactionModelMapper : ModelMapper<BankTransaction, BankTransactionModel> {

    override fun transform(domainEntity: BankTransaction): BankTransactionModel {
        return BankTransactionModel(
                id = domainEntity.id,
                senderId = domainEntity.senderId,
                senderName = domainEntity.senderName,
                senderPhoto = domainEntity.senderPhoto.uri,
                receiverId = domainEntity.receiverId,
                receiverName = domainEntity.receiverName,
                receiverPhoto = domainEntity.receiverPhoto.uri,
                description = domainEntity.description,
                amount = domainEntity.amount,
                createdAt = domainEntity.dateTime.createdAt,
                updatedAt = domainEntity.dateTime.updatedAt
        )
    }
}

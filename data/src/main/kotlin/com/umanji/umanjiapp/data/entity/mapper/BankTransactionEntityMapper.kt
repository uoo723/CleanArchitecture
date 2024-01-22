package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.BankTransactionEntity
import com.umanji.umanjiapp.domain.entity.BankTransaction
import com.umanji.umanjiapp.domain.entity.EntityDateTime
import com.umanji.umanjiapp.domain.entity.Image

/**
 * Mapper for [BankTransaction] and [BankTransactionEntity].
 */
internal object BankTransactionEntityMapper : EntityMapper<BankTransactionEntity, BankTransaction> {

    override fun transformDomain(dataEntity: BankTransactionEntity): BankTransaction {
        return BankTransaction(
                id = dataEntity.id,
                senderId = dataEntity.senderId,
                senderName = dataEntity.senderName,
                senderPhoto = Image(uri = dataEntity.senderPhoto),
                receiverId = dataEntity.receiverId,
                receiverName = dataEntity.receiverName,
                receiverPhoto = Image(uri = dataEntity.receiverPhoto),
                description = dataEntity.description,
                amount = dataEntity.amount,
                dateTime = EntityDateTime(dataEntity.createdAt, dataEntity.updatedAt)
        )
    }

    override fun transformData(domainEntity: BankTransaction): BankTransactionEntity {
        return BankTransactionEntity(
                id = domainEntity.senderId,
                senderId = domainEntity.senderId,
                senderName = domainEntity.senderName,
                senderPhoto = domainEntity.senderPhoto.uri,
                receiverId = domainEntity.receiverId,
                receiverName = domainEntity.receiverName,
                receiverPhoto = domainEntity.receiverPhoto.uri,
                description = domainEntity.description ?: "",
                amount = domainEntity.amount,
                createdAt = domainEntity.dateTime.createdAt,
                updatedAt = domainEntity.dateTime.updatedAt
        )
    }
}

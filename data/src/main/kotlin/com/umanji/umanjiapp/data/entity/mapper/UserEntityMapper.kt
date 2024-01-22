package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.UserEntity
import com.umanji.umanjiapp.domain.entity.EntityDateTime
import com.umanji.umanjiapp.domain.entity.Image
import com.umanji.umanjiapp.domain.entity.User


/**
 * Mapper for [User] and [UserEntity].
 */
internal object UserEntityMapper : EntityMapper<UserEntity, User> {
    override fun transformDomain(dataEntity: UserEntity): User {
        return User(
                id = dataEntity.id,
                email = dataEntity.email,
                name = dataEntity.userName,
                phone = dataEntity.phone,
                money = dataEntity.money,
                images = dataEntity.photos.map { Image(uri = it) },
                dateTime = EntityDateTime(dataEntity.createdAt, dataEntity.updatedAt)
        )
    }

    override fun transformData(domainEntity: User): UserEntity {
        return UserEntity(
                id = domainEntity.id,
                email = domainEntity.email,
                phone = domainEntity.phone,
                userName = domainEntity.name,
                money = domainEntity.money,
                photos = domainEntity.images.map { it.uri },
                createdAt = domainEntity.dateTime.createdAt,
                updatedAt = domainEntity.dateTime.updatedAt
        )
    }
}

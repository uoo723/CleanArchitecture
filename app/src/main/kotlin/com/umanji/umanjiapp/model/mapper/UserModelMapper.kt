package com.umanji.umanjiapp.model.mapper

import com.umanji.umanjiapp.domain.entity.User
import com.umanji.umanjiapp.model.UserModel

/**
 * Mapper for [UserModel] and [User].
 */
object UserModelMapper : ModelMapper<User, UserModel> {

    override fun transform(domainEntity: User): UserModel {
        return UserModel(
                id = domainEntity.id,
                email = domainEntity.email,
                userName = domainEntity.name,
                money = domainEntity.money,
                images = domainEntity.images.map { it.uri }
        )
    }
}

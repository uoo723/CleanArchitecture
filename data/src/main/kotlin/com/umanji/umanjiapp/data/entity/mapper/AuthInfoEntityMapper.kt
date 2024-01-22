package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.AuthInfoEntity
import com.umanji.umanjiapp.domain.entity.AuthInfo

/**
 * Mapper for [AuthInfo] and [AuthInfoEntity].
 */
object AuthInfoEntityMapper : EntityMapper<AuthInfoEntity, AuthInfo> {

    override fun transformDomain(dataEntity: AuthInfoEntity): AuthInfo {
        return AuthInfo(
                id = dataEntity.id,
                password = dataEntity.password,
                token = dataEntity.token,
                user = dataEntity.user?.let { UserEntityMapper.transformDomain(it) }
        )
    }

    override fun transformData(domainEntity: AuthInfo): AuthInfoEntity {
        return AuthInfoEntity(
                id = domainEntity.id,
                password = domainEntity.password,
                token = domainEntity.token,
                user = domainEntity.user?.let { UserEntityMapper.transformData(it) }
        )
    }
}

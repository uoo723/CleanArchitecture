package com.umanji.umanjiapp.data.entity.mapper

import com.umanji.umanjiapp.data.entity.GroupEntity
import com.umanji.umanjiapp.domain.entity.Group

/**
 * Created by paulhwang on 18/02/2018.
 */

/**
 * Mapper for [Group] and [GroupEntity].
 */
internal object GroupEntityMapper : EntityMapper<GroupEntity, Group> {
    override fun transformDomain(dataEntity: GroupEntity): Group {
        return Group(
                group_name = dataEntity.group_name
        )
    }

    override fun transformData(domainEntity: Group): GroupEntity {
        return GroupEntity(
                group_name = domainEntity.group_name
        )
    }
}

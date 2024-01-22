package com.umanji.umanjiapp.model.mapper

import com.umanji.umanjiapp.domain.entity.Group
import com.umanji.umanjiapp.model.GroupModel

/**
 * Mapper for [Group] and [GroupModel].
 */
object GroupModelMapper : ModelMapper<Group, GroupModel> {

    override fun transform(domainEntity: Group): GroupModel {
        return GroupModel(
                group_name = domainEntity.group_name
        )
    }
}
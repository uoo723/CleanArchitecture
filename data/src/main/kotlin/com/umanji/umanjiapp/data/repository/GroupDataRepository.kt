package com.umanji.umanjiapp.data.repository

import com.umanji.umanjiapp.data.entity.mapper.GroupEntityMapper
import com.umanji.umanjiapp.data.repository.datasource.group.CloudGroupDataStore
import com.umanji.umanjiapp.domain.entity.Group
import com.umanji.umanjiapp.domain.repository.GroupRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [GroupRepository] for retrieving user data.
 */
@Singleton
class GroupDataRepository @Inject constructor(
        private val cloudGroupDataStore: CloudGroupDataStore
//        private val localUserDataStore: LocalUserDataStore
) : GroupRepository {

    override fun createGroup(group_name: Group): Completable {
        return cloudGroupDataStore.createGroup(GroupEntityMapper.transformData(group_name))
    }
}

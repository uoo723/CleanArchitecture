package com.umanji.umanjiapp.data.repository.datasource.group

import com.umanji.umanjiapp.data.entity.GroupEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface that represents a data store from where user data is retrieved.
 */
interface GroupDataStore {

    /**
     * Get an [Single] which will emit a [GroupEntity].
     */
    fun createGroup(group: GroupEntity): Completable
}

package com.umanji.umanjiapp.domain.repository

import com.umanji.umanjiapp.domain.entity.Group
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by paulhwang on 18/02/2018.
 */

interface GroupRepository {
    /**
     * Get a [Single] which will emit a [Group].
     */
    fun createGroup(group_name: Group) : Completable

//    fun createGroup(group_name: String): Single<Group>
}
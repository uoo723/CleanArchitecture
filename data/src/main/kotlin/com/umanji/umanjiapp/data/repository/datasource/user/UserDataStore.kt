package com.umanji.umanjiapp.data.repository.datasource.user

import com.umanji.umanjiapp.data.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Interface that represents a data store from where user data is retrieved.
 */
interface UserDataStore {

    /**
     * Get an [Single] which will emit a [UserEntity].
     */
    fun me(): Single<UserEntity>

    fun getUser(phoneNumber: String): Single<UserEntity>

    fun saveMe(userEntity: UserEntity): Completable

    fun deleteMe(): Completable
}

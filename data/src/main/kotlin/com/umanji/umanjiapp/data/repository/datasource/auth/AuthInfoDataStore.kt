package com.umanji.umanjiapp.data.repository.datasource.auth

import com.umanji.umanjiapp.data.entity.AuthInfoEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface that represents a data store from where auth info is retrieved.
 */
internal interface AuthInfoDataStore {

    fun authInfo(): Single<AuthInfoEntity>

    fun createAuthInfo(authInfo: AuthInfoEntity): Single<AuthInfoEntity>

    fun saveAuthInfo(authInfoEntity: AuthInfoEntity): Completable

    fun deleteAuthInfo(): Completable
}

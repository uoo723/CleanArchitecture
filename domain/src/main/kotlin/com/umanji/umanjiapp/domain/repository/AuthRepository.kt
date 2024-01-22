package com.umanji.umanjiapp.domain.repository

import com.umanji.umanjiapp.domain.entity.AuthInfo
import io.reactivex.Completable
import io.reactivex.Single


/**
 * Interface that represents a Repository for [AuthInfo].
 */
interface AuthRepository {

    /**
     * Get a [Single] which will emit a [AuthInfo].
     */
    fun authInfo(): Single<AuthInfo>

    /**
     * Get a [Single] which will be emit a fresh [AuthInfo].
     */
    fun checkAutInfo(): Single<AuthInfo>

    /**
     * Get a [Single] which will emit a pair of [AuthInfo].
     */
    fun createAuthInfo(authInfo: AuthInfo): Single<AuthInfo>

    /**
     * Get a [Completable] which will be completed if saving [AuthInfo] is successful.
     */
    fun saveAuthInfo(authInfo: AuthInfo): Completable

    /**
     * Get a [Completable] which will be completed if deleting [AuthInfo] is successful.
     */
    fun deleteAuthInfo(): Completable
}

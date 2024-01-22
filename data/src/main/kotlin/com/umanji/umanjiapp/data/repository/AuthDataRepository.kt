package com.umanji.umanjiapp.data.repository

import com.umanji.umanjiapp.data.entity.mapper.AuthInfoEntityMapper
import com.umanji.umanjiapp.data.network.ApiHeaders
import com.umanji.umanjiapp.data.network.RestApi
import com.umanji.umanjiapp.data.repository.datasource.auth.CloudAuthInfoDataStore
import com.umanji.umanjiapp.data.repository.datasource.auth.LocalAuthInfoDataStore
import com.umanji.umanjiapp.domain.entity.AuthInfo
import com.umanji.umanjiapp.domain.repository.AuthRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [AuthRepository] for retrieving auth info.
 */
@Singleton
class AuthDataRepository @Inject constructor(
        private val cloudAuthInfoDataStore: CloudAuthInfoDataStore,
        private val localAuthInfoDataStore: LocalAuthInfoDataStore,
        private val restApi: RestApi,
        private val apiHeaders: ApiHeaders
) : AuthRepository {

    override fun authInfo(): Single<AuthInfo> {
        return localAuthInfoDataStore.authInfo().map(AuthInfoEntityMapper::transformDomain)
    }

    override fun checkAutInfo(): Single<AuthInfo> {
        return localAuthInfoDataStore.authInfo()
                .flatMap {
                    restApi.checkToken(it.token!!).map {
                        AuthInfoEntityMapper.transformDomain(it.data)
                    }
                }
                .flatMap {
                    apiHeaders.authToken = it.token
                    Single.just(it)
                }
    }

    override fun createAuthInfo(authInfo: AuthInfo): Single<AuthInfo> {
        return cloudAuthInfoDataStore.createAuthInfo(AuthInfoEntityMapper.transformData(authInfo))
                .map(AuthInfoEntityMapper::transformDomain)
    }

    override fun saveAuthInfo(authInfo: AuthInfo): Completable {
        return localAuthInfoDataStore.saveAuthInfo(AuthInfoEntityMapper.transformData(authInfo))
    }

    override fun deleteAuthInfo(): Completable {
        return cloudAuthInfoDataStore.deleteAuthInfo()
                .andThen(localAuthInfoDataStore.deleteAuthInfo())
    }
}

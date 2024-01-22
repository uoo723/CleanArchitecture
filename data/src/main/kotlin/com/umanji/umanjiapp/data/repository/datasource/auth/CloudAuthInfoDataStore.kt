package com.umanji.umanjiapp.data.repository.datasource.auth

import com.umanji.umanjiapp.data.entity.AuthInfoEntity
import com.umanji.umanjiapp.data.exception.CannotCalled
import com.umanji.umanjiapp.data.exception.Required
import com.umanji.umanjiapp.data.network.RestApi
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [AuthInfoDataStore] implementation based on connections to the api.
 */
@Singleton
open class CloudAuthInfoDataStore @Inject constructor(
        private val restApi: RestApi
) : AuthInfoDataStore {

    override fun authInfo(): Single<AuthInfoEntity> {
        throw CannotCalled()
    }

    override fun createAuthInfo(authInfo: AuthInfoEntity): Single<AuthInfoEntity> {
        val id = authInfo.id ?: return Single.error(Required("id is required"))
        val password = authInfo.password ?:
                return Single.error(Required("password is required"))

        return if (authInfo.user == null) {
            restApi.signIn(id, password).map { it.data }
        } else {
            val user = authInfo.user
            restApi.signUp(user.email, password, user.phone, user.userName).map { it.data }
        }
    }

    override fun saveAuthInfo(authInfoEntity: AuthInfoEntity): Completable {
        throw CannotCalled()
    }

    override fun deleteAuthInfo(): Completable {
//        return restApi.logout()   // TODO: return restApi.logout()
        return Completable.complete()
    }
}

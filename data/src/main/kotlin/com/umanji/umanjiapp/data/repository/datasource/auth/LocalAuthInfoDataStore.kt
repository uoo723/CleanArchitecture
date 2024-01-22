package com.umanji.umanjiapp.data.repository.datasource.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.umanji.umanjiapp.data.entity.AuthInfoEntity
import com.umanji.umanjiapp.data.exception.CannotCalled
import com.umanji.umanjiapp.data.exception.NoValue
import com.umanji.umanjiapp.data.network.ApiHeaders
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [AuthInfoDataStore] implementation based on local storage.
 */
@Singleton
open class LocalAuthInfoDataStore @Inject constructor(
        context: Context,
        private val apiHeaders: ApiHeaders
) : AuthInfoDataStore {

    private val sharedPrefs: SharedPreferences =
            context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    override fun authInfo(): Single<AuthInfoEntity> {
        val token: String = sharedPrefs.getString("token", null)
                ?: return Single.error(NoValue("no token"))
        return Single.just(AuthInfoEntity(token = token))
    }

    override fun createAuthInfo(authInfo: AuthInfoEntity): Single<AuthInfoEntity> {
        throw CannotCalled()
    }

    @SuppressLint("ApplySharedPref")
    override fun saveAuthInfo(authInfoEntity: AuthInfoEntity): Completable {
        val token: String = authInfoEntity.token
                ?: return Completable.error(NoValue("no token"))
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        return Completable.fromAction {
            editor.putString("token", token)
            editor.commit()
        }
    }

    @SuppressLint("ApplySharedPref")
    override fun deleteAuthInfo(): Completable {
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        return Completable.fromAction {
            apiHeaders.authToken = null
            editor.remove("token")
            editor.commit()
        }
    }
}

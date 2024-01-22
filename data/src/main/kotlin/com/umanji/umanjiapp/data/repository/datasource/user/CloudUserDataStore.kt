package com.umanji.umanjiapp.data.repository.datasource.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.umanji.umanjiapp.data.entity.UserEntity
import com.umanji.umanjiapp.data.exception.CannotCalled
import com.umanji.umanjiapp.data.exception.NoValue
import com.umanji.umanjiapp.data.network.RestApi
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [UserDataStore] implementation based on connections to the api.
 *
 * @constructor A [UserDataStore] based on connections to the api (Cloud).
 * @property restApi The [RestApi] implementation to use.
 */
@Singleton
open class CloudUserDataStore @Inject constructor(
        context: Context,
        private val restApi: RestApi,
        private val gson: Gson
) : UserDataStore {

    private val sharedPrefs: SharedPreferences =
            context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    @SuppressLint("ApplySharedPref")
    override fun me(): Single<UserEntity> {
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        val token: String = sharedPrefs.getString("token", null)
                ?: return Single.error(NoValue("no token"))
        return restApi.checkToken(token).map {
            val userJson: String = gson.toJson(it.data.user)
            editor.putString("user", userJson)
            editor.commit()
            it.data.user
        }
    }

    override fun getUser(phoneNumber: String): Single<UserEntity> {
        return restApi.getUser(phoneNumber).map { it.data }
    }

    override fun saveMe(userEntity: UserEntity): Completable {
        throw CannotCalled()
    }

    override fun deleteMe(): Completable {
        throw CannotCalled()
    }
}

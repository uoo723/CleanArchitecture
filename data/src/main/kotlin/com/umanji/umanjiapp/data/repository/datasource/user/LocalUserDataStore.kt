package com.umanji.umanjiapp.data.repository.datasource.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.umanji.umanjiapp.data.entity.UserEntity
import com.umanji.umanjiapp.data.exception.CannotCalled
import com.umanji.umanjiapp.data.exception.NoValue
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


/**
 * [UserDataStore] implementation based on local storage.
 */
@Singleton
open class LocalUserDataStore @Inject constructor(
        context: Context,
        private val gson: Gson
) : UserDataStore {

    private val sharedPrefs: SharedPreferences =
            context.getSharedPreferences("user", Context.MODE_PRIVATE)

    override fun me(): Single<UserEntity> {
        val userJson: String = sharedPrefs.getString("user", null)
                ?: return Single.error(NoValue("no user"))
        return Single.just(gson.fromJson(userJson, UserEntity::class.java))
    }

    override fun getUser(phoneNumber: String): Single<UserEntity> {
        throw CannotCalled()
    }

    @SuppressLint("ApplySharedPref")
    override fun saveMe(userEntity: UserEntity): Completable {
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        return Completable.fromAction {
            val userJson: String = gson.toJson(userEntity)
            editor.putString("user", userJson)
            editor.commit()
        }
    }

    @SuppressLint("ApplySharedPref")
    override fun deleteMe(): Completable {
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        return Completable.fromAction {
            editor.remove("user")
            editor.commit()
        }
    }
}

package com.umanji.umanjiapp.data.repository

import com.umanji.umanjiapp.data.entity.mapper.UserEntityMapper
import com.umanji.umanjiapp.data.repository.datasource.user.CloudUserDataStore
import com.umanji.umanjiapp.data.repository.datasource.user.LocalUserDataStore
import com.umanji.umanjiapp.domain.entity.User
import com.umanji.umanjiapp.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [UserRepository] for retrieving user data.
 */
@Singleton
class UserDataRepository @Inject constructor(
        private val cloudUserDataStore: CloudUserDataStore,
        private val localUserDataStore: LocalUserDataStore
) : UserRepository {

    override fun me(force: Boolean): Single<User> {
        return if (force) {
            cloudUserDataStore.me().map(UserEntityMapper::transformDomain)
        } else {
            localUserDataStore.me().map(UserEntityMapper::transformDomain)
        }
    }

    override fun getUser(phoneNumber: String): Single<User> {
        return cloudUserDataStore.getUser(phoneNumber).map(UserEntityMapper::transformDomain)
    }

    override fun saveMe(user: User): Completable {
        return localUserDataStore.saveMe(UserEntityMapper.transformData(user))
    }

    override fun deleteMe(): Completable {
        return localUserDataStore.deleteMe()
    }
}

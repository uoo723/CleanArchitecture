package com.umanji.umanjiapp.data.repository

import com.umanji.umanjiapp.data.entity.UserEntity
import com.umanji.umanjiapp.data.repository.datasource.user.CloudUserDataStore
import com.umanji.umanjiapp.data.repository.datasource.user.LocalUserDataStore
import io.reactivex.Single
import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*


class UserDataRepositoryTest : Spek({

    lateinit var userDataRepository: UserDataRepository

    val currentDate = Calendar.getInstance().time!!
    val userEntity = UserEntity(
            id = "123",
            email = "example@example.com",
            phone = "01012345678",
            userName = "test user",
            createdAt = currentDate,
            updatedAt = currentDate
    )

    val mockCloudUserDataStore = mock(CloudUserDataStore::class)
    val mockLocalUserDataStore = mock(LocalUserDataStore::class)
    When calling mockLocalUserDataStore.me() itReturns Single.just(userEntity)

    beforeEachTest {
        userDataRepository = UserDataRepository(mockCloudUserDataStore, mockLocalUserDataStore)
    }

    on("which userDataRepository.me() is called") {
        userDataRepository.me()
        it("must be called mockUserDataStore.me()") {
            Verify on mockLocalUserDataStore that mockLocalUserDataStore.me() was called
        }
    }
})

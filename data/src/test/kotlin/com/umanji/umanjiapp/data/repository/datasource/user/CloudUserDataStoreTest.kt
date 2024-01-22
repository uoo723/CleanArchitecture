package com.umanji.umanjiapp.data.repository.datasource.user

import com.nhaarman.mockitokotlin2.any
import com.umanji.umanjiapp.data.entity.UserEntity
import com.umanji.umanjiapp.data.network.RestApi
import io.reactivex.Completable
import io.reactivex.Single
import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.dsl.xgiven


class CloudUserDataStoreTest : Spek({
    lateinit var cloudUserDataStore: CloudUserDataStore

    val mockRestApi = mock(RestApi::class)

//    When calling mockRestApi.signIn(any(), any()) itReturns Completable.complete()
//    When calling mockRestApi.me() itReturns Single.just(UserEntity())

    xgiven("Test CloudUserDataStore") {
        beforeEachTest {
            cloudUserDataStore = CloudUserDataStore(mockRestApi)
        }

        on("Test CloudUserDataStore#me without credential") {
            cloudUserDataStore.me()
            it("No interactions on RestApi because no credential") {
                VerifyNoInteractions on mockRestApi
            }
        }

        on("Test CloudUserDataStore#me with credential") {
//            cloudUserDataStore.setCredential("test@exmaple.com", "1234")
            cloudUserDataStore.me()
            it("RestApi#signIn must be called") {
                Verify on mockRestApi that mockRestApi.signIn(any(), any()) was called
            }

            it("RestApi#me must be called") {
//                Verify on mockRestApi that mockRestApi.me() was called
            }
        }
    }
})

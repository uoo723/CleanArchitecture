package com.umanji.umanjiapp.data.repository.datasource.post

import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.umanji.umanjiapp.data.entity.PostEntity
import com.umanji.umanjiapp.data.network.ApiResponse
import com.umanji.umanjiapp.data.network.RestApi
import com.umanji.umanjiapp.data.util.MediaUtils
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class CloudPostDataStoreTest : Spek({
    lateinit var cloudPostDataStore: CloudPostDataStore

    val fakePosts = listOf(
            PostEntity(id = "1"),
            PostEntity(id = "2")
    )
    val fakeApiResponse = ApiResponse(data = fakePosts)

    val mockRestApi: RestApi = mock()
    val mockGson: Gson = mock()
    val mockMediaUtils: MediaUtils = mock()
    lateinit var testObserver: TestObserver<List<PostEntity>>

    When calling mockRestApi.posts(any(), any()) itReturns Single.just(fakeApiResponse)
    beforeEachTest {
        cloudPostDataStore = CloudPostDataStore(mockRestApi, mockGson, mockMediaUtils)
    }

    on("which CloudPostDataStore.me() is successful") {
        testObserver = cloudPostDataStore.posts(any(), any(), any(), any(), any(), any()).test()

        it("must emit a list a PostEntity") {
            testObserver.assertValue(fakePosts)
        }
    }
})

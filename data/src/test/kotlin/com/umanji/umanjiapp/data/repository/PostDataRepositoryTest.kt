package com.umanji.umanjiapp.data.repository

import com.nhaarman.mockitokotlin2.any
import com.umanji.umanjiapp.data.entity.PostEntity
import com.umanji.umanjiapp.data.entity.mapper.PostEntityMapper
import com.umanji.umanjiapp.data.repository.datasource.post.CloudPostDataStore
import com.umanji.umanjiapp.domain.entity.Post
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class PostDataRepositoryTest : Spek({

    lateinit var postDataRepository: PostDataRepository

    val fakePostEntities = listOf(
            PostEntity(id = "1"),
            PostEntity(id = "2")
    )
    val fakePosts = fakePostEntities.map(PostEntityMapper::transformDomain)

    val mockPostCloudDataStore = mock(CloudPostDataStore::class)
    lateinit var testObserver: TestObserver<List<Post>>

    When calling mockPostCloudDataStore.posts(any(), any(), any(), any(), any(), any()) itReturns Single.just(fakePostEntities)

    beforeEachTest {
        postDataRepository = PostDataRepository(mockPostCloudDataStore)
    }

    on("which PostDataRepository.posts() is successful") {
        testObserver = postDataRepository.posts(any(), any(), any()).test()

        it("must emit a fakePosts") {
            testObserver.assertValue {
                it[0].id == fakePosts[0].id
            }
        }
    }
})

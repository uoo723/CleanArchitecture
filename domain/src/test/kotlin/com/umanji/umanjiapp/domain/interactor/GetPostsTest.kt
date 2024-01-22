package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.Post
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.PostRepository
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class GetPostsTest : Spek({
    lateinit var getPosts: GetPosts

    val fakePostList = listOf(
            Post(
                    id = "1",
                    content = "content1"
            ),
            Post(
                    id = "2",
                    content = "content2"
            )
    )

    val fakeParam = GetPosts.Params(
            offset = 0,
            limit = 10
    )

    val mockThreadExecutor = mock(ThreadExecutor::class)
    val mockPostExecutionThread = mock(PostExecutionThread::class)
    val mockPostRepository = mock(PostRepository::class)
    lateinit var testObserver: TestObserver<List<Post>>

    beforeEachTest {
        getPosts = GetPosts(mockThreadExecutor, mockPostExecutionThread, mockPostRepository)
    }

    given("that getting a list of post is successful") {
        on("which param is not provided") {
            When calling mockPostRepository.posts() itReturns Single.just(fakePostList)
            testObserver = getPosts.buildUseCaseObservable(null).test()

            it("must emit a list of post") {
                testObserver.assertValue(fakePostList)
            }
        }

        on("which param is provided") {
            (When calling mockPostRepository.posts(fakeParam.offset, fakeParam.limit)
                    itReturns Single.just(fakePostList))
            testObserver = getPosts.buildUseCaseObservable(fakeParam).test()

            it("must emit a list of post") {
                testObserver.assertValue(fakePostList)
            }
        }
    }

    on("which getting a list of post is failed") {
        val exception = Exception("Error occurred")
        When calling mockPostRepository.posts() itReturns Single.error(exception)
        testObserver = getPosts.buildUseCaseObservable(null).test()

        it("must emit a Exception") {
            testObserver.assertError(exception)
        }
    }
})

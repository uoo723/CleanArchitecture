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
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class GetPostTest : Spek({
    lateinit var getPost: GetPost

    val fakePost = Post(
            id = "1",
            content = "content"
    )

    val mockThreadExecutor = mock(ThreadExecutor::class)
    val mockPostExecutionThread = mock(PostExecutionThread::class)
    val mockPostRepository = mock(PostRepository::class)
    lateinit var testObserver: TestObserver<Post>
    val exception = Exception("No Post")

    When calling mockPostRepository.post("1") itReturns Single.just(fakePost)
    When calling mockPostRepository.post("2") itReturns Single.error(exception)

    beforeEachTest {
        getPost = GetPost(mockThreadExecutor, mockPostExecutionThread, mockPostRepository)
    }

    on("which post is existed") {
        testObserver = getPost.buildUseCaseObservable("1").test()

        it("must emit a Post") {
            testObserver.assertValue(fakePost)
        }
    }

    on("which post is note existed") {
        testObserver = getPost.buildUseCaseObservable("2").test()

        it("must emit a Exception") {
            testObserver.assertError(exception)
        }
    }
})

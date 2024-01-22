package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.Post
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.PostRepository
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class UpdatePostTest : Spek({
    lateinit var updatePost: UpdatePost

    val fakePost = Post(
            id = "1",
            content = "content"
    )

    val mockThreadExecutor = mock(ThreadExecutor::class)
    val mockPostExecutionThread = mock(PostExecutionThread::class)
    val mockPostRepository = mock(PostRepository::class)
    lateinit var testObserver: TestObserver<Any>

    beforeEachTest {
        updatePost = UpdatePost(mockThreadExecutor, mockPostExecutionThread, mockPostRepository)
    }

    on("which post is successfully updated") {
        When calling mockPostRepository.updatePost(fakePost) itReturns Completable.complete()

        testObserver = updatePost.buildUseCaseObservable(fakePost).test()

        it("must be completed") {
            testObserver.assertComplete()
        }
    }

    on("which post is not updated") {
        val exception = Exception("failed updating post")
        When calling mockPostRepository.updatePost(fakePost) itReturns Completable.error(exception)

        testObserver = updatePost.buildUseCaseObservable(fakePost).test()

        it("must be emit a Exception") {
            testObserver.assertError(exception)
        }
    }
})

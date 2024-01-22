package com.umanji.umanjiapp.domain.interactor

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


class DeletePostTest : Spek({
    lateinit var deletePost: DeletePost

    val fakeId = "1"

    val mockThreadExecutor = mock(ThreadExecutor::class)
    val mockPostExecutionThread = mock(PostExecutionThread::class)
    val mockPostRepository = mock(PostRepository::class)
    lateinit var testObserver: TestObserver<Any>

    beforeEachTest {
        deletePost = DeletePost(mockThreadExecutor, mockPostExecutionThread, mockPostRepository)
    }

    on("which post is successfully deleted") {
        When calling mockPostRepository.deletePost(fakeId) itReturns Completable.complete()

        testObserver = deletePost.buildUseCaseObservable(fakeId).test()

        it("must be completed") {
            testObserver.assertComplete()
        }
    }

    on("which post is not deleted") {
        val exception = Exception("failed deleting post")
        When calling mockPostRepository.deletePost(fakeId) itReturns Completable.error(exception)

        testObserver = deletePost.buildUseCaseObservable(fakeId).test()

        it("must be emit a Exception") {
            testObserver.assertError(exception)
        }
    }
})

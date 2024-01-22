package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.User
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.UserRepository
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class GetMeTest : Spek({
    lateinit var getMe: GetMe

    val fakeUser = User(
            id = "1",
            email = "test@example.com",
            name = "test",
            phone = "01012345678"
    )

    val mockThreadExecutor = mock(ThreadExecutor::class)
    val mockPostExecutionThread = mock(PostExecutionThread::class)
    val mockUserRepository = mock(UserRepository::class)
    lateinit var testObserver: TestObserver<User>

    beforeEachTest {
        getMe = GetMe(mockThreadExecutor, mockPostExecutionThread, mockUserRepository)
    }

    on("which getting User is successful") {
        When calling mockUserRepository.me() itReturns Single.just(fakeUser)
        testObserver = getMe.buildUseCaseObservable(null).test()

        it("must emit a User") {
            testObserver.assertValue(fakeUser)
        }
    }

    on("which getting User is failed") {
        val exception = Exception("No User")
        When calling mockUserRepository.me() itReturns Single.error(exception)
        testObserver = getMe.buildUseCaseObservable(null).test()

        it("must emit a Exception") {
            testObserver.assertError(exception)
        }
    }
})

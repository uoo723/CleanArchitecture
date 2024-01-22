package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.AuthInfo
import com.umanji.umanjiapp.domain.entity.User
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.AuthRepository
import com.umanji.umanjiapp.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class SignInOrUpTest : Spek({
    lateinit var signInOrUp: SignInOrUp

    val fakeUser = User(
            id = "1",
            name = "test",
            email = "test@example.com",
            phone = "01012345678"
    )

    val fakeAuthInfoSignInInput = AuthInfo(
            id = "test@example.com",
            password = "test"
    )

    val fakeAuthInfoSignUpInput = AuthInfo(
            id = "test@example.com",
            password = "test",
            user = fakeUser
    )

    val fakeAuthInfoOutput = AuthInfo(
            token = "abcd",
            user = fakeUser
    )

    val mockThreadExecutor = mock(ThreadExecutor::class)
    val mockPostExecutionThread = mock(PostExecutionThread::class)
    val mockUserRepository = mock(UserRepository::class)
    val mockAuthRepository = mock(AuthRepository::class)
    lateinit var testObserver: TestObserver<Any>

    beforeEachTest {
        signInOrUp = SignInOrUp(mockThreadExecutor, mockPostExecutionThread,
                mockAuthRepository, mockUserRepository)
    }

    on("succeed sign in") {
        (When calling mockAuthRepository.createAuthInfo(fakeAuthInfoSignInInput)
                itReturns Single.just(fakeAuthInfoOutput))
        (When calling mockAuthRepository.saveAuthInfo(fakeAuthInfoOutput)
                itReturns Completable.complete())
        When calling mockUserRepository.saveMe(fakeUser) itReturns Completable.complete()

        testObserver = signInOrUp.buildUseCaseObservable(fakeAuthInfoSignInInput).test()

        it("must be completed") {
            testObserver.assertComplete()
        }
    }

    on("succeed sign up") {
        (When calling mockAuthRepository.createAuthInfo(fakeAuthInfoSignUpInput)
                itReturns Single.just(fakeAuthInfoOutput))
        (When calling mockAuthRepository.saveAuthInfo(fakeAuthInfoOutput)
                itReturns Completable.complete())
        When calling mockUserRepository.saveMe(fakeUser) itReturns Completable.complete()

        testObserver = signInOrUp.buildUseCaseObservable(fakeAuthInfoSignInInput).test()

        it("must be completed") {
            testObserver.assertComplete()
        }
    }

    on("failed sign in") {
        val exception = Exception("Auth failed")
        (When calling mockAuthRepository.createAuthInfo(fakeAuthInfoSignInInput)
                itReturns Single.error(exception))

        testObserver = signInOrUp.buildUseCaseObservable(fakeAuthInfoSignInInput).test()

        it("must be emit a Exception") {
            testObserver.assertError(exception)
        }
    }
})

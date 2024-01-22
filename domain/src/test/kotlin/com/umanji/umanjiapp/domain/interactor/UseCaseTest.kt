package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


class UseCaseTest : Spek({
    class Params

    val emptyParams = Params()

    val mockThreadExecutor = mock(ThreadExecutor::class)
    val mockPostExecutionThread = mock(PostExecutionThread::class)

    When calling mockPostExecutionThread.scheduler itReturns TestScheduler()

    given("Test UseCase") {
        on("UseCase#buildUseCaseObservable that causes calling Observer#onComplete") {
            val publishSubject = PublishSubject.create<Any>()
            val testUseCase = object : UseCase<Params, Any>(
                    mockThreadExecutor, mockPostExecutionThread) {
                override fun buildUseCaseObservable(params: Params?): Observable<Any> =
                    publishSubject
            }
            val testObserver = testUseCase.buildUseCaseObservable(emptyParams).test()

            publishSubject.onComplete()

            it("Observer#onComplete must be called") {
                testObserver.assertComplete()
            }
        }

        on("UseCase#buildUseCaseObservable that causes calling Observer#onError") {
            val publishSubject = PublishSubject.create<Any>()
            val testUseCase = object : UseCase<Params, Any>(
                    mockThreadExecutor, mockPostExecutionThread) {
                override fun buildUseCaseObservable(params: Params?): Observable<Any> =
                    publishSubject
            }
            val throwable = Throwable("Error occurred")
            val testObserver = testUseCase.buildUseCaseObservable(emptyParams).test()

            publishSubject.onError(throwable)

            it("Observer#onError must be called") {
                testObserver.assertError(throwable)
            }
        }

        on("UseCase#buildUseCaseObservable that causes calling Observer#onNext") {
            val publishSubject = PublishSubject.create<Params>()
            val testUseCase = object : UseCase<Params, Params>(
                    mockThreadExecutor, mockPostExecutionThread) {
                override fun buildUseCaseObservable(params: Params?): Observable<Params> =
                        publishSubject
            }
            val testObserver = testUseCase.buildUseCaseObservable(emptyParams).test()

            publishSubject.onNext(emptyParams)

            it("Observer#onNext must be called") {
                testObserver.assertValue(emptyParams)
            }
        }

        on("Test UseCase#dispose") {
            val testObserver = object : DisposableObserver<Any>() {
                override fun onNext(t: Any) = Unit
                override fun onError(e: Throwable) = Unit
                override fun onComplete() = Unit
            }
            val testUseCase = object : UseCase<Params, Any>(
                    mockThreadExecutor, mockPostExecutionThread) {
                override fun buildUseCaseObservable(params: Params?): Observable<Any> =
                        Completable.complete().toObservable()
            }
            testUseCase.execute(emptyParams, testObserver)
            testUseCase.dispose()

            it("DisposableObserver#isDispose should be true") {
                testObserver.isDisposed shouldBe true
            }
        }
    }
})

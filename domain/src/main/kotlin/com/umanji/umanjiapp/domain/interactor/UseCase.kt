package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.observers.DisposableObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a [DisposableObserver]
 * that will execute its job in a background thread and will post the result in the UI thread.
 *
 * @param In Input port of the UseCase.
 * @param Out Output port of the UseCase.
 */
abstract class UseCase<in In, Out>(
        private val threadExecutor: ThreadExecutor,
        private val postExecutionThread: PostExecutionThread
) {
    private var disposable: CompositeDisposable = CompositeDisposable()

    private val onNextStub: (Out) -> Unit = {}
    private val onErrorStub: (Throwable) -> Unit =
            { RxJavaPlugins.onError(OnErrorNotImplementedException(it)) }
    private val onCompleteStub: () -> Unit = {}

    /**
     * Build an [Observable] which will be used when executing the current [UseCase].
     */
    abstract fun buildUseCaseObservable(params: In): Observable<Out>

    /**
     * Executes the current use case.
     *
     * @param params Parameters used to build/execute this use case.
     * @param observer [DisposableObserver] which will be listening to the observable build by
     *      [buildUseCaseObservable] method.
     */
    open fun execute(params: In, observer: DisposableObserver<Out>) {
        val observable = buildUseCaseObservable(params)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }

    open fun execute(params: In, onNext: (Out) -> Unit = onNextStub,
                     onError: (Throwable) -> Unit = onErrorStub,
                     onComplete: () -> Unit = onCompleteStub) {
        val disposable = buildUseCaseObservable(params)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
                .subscribe(onNext, onError, onComplete)
        addDisposable(disposable)
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    open fun dispose() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    /**
     * Reset [CompositeDisposable].
     */
    open fun clear() {
        if (disposable.isDisposed) {
            disposable = CompositeDisposable()
        }
    }

    private fun addDisposable(disposable: Disposable) {
        this.disposable.add(disposable)
    }
}

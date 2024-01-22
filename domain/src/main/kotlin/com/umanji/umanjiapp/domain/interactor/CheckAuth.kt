package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.AuthRepository
import com.umanji.umanjiapp.domain.repository.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * checking auth's freshness.
 */
open class CheckAuth @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val userRepository: UserRepository,
        private val authRepository: AuthRepository
) : UseCase<Nothing?, Nothing>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Nothing?): Observable<Nothing> =
            authRepository.checkAutInfo().flatMapCompletable {
                userRepository.saveMe(it.user!!)
            }.toObservable()
}

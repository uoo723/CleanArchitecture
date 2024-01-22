package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.AuthRepository
import com.umanji.umanjiapp.domain.repository.UserRepository
import io.reactivex.Observable
import javax.inject.Inject


/**
 * This class is an implementation of [UseCase] that represents a use case for
 * logout.
 */
open class Logout @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val authRepository: AuthRepository,
        private val userRepository: UserRepository
) : UseCase<Nothing?, Nothing>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Nothing?): Observable<Nothing> =
            authRepository.deleteAuthInfo().andThen(userRepository.deleteMe()).toObservable()
}

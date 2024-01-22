package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.AuthInfo
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.AuthRepository
import com.umanji.umanjiapp.domain.repository.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * sign in or sign up.
 */
open class SignInOrUp @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val authRepository: AuthRepository,
        private val userRepository: UserRepository
) : UseCase<AuthInfo, Nothing>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: AuthInfo): Observable<Nothing> {
        return authRepository.createAuthInfo(params)
                .flatMapCompletable {
                    authRepository.saveAuthInfo(it)
                            .mergeWith(userRepository.saveMe(it.user!!))
                }
                .toObservable()
    }
}

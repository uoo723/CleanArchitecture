package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.User
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a specific [User].
 */
open class GetUser @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val userRepository: UserRepository
) : UseCase<String, User>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: String): Observable<User> =
            userRepository.getUser(params).toObservable()
}

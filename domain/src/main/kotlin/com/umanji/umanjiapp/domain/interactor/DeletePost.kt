package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.Post
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.PostRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * deleting a [Post].
 */
open class DeletePost @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val postRepository: PostRepository
) : UseCase<String, Nothing>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: String): Observable<Nothing> {
        return postRepository.deletePost(params).toObservable()
    }
}

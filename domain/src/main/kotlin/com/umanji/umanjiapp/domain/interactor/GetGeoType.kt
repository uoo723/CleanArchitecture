package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Post
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.PostRepository
import io.reactivex.Observable
import javax.inject.Inject


/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a [Post] that represents type.
 */
open class GetGeoType @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val postRepository: PostRepository
) : UseCase<Location, Post>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Location): Observable<Post> {
        return postRepository.geoType(params).toObservable()
    }
}

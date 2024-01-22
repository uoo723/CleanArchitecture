package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.Post
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.PostRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a list of [Post].
 */
open class GetPosts @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val postRepository: PostRepository
) : UseCase<GetPosts.Params, List<Post>>(threadExecutor, postExecutionThread) {

    interface Params

    data class Params1(
            val offset: Int = 0,            // page
            val portalName: String,
            val portalType: String,
            val limit: Int? = null
    ) : Params

    data class Params2(
            val latitude: Double,
            val longitude: Double,
            val zoom: Int,
            val limit: Int? = null
    ) : Params

    data class Params3(
            val type: String,
            val id: String,
            val limit: Int? = null
    ) : Params

    data class Params4(
            val userId: String
    ) : Params

    override fun buildUseCaseObservable(params: Params): Observable<List<Post>> {
        return when (params) {
            is Params1 ->
                postRepository.posts(params.offset, params.portalName,
                        params.portalType, params.limit).toObservable()
            is Params2 ->
                postRepository.posts(params.latitude, params.longitude,
                        params.zoom, params.limit).toObservable()
            is Params3 ->
                postRepository.posts(params.type, params.id, params.limit).toObservable()
            is Params4 ->
                    postRepository.posts(params.userId).toObservable()
            else -> throw IllegalArgumentException("params is not valid")
        }
    }
}

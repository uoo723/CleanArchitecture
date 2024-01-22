package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.Location
import com.umanji.umanjiapp.domain.entity.Portal
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.GeoRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a [Portal].
 */
open class GetPortal @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val geoRepository: GeoRepository
) : UseCase<GetPortal.Params, Portal>(threadExecutor, postExecutionThread) {

    data class Params(
            val location: Location? = null,
            val id: String? = null
    )

    override fun buildUseCaseObservable(params: Params): Observable<Portal> {
        return if (params.location != null && params.id == null) {
            geoRepository.portal(params.location).toObservable()
        } else if (params.location == null && params.id != null) {
            geoRepository.portal(params.id).toObservable()
        } else {
            Observable.error(IllegalArgumentException("GetPortal#Params is illegal"))
        }
    }
}

package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.Group
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.GroupRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * creating a [Group].
 */

open class CreateGroup @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val groupRepository: GroupRepository
) : UseCase<Group, Nothing>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Group): Observable<Nothing> {
        return groupRepository.createGroup(params).toObservable()
    }
}
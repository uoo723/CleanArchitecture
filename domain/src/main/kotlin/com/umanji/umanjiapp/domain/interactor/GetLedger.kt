package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.entity.BankTransaction
import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.BankRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * retrieving a user's leger.
 */
open class GetLedger @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val bankRepository: BankRepository
) : UseCase<String, List<BankTransaction>>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: String): Observable<List<BankTransaction>> =
            bankRepository.getLedger(params).toObservable()
}

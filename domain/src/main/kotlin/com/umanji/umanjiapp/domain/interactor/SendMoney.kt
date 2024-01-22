package com.umanji.umanjiapp.domain.interactor

import com.umanji.umanjiapp.domain.executor.PostExecutionThread
import com.umanji.umanjiapp.domain.executor.ThreadExecutor
import com.umanji.umanjiapp.domain.repository.BankRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * This class is an implementation of [UseCase] that represents a use case for
 * sending money.
 */
open class SendMoney @Inject constructor(
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread,
        private val bankRepository: BankRepository
) : UseCase<SendMoney.Params, Nothing>(threadExecutor, postExecutionThread) {

    data class Params(
            val sender: String,
            val receiver: String,
            val amount: Double,
            val description: String?
    )

    override fun buildUseCaseObservable(params: Params): Observable<Nothing> =
            bankRepository.sendMoney(params.sender, params.receiver,
                    params.amount, params.description).toObservable()
}

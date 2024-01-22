package com.umanji.umanjiapp.domain.repository

import com.umanji.umanjiapp.domain.entity.BankTransaction
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface that represents a Repository related to bank data.
 */
interface BankRepository {

    /**
     * Get a [Completable] which will be completed when sending money is successful.
     *
     * @param sender Sender id
     * @param receiver Receiver id
     * @param amount The amount of the money to be sent.
     * @param description The description about transaction
     */
    fun sendMoney(sender: String, receiver: String,
                  amount: Double, description: String? = null): Completable

    /**
     * Get a [Single] which will emit a list of [BankTransaction].
     *
     * @param sender Sender id
     */
    fun getLedger(sender: String): Single<List<BankTransaction>>
}

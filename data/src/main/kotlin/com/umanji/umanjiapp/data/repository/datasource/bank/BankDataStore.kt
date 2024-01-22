package com.umanji.umanjiapp.data.repository.datasource.bank

import com.umanji.umanjiapp.data.entity.BankTransactionEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface that represents a data store from where bank data is retrieved.
 */
internal interface BankDataStore {

    fun sendMoney(sender: String, receiver: String,
                  amount: Double, description: String? = null): Completable

    fun getLedger(sender: String): Single<List<BankTransactionEntity>>
}

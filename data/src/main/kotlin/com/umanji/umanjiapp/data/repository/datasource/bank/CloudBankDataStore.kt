package com.umanji.umanjiapp.data.repository.datasource.bank

import com.umanji.umanjiapp.data.entity.BankTransactionEntity
import com.umanji.umanjiapp.data.network.RestApi
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [BankDataStore] implementation based on connections to the api.
 */
@Singleton
open class CloudBankDataStore @Inject constructor(
        private val restApi: RestApi
): BankDataStore {

    override fun sendMoney(sender: String, receiver: String,
                           amount: Double, description: String?): Completable {
        return restApi.sendMoney(sender, receiver, amount, description)
    }

    override fun getLedger(sender: String): Single<List<BankTransactionEntity>> {
        return restApi.getLedger(sender).map { it.data }
    }
}

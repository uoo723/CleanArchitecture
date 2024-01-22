package com.umanji.umanjiapp.data.repository

import com.umanji.umanjiapp.data.entity.mapper.BankTransactionEntityMapper
import com.umanji.umanjiapp.data.repository.datasource.bank.CloudBankDataStore
import com.umanji.umanjiapp.domain.entity.BankTransaction
import com.umanji.umanjiapp.domain.repository.BankRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [BankRepository] for retrieving geographic data.
 */
@Singleton
class BankDataRepository @Inject constructor(
        private val cloudBankDataStore: CloudBankDataStore
) : BankRepository {

    override fun sendMoney(sender: String, receiver: String,
                           amount: Double, description: String?): Completable {
        return cloudBankDataStore.sendMoney(sender, receiver, amount, description)
    }

    override fun getLedger(sender: String): Single<List<BankTransaction>> {
        return cloudBankDataStore.getLedger(sender).map {
            it.map(BankTransactionEntityMapper::transformDomain)
        }
    }
}

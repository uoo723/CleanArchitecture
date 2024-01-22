package com.umanji.umanjiapp.ui.wallet

import com.umanji.umanjiapp.model.BankTransactionModel
import com.umanji.umanjiapp.ui.BaseView


interface WalletView : BaseView {
    fun showMoney(money: Double)
    fun showLedger(ledger: List<BankTransactionModel>)
    fun showRequiringLogin()
}

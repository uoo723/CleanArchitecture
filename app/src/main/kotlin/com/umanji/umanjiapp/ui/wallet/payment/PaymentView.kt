package com.umanji.umanjiapp.ui.wallet.payment

import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.ui.BaseView

interface PaymentView : BaseView {
    enum class FormType {
        PHONE, AMOUNT
    }

    fun showError(message: String, type: FormType)
    fun showRequiringLogin()
    fun showMoney(money: Double)

    /**
     * user is null if no result
     */
    fun showReceiver(user: UserModel?)

    fun showSuccessfulSending()
}

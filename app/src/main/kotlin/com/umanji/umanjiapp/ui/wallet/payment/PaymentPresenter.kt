package com.umanji.umanjiapp.ui.wallet.payment

import android.content.Context
import android.util.Log
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.common.util.isCellPhoneValid
import com.umanji.umanjiapp.data.exception.NoValue
import com.umanji.umanjiapp.domain.interactor.GetMe
import com.umanji.umanjiapp.domain.interactor.GetUser
import com.umanji.umanjiapp.domain.interactor.SendMoney
import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.model.mapper.UserModelMapper
import com.umanji.umanjiapp.ui.BasePresenter
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@ActivityScope
class PaymentPresenter @Inject constructor(
        private val context: Context,
        private val networkUtils: NetworkUtils,
        private val getMe: GetMe,
        private val getUser: GetUser,
        private val sendMoney: SendMoney
) : BasePresenter<PaymentView> {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = PaymentPresenter::class.java.simpleName

    private var view: PaymentView? = null

    private var user: UserModel? = null

    private var sender: UserModel? = null
    private var receiver: UserModel? = null

    private var amount: Double? = null
    private var description: String? = null

    override fun bindView(view: PaymentView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()
        view = null

        getMe.dispose()
        getUser.dispose()
        sendMoney.dispose()
    }

    fun getMoney() {
        view?.showProgress()


        getMe.clear()
        getMe.execute(true, onNext = {
            user = UserModelMapper.transform(it)

            view?.hideProgress()
            view?.showMoney(it.money)
//            getLeger()
        }, onError = {
            view?.hideProgress()
            if (it is NoValue) {
                view?.showRequiringLogin()
            } else {
                view?.showError(it.message)
                Log.d(TAG, "getMe", it)
            }
        })
    }

    fun searchUser(phoneNumber: String) {
        if (phoneNumber.isBlank()) {
            view?.showError(context.getString(R.string.required_form), PaymentView.FormType.PHONE)
            return
        }

        if (!phoneNumber.isCellPhoneValid()) {
            view?.showError(context.getString(R.string.invalid_form), PaymentView.FormType.PHONE)
            return
        }

        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        view?.showProgress()

        getUser.clear()
        getUser.execute(phoneNumber, onNext = {
            view?.hideProgress()

            if (it.id.isBlank()) {
                receiver = null
                view?.showReceiver(null)
            } else {
                receiver = UserModelMapper.transform(it)
                view?.showReceiver(UserModelMapper.transform(it))
            }
        }, onError = {
            view?.hideProgress()
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                else -> {
                    Log.d(TAG, "getUser", it)
                    view?.showError(it.message)
                }
            }
        })
    }

    fun sendMoney(amount: String, description: String? = null) {
        if (amount.isBlank()) {
            view?.showError(context.getString(R.string.required_form), PaymentView.FormType.AMOUNT)
            return
        }

        val amountDouble = try {
            amount.replace(",", "").toDouble()
        } catch (e: NumberFormatException) {
            view?.showError(context.getString(R.string.invalid_form), PaymentView.FormType.AMOUNT)
            return
        }

        if (sender == null)  {
            this.amount = amountDouble
            this.description = description
            getSender()
            return
        }

        val sender: UserModel = sender ?: throw IllegalStateException("sender is null")
        val receiver: UserModel? = receiver

        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        if (receiver == null) {
            view?.showError(context.getString(R.string.no_receiver))
            return
        }

        if (sender.money < amountDouble) {
            view?.showError(context.getString(R.string.excess_money))
            return
        }

        val params = SendMoney.Params(sender.id, receiver.id, amountDouble, description)

        view?.showProgress()

        sendMoney.clear()
        sendMoney.execute(params, onComplete = {
            view?.hideProgress()
            view?.showSuccessfulSending()
        }, onError = {
            view?.hideProgress()
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                else -> {
                    Log.d(TAG, "sendMoney", it)
                    view?.showError(it.message)
                }
            }
        })
    }

    private fun getSender() {
        getMe.clear()
        getMe.execute(true, onNext = {
            sender = UserModelMapper.transform(it)
            sendMoney(amount!!.toString(), description)
        }, onError = {
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                is NoValue ->
                    view?.showRequiringLogin()
                else -> {
                    Log.d(TAG, "getMe", it)
                    view?.showError(it.message)
                }
            }
        })
    }
}

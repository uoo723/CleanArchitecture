package com.umanji.umanjiapp.ui.wallet

import android.content.Context
import android.util.Log
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.data.exception.NoValue
import com.umanji.umanjiapp.domain.interactor.GetLedger
import com.umanji.umanjiapp.domain.interactor.GetMe
import com.umanji.umanjiapp.model.BankTransactionModel
import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.model.mapper.BankTransactionModelMapper
import com.umanji.umanjiapp.model.mapper.UserModelMapper
import com.umanji.umanjiapp.ui.BasePresenter
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


@ActivityScope
class WalletPresenter @Inject constructor(
        private val context: Context,
        private val getMe: GetMe,
        private val getLedger: GetLedger
): BasePresenter<WalletView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = WalletPresenter::class.java.simpleName

    var num: Int = 0    // For logging
//    val items: MutableList<BankTransactionModel> = mutableListOf()

    private var view: WalletView? = null

    private var user: UserModel? = null

    override fun bindView(view: WalletView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()
        view = null

        getMe.dispose()
        getLedger.dispose()
    }

    fun getMoney() {
        view?.showProgress()


        getMe.clear()
        getMe.execute(true, onNext = {
            user = UserModelMapper.transform(it)

            view?.hideProgress()
            view?.showMoney(it.money)
            getLeger()
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

    private fun getLeger() {
        val userId: String = user?.id ?: throw IllegalStateException("user is null")

        view?.showProgress()

        getLedger.clear()
        getLedger.execute(userId, onNext = {
            view?.hideProgress()

            view?.showLedger(it.map(BankTransactionModelMapper::transform))
        }, onError = {
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                else -> {
                    Log.d(TAG, "getLedger", it)
                    view?.showError(it.message)
                }
            }
        })
    }
}

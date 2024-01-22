package com.umanji.umanjiapp.ui.sign

import android.content.Context
import android.util.Log
import android.util.Patterns
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.domain.entity.AuthInfo
import com.umanji.umanjiapp.domain.interactor.SignInOrUp
import com.umanji.umanjiapp.ui.BasePresenter
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


@ActivityScope
class SignInPresenter @Inject constructor(
        private val context: Context,
        private val networkUtils: NetworkUtils,
        private val signIn: SignInOrUp
) : BasePresenter<SignInView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = SignInPresenter::class.java.simpleName

    private var view: SignInView? = null

    override fun bindView(view: SignInView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()

        view = null
        signIn.dispose()
    }

    fun validate(str: String, type: SignInView.FormType): Boolean {
        if (str.isBlank()) {
            view?.showError(context.getString(R.string.required_form), type)
            return false
        }

        when (type) {
            SignInView.FormType.EMAIL -> {
                if (!Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                    view?.showError(context.getString(R.string.invalid_form), type)
                    return false
                }
            }
            else -> Unit
        }

        view?.passValidate(type)
        return true
    }

    fun signIn(id: String, password: String) {
        if (!validate(id, SignInView.FormType.EMAIL))
            return

        if (!validate(password, SignInView.FormType.PASSWORD))
            return

        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        val authInfo = AuthInfo(id = id, password = password)

        view?.showProgress()

        signIn.clear()
        signIn.execute(authInfo, onError = {
            view?.hideProgress()

            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                is HttpException -> {
                    if (it.code() == 401) {
                        view?.showError(context.getString(R.string.not_match_id_or_password),
                                SignInView.FormType.PASSWORD)
                    } else {
                        view?.showError(it.message)
                        Log.d(TAG, "signIn in HttpException", it)
                    }
                }
                else -> {
                    view?.showError(it.message)
                    Log.d(TAG, "signIn", it)
                }
            }
        }, onComplete = {
            view?.hideProgress()
            view?.succeedLogin()
        })
    }
}

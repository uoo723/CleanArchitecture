package com.umanji.umanjiapp.ui.sign

import android.content.Context
import android.util.Log
import android.util.Patterns
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.common.util.isCellPhoneValid
import com.umanji.umanjiapp.domain.entity.AuthInfo
import com.umanji.umanjiapp.domain.entity.User
import com.umanji.umanjiapp.domain.interactor.SignInOrUp
import com.umanji.umanjiapp.ui.BasePresenter
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


@ActivityScope
class SignUpPresenter @Inject constructor(
        private val signUp: SignInOrUp,
        private val networkUtils: NetworkUtils,
        private val context: Context
) : BasePresenter<SignUpView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = SignUpPresenter::class.java.simpleName

    private var view: SignUpView? = null

    private var password: String = ""

    override fun bindView(view: SignUpView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()

        view = null
        signUp.dispose()
    }

    fun validate(str: String, type: SignUpView.FormType): Boolean {
        if (str.isBlank()) {
            view?.showError(context.getString(R.string.required_form), type)
            return false
        }

        when (type) {
            SignUpView.FormType.EMAIL -> {
                if (!Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                    view?.showError(context.getString(R.string.invalid_form), type)
                    return false
                }
            }
            SignUpView.FormType.PHONE -> {
                if (!str.isCellPhoneValid()) {
                    view?.showError(context.getString(R.string.invalid_form), type)
                    return false
                }
            }
            SignUpView.FormType.PASSWORD -> {
                if (str.length < 6) {
                    view?.showError(context.getString(R.string.short_password), type)
                    return false
                }
                password = str
            }
            SignUpView.FormType.PASSWORD_CONFIRM -> {
                if (password != str) {
                    view?.showError(context.getString(R.string.not_match), type)
                    return false
                }
            }
            else -> Unit
        }
        view?.passValidate(type)
        return true
    }

    fun signUp(userName: String, email: String, phone: String, password: String, confirm: String) {
        if (!validate(userName, SignUpView.FormType.USER_NAME))
            return

        if (!validate(email, SignUpView.FormType.EMAIL))
            return

        if (!validate(phone, SignUpView.FormType.PHONE))
            return

        if (!validate(password, SignUpView.FormType.PASSWORD))
            return

        if (!validate(confirm, SignUpView.FormType.PASSWORD_CONFIRM))
            return

        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        val authInfo = AuthInfo(
                id = email,
                password = password,
                user = User(
                        name = userName,
                        email = email,
                        phone = phone
                )
        )

        signUp.clear()
        signUp.execute(authInfo, onError = {
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                else -> {
                    Log.d(TAG, "signUp", it)
                    view?.showError(it.message)
                }
            }
        }, onComplete = {
            view?.succeedSignup()
        })
    }
}

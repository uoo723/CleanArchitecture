package com.umanji.umanjiapp.ui.main

import android.util.Log
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.data.exception.NoValue
import com.umanji.umanjiapp.domain.interactor.GetMe
import com.umanji.umanjiapp.domain.interactor.Logout
import com.umanji.umanjiapp.model.mapper.UserModelMapper
import com.umanji.umanjiapp.ui.BasePresenter
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(
        private val getMe: GetMe,
        private val logout: Logout
) : BasePresenter<MainView> {

    enum class ViewState {
        MAIN, MAP
    }

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = MainPresenter::class.java.simpleName

    private var view: MainView? = null

    var viewState = ViewState.MAIN
        private set

    override fun bindView(view: MainView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()

        view = null
        getMe.dispose()
        logout.dispose()
    }

    fun getMe(isLogin: Boolean) {
        if (!isLogin)
            return

        getMe.clear()
        getMe.execute(null, onNext = {
            view?.showLoginState(UserModelMapper.transform(it))
        }, onError = {
            if (it is NoValue) {
                view?.showLogoutState()
            } else {
                Log.d(TAG, "getMe", it)
            }
        })
    }

    fun logout() {
        logout.clear()
        logout.execute(null, onError = {
            view?.showError(it.message)
        }, onComplete = {
            view?.showLogoutState()
        })
    }

    fun changeViewState() {
        viewState = when (viewState) {
            ViewState.MAIN -> ViewState.MAP
            ViewState.MAP -> ViewState.MAIN
        }
    }
}

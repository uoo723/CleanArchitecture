package com.umanji.umanjiapp

import android.content.Intent
import android.support.multidex.MultiDexApplication
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.umanji.umanjiapp.broadcastreceiver.LoginStateChangeReceiver
import com.umanji.umanjiapp.common.util.RestApiErrorUtils
import com.umanji.umanjiapp.domain.interactor.CheckAuth
import com.umanji.umanjiapp.domain.interactor.Logout
import retrofit2.HttpException
import javax.inject.Inject

class MyApplication : MultiDexApplication() {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = MyApplication::class.java.simpleName

    lateinit var appComponent: AppComponent
        private set

    @Inject lateinit var apiErrorUtils: RestApiErrorUtils
    @Inject lateinit var checkAuth: CheckAuth
    @Inject lateinit var logout: Logout

    var isLogin: Boolean = false

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule("http://119.205.233.249:3000/v1/"))
                .repositoryModule(RepositoryModule())
                .build()
        appComponent.inject(this)
        checkLogin()
    }


    fun checkLogin() {
        checkAuth.clear()
        checkAuth.execute(null, onComplete = {
            isLogin = true
            LocalBroadcastManager.getInstance(this).sendBroadcast(
                    Intent(LoginStateChangeReceiver.ACTION).apply {
                        putExtra(LoginStateChangeReceiver.KEY_IS_LOGIN, true)
                    })
        }, onError = {
            isLogin = false
            if (it is HttpException) {
                try {
                    val apiResponse = apiErrorUtils.parseError(it.response())
                    apiResponse.message?.let {
                        if (it == "jwt expired") {
                            logout()
                        }
                    }
                } catch (e: Throwable) {
                    Log.d(TAG, "HttpException", e)
                }
            }
        })
    }

    private fun logout() {
        logout.clear()
        logout.execute(null, onComplete = {
            isLogin = false
        }, onError = {
            Log.d(TAG, "logout", it)
        })
    }
}

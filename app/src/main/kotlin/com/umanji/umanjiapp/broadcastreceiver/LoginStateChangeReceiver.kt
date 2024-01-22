package com.umanji.umanjiapp.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.umanji.umanjiapp.common.util.getBundleKey


class LoginStateChangeReceiver : BroadcastReceiver() {

    companion object {
        val ACTION = "loginStateChange".getBundleKey("com.umanji.umanjiapp.action")
        val KEY_IS_LOGIN = "isLogin".getBundleKey()
    }

    var onChanged: ((isLogin: Boolean) -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION)
            return

        val isLogin = intent.getBooleanExtra(KEY_IS_LOGIN, false)

        onChanged?.invoke(isLogin)
    }
}

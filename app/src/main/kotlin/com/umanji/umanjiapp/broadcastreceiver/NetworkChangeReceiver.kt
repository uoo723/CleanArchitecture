package com.umanji.umanjiapp.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.umanji.umanjiapp.common.util.NetworkUtils


class NetworkChangeReceiver : BroadcastReceiver() {

    var onChanged: ((isNetworkAvailable: Boolean) -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ConnectivityManager.CONNECTIVITY_ACTION)
            return

        val networkUtils = NetworkUtils(context)

        onChanged?.invoke(networkUtils.isConnected())
    }
}

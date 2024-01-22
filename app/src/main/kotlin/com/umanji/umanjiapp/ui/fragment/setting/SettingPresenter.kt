package com.umanji.umanjiapp.ui.fragment.setting

import android.content.Context
import android.util.Log
import com.umanji.umanjiapp.common.FragmentScope
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.ui.BasePresenter
import javax.inject.Inject

/**
 * Created by paulhwang on 24/03/2018.
 */

@FragmentScope
class SettingPresenter @Inject constructor(
        private val context: Context,
        private val networkUtils: NetworkUtils
) : BasePresenter<SettingView> {
    private val TAG = SettingPresenter::class.java.simpleName

    override fun bindView(view: SettingView) {
        Log.d(TAG, "this is bindView")
    }

    override fun unbindView() {
        Log.d(TAG, "this is unbindView")
    }

}
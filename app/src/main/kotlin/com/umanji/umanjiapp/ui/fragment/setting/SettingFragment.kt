package com.umanji.umanjiapp.ui.fragment.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.ui.ActivityComponent
import com.umanji.umanjiapp.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_setting.*
import javax.inject.Inject

/**
 * Created by paulhwang on 24/03/2018.
 */

class SettingFragment: BaseFragment<ActivityComponent, SettingComponent>(), SettingView {

    @Suppress("PrivatePropertyName")
    private val TAG = SettingFragment::class.java.simpleName

    @Inject
    lateinit var presenter: SettingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)

    }

    override fun initWidgets() {
        setting_test.text = getString(R.string.lorem)
        Log.d(TAG, "show initWidgets")
    }

    override fun showProgress() {
        Log.d(TAG, "show showProgress")
    }

    override fun hideProgress() {
        Log.d(TAG, "show hideProgress")
    }

    override fun showError(message: String?) {
        Log.d(TAG, "show showError : $message")
    }

    override fun showTest() {
        Log.d(TAG, "show showTest()")
    }

    override fun createComponent(): SettingComponent =
        DaggerSettingComponent.builder()
                .activityComponent(activityComponent)
                .build()




}
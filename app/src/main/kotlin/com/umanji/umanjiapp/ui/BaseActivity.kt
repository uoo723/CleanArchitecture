package com.umanji.umanjiapp.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.umanji.umanjiapp.AppComponent
import com.umanji.umanjiapp.MyApplication
import com.umanji.umanjiapp.common.util.ComponentManager

abstract class BaseActivity<out C : Any> : AppCompatActivity() {
    val appComponent: AppComponent
        get() = (application as MyApplication).appComponent

    private var _component: C? = null

    var isLogin: Boolean
        get() = (application as MyApplication).isLogin
        set(value) {
            (application as MyApplication).isLogin = value
        }

    val component: C
        get() = _component ?: createComponent().apply { _component = this }

    abstract fun createComponent(): C
    abstract fun initWidgets()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveComponent(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent(savedInstanceState)
        initWidgets()
    }

    private fun initComponent(savedInstanceState: Bundle?) {
        if (_component == null) {
            _component = if (savedInstanceState == null) {
                createComponent()
            } else {
                ComponentManager.restoreComponent<C>(savedInstanceState) ?: createComponent()
            }
        }
    }

    private fun saveComponent(outState: Bundle) {
        _component?.let { ComponentManager.saveComponent(it, outState) }
    }
}

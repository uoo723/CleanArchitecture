package com.umanji.umanjiapp.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.umanji.umanjiapp.HasComponent
import com.umanji.umanjiapp.common.util.ComponentManager

/**
 * Base [Fragment] class for every fragment in this application.
 *
 * @param PC parent component
 * @param C this component
 */
abstract class BaseFragment<out PC, out C : Any> : Fragment() {

    @Suppress("UNCHECKED_CAST")
    val activityComponent: PC
        get() = (activity as HasComponent<PC>).component

    private var _component: C? = null

    var isLogin: Boolean
        get() = (activity as BaseActivity<*>).isLogin
        set(value) {
            (activity as BaseActivity<*>).isLogin = value
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
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

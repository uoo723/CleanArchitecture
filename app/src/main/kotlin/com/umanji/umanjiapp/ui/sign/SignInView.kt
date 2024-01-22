package com.umanji.umanjiapp.ui.sign

import com.umanji.umanjiapp.ui.BaseView


interface SignInView : BaseView {
    enum class FormType {
        EMAIL, PASSWORD
    }

    /**
     * When login succeed.
     */
    fun succeedLogin()
    fun showError(message: String, type: FormType)
    fun passValidate(type: FormType)
}

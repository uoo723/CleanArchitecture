package com.umanji.umanjiapp.ui.sign

import com.umanji.umanjiapp.ui.BaseView


interface SignUpView : BaseView {
    enum class FormType {
        USER_NAME, EMAIL, PHONE, PASSWORD, PASSWORD_CONFIRM
    }

    fun succeedSignup()
    fun showError(message: String, type: FormType)
    fun passValidate(type: FormType)
}

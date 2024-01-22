package com.umanji.umanjiapp.ui.main

import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.ui.BaseView

interface MainView : BaseView {
    fun showLoginState(user: UserModel)
    fun showLogoutState()
}

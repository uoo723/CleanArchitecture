package com.umanji.umanjiapp.ui.group

import android.net.Uri
import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.ui.BaseView

interface CreateGroupView : BaseView {
    fun showPhoto(uri: Uri, fromCamera: Boolean)
    fun showClearPhoto()
    fun showMe(user: UserModel)
    fun succeedCreatePost()
}

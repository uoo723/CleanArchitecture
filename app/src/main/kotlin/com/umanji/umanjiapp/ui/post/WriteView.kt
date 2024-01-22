package com.umanji.umanjiapp.ui.post

import android.net.Uri
import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.ui.BaseView

interface WriteView : BaseView {
    fun showPhoto(uri: Uri, fromCamera: Boolean)
    fun showClearPhoto()
    fun showMe(user: UserModel)
    fun succeedCreatePost()
}

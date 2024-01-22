package com.umanji.umanjiapp.ui.post

import com.umanji.umanjiapp.model.PostModel
import com.umanji.umanjiapp.ui.BaseView


interface PostView : BaseView {
    fun showPost(post: PostModel)
}

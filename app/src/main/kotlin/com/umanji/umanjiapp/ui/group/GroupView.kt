package com.umanji.umanjiapp.ui.group

import com.umanji.umanjiapp.model.PostModel
import com.umanji.umanjiapp.ui.BaseView


interface GroupView : BaseView {
    fun showPost(post: PostModel)
}

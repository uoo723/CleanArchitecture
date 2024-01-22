package com.umanji.umanjiapp.ui.fragment.post

import com.umanji.umanjiapp.ui.BaseView


interface GroupListView : BaseView {
    fun showPosts()
    fun refreshed()
    fun showLoadMoreProgress()
}

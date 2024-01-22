package com.umanji.umanjiapp.ui.group

import android.content.Intent
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.common.util.getBundleKey
import com.umanji.umanjiapp.model.PostModel
import com.umanji.umanjiapp.ui.BasePresenter
import javax.inject.Inject


@ActivityScope
class GroupPresenter @Inject constructor() : BasePresenter<GroupView> {

    private var view: GroupView? = null

    override fun bindView(view: GroupView) {
        this.view = view
    }

    override fun unbindView() {
        view = null
    }

//    fun getPost(intent: Intent) {
//        val post: PostModel? = intent.getParcelableExtra(GroupActivity.KEY_POST)
//        if (post == null) {
//            view?.showError("no post")
//        } else {
//            view?.showPost(post)
//        }
//    }
}

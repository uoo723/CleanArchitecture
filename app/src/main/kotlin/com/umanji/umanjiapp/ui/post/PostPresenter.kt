package com.umanji.umanjiapp.ui.post

import android.content.Intent
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.common.util.getBundleKey
import com.umanji.umanjiapp.model.PostModel
import com.umanji.umanjiapp.ui.BasePresenter
import javax.inject.Inject


@ActivityScope
class PostPresenter @Inject constructor() : BasePresenter<PostView> {

    private var view: PostView? = null

    override fun bindView(view: PostView) {
        this.view = view
    }

    override fun unbindView() {
        view = null
    }

    fun getPost(intent: Intent) {
        val post: PostModel? = intent.getParcelableExtra(PostActivity.KEY_POST)
        if (post == null) {
            view?.showError("no post")
        } else {
            view?.showPost(post)
        }
    }
}

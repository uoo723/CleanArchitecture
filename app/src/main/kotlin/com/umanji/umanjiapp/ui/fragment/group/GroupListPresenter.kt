package com.umanji.umanjiapp.ui.fragment.post

import android.content.Context
import android.util.Log
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.FragmentScope
import com.umanji.umanjiapp.common.util.NetworkUtils
import com.umanji.umanjiapp.domain.interactor.GetPosts
import com.umanji.umanjiapp.model.PostModel
import com.umanji.umanjiapp.model.mapper.PostModelMapper
import com.umanji.umanjiapp.ui.BasePresenter
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject


@FragmentScope
class GroupListPresenter @Inject constructor(
        private val context: Context,
        private val networkUtils: NetworkUtils,
        private val getPosts: GetPosts
) : BasePresenter<GroupListView> {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG = GroupListPresenter::class.java.simpleName
    var num: Int = 0    // For logging

    private var view: GroupListView? = null
    private var params: GetPosts.Params? = null

    val items: MutableList<PostModel> = mutableListOf()

    override fun bindView(view: GroupListView) {
        this.view = view
    }

    override fun unbindView() {
        view?.hideProgress()

        view = null
        getPosts.dispose()
    }

    fun getPosts(params: GetPosts.Params, loadMore: Boolean = false) {
        Log.d("$TAG$num", "params: $params, loadMore: $loadMore")
        this.params = params

        if (!loadMore && items.isNotEmpty()) {
            Log.d("$TAG$num", "This calling is not from loading more data nor getting first")
            view?.showError()
            return
        }

        if (!networkUtils.isConnected()) {
            view?.showError(context.getString(R.string.required_network))
            return
        }

        if (loadMore) {
            view?.showLoadMoreProgress()
        } else {
            view?.showProgress()
        }

        getPosts.clear()
        getPosts.execute(params, {
            view?.hideProgress()
            Log.d("$TAG$num", "posts: $it")

            items.addAll(it.map(PostModelMapper::transform))
            view?.showPosts()
        }, {
            view?.hideProgress()
            when (it) {
                is SocketTimeoutException ->
                    view?.showError(context.getString(R.string.unstable_server))
                is ConnectException ->
                    view?.showError(context.getString(R.string.no_connection_to_server))
                else -> {
                    Log.d("$TAG$num", "getPosts", it)
                    view?.showError(it.message)
                }
            }
        })
    }

    fun refresh(params: GetPosts.Params? = null) {
        if (params != null) {
            this.params = params
        }

        if (this.params == null) {
            view?.hideProgress()
            return
        }

        items.clear()
        view?.refreshed()
        this.params?.let {
            if (it is GetPosts.Params1) {
                this.params = it.copy(offset = 0)
            }
        }
        this.params?.let { getPosts(it) }
    }
}

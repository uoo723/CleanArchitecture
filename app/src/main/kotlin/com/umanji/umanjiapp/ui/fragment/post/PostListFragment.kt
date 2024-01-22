package com.umanji.umanjiapp.ui.fragment.post

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.listener.EndlessRecyclerScrollListener
import com.umanji.umanjiapp.common.util.DateUtils
import com.umanji.umanjiapp.common.util.showSnackBar
import com.umanji.umanjiapp.domain.interactor.GetPosts
import com.umanji.umanjiapp.ui.ActivityComponent
import com.umanji.umanjiapp.ui.BaseFragment
import com.umanji.umanjiapp.ui.fragment.post.adapter.PostAdapter
import com.umanji.umanjiapp.ui.info.InfoActivity
import com.umanji.umanjiapp.ui.main.fragment.main.MainFragmentPresenter
import com.umanji.umanjiapp.ui.post.PostActivity
import com.umanji.umanjiapp.ui.post.WriteActivity
import com.umanji.umanjiapp.ui.sign.SigninActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_post.*
import javax.inject.Inject


class PostListFragment : BaseFragment<ActivityComponent, PostListComponent>(), PostListView {

    @Suppress("PrivatePropertyName")
    private val TAG = PostListFragment::class.java.simpleName

    @Inject
    lateinit var presenter: PostListPresenter
    @Inject
    lateinit var dateUtils: DateUtils
    @Inject
    lateinit var mainPresenter: MainFragmentPresenter

//    @Suppress("PrivatePropertyName")
    private val REQUEST_CODE_WRITE = 0
    private val tab: TabLayout get() = activity.tab

    private val postAdapter by lazy { PostAdapter(dateUtils) }
    private val num by lazy { arguments?.getInt("num") ?: 0 }

    private var lazyUpdate: Boolean = false

    private lateinit var scrollListener: EndlessRecyclerScrollListener

    var params: GetPosts.Params? = null
        set(value) {
            field = value
            if (value != null) {
                try {
                    presenter.getPosts(value)
                } catch (e: UninitializedPropertyAccessException) {
                    lazyUpdate = true
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        presenter.num = num
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)

        if (lazyUpdate) {
            params?.let { presenter.getPosts(it) }
            lazyUpdate = false
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun initWidgets() {
        posts.adapter = postAdapter
        posts.layoutManager = LinearLayoutManager(context)

        scrollListener = object : EndlessRecyclerScrollListener(posts.layoutManager
                as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemCount: Int, view: RecyclerView) {
                Log.d("$TAG$num", "page: $page, totalItemCount: $totalItemCount")
                params?.let {
                    if (it is GetPosts.Params1) {
                        presenter.getPosts(it.copy(offset = page), true)
                    }
                }
            }
        }

        posts.addOnScrollListener(scrollListener)

        postAdapter.setItems(presenter.items)
        postAdapter.header = PostAdapter.Header(image = "dummy")
        postAdapter.itemListener = {
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra(PostActivity.KEY_POST, it)
            startActivity(intent, ActivityOptions.makeCustomAnimation(context,
                    R.anim.slide_in_right, R.anim.out_back).toBundle())
        }

        postAdapter.headerListener = {
            if (isLogin) {
                startActivityForResult(
                        Intent(context, WriteActivity::class.java).apply {
                            putExtras(mainPresenter.getBundle(activity.intent, tab.selectedTabPosition))
                        },
                        REQUEST_CODE_WRITE, ActivityOptions.makeCustomAnimation(context,
                        R.anim.slide_in_top, R.anim.out_back).toBundle())
            } else {
                startActivity(Intent(context, SigninActivity::class.java))
            }
        }
        postAdapter.userIdListener = {
            startActivity(Intent(context, InfoActivity::class.java).apply {
                putExtra(InfoActivity.KEY_INFO_TYPE, InfoActivity.InfoType.PROFILE)
                putExtra(InfoActivity.KEY_INFO_ID, it.userId)
                putExtra(InfoActivity.KEY_INFO_TITLE, it.userName)
            }, ActivityOptions.makeCustomAnimation(context,
                    R.anim.slide_in_right, R.anim.out_back).toBundle())
        }
        postAdapter.placeIdListener = {
            startActivity(Intent(context, InfoActivity::class.java).apply {
                if (it.portalId.isNotBlank()) {
                    putExtra(InfoActivity.KEY_INFO_TYPE, InfoActivity.InfoType.PORTAL)
                    putExtra(InfoActivity.KEY_INFO_ID, it.portalId)
                    putExtra(InfoActivity.KEY_INFO_TITLE, it.placeName)
                } else if (it.placeId.isNotBlank()) {
                    putExtra(InfoActivity.KEY_INFO_TYPE, InfoActivity.InfoType.PLACE)
                    putExtra(InfoActivity.KEY_INFO_ID, it.placeId)
                    putExtra(InfoActivity.KEY_INFO_TITLE, it.placeName)
                }
            }, ActivityOptions.makeCustomAnimation(context,
                    R.anim.slide_in_right, R.anim.out_back).toBundle())
        }
        postAdapter.linkPreviewListener = {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }
        swiperefresh.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        if (resultCode != Activity.RESULT_OK || data == null)
            return

        if (requestCode == REQUEST_CODE_WRITE) {
            presenter.bindView(this)
            mainPresenter.refresh(intent = data)
        }
    }

    override fun showProgress() {
        progress.bringToFront()
        progress.show()
    }

    override fun showLoadMoreProgress() {
        if ((posts.layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition() == presenter.items.size - 1) {
            loadmore_progressbar_area.bringToFront()
            loadmore_progressbar_area.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {
        swiperefresh.isRefreshing = false
        progress.hide()
        loadmore_progressbar_area.visibility = View.GONE
    }

    override fun showError(message: String?) {
        Log.d(TAG, "showError: $message")
        message?.let { view?.showSnackBar(it) }
        swiperefresh.isRefreshing = false
    }

    override fun showPosts() {
        Log.d("$TAG$num", "showPosts")
        postAdapter.notifyDataSetChanged()
    }

    fun refresh(params: GetPosts.Params? = null) {
        try {
            Log.d("$TAG$num", "refresh")
            presenter.refresh(params)
        } catch (e: UninitializedPropertyAccessException) {
        }
    }

    override fun refreshed() {
        Log.d("$TAG$num", "refreshed")
        postAdapter.notifyDataSetChanged()
        scrollListener.resetState()
    }

    override fun createComponent(): PostListComponent =
            DaggerPostListComponent.builder()
                    .activityComponent(activityComponent)
                    .build()
}

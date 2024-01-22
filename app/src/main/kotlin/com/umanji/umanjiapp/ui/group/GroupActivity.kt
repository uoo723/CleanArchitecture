package com.umanji.umanjiapp.ui.group

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.*
import com.umanji.umanjiapp.model.PostModel
import com.umanji.umanjiapp.ui.BaseActivity
import com.umanji.umanjiapp.ui.info.InfoActivity
import com.umanji.umanjiapp.ui.post.CommentActivity
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.link_preview.*
import javax.inject.Inject


class GroupActivity : BaseActivity<GroupComponent>(), GroupView {

    companion object {
        val KEY_POST = "post".getBundleKey()
    }

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = GroupActivity::class.java.simpleName

    @Inject lateinit var presenter: GroupPresenter
    @Inject lateinit var dateUtils: DateUtils

    private val textCrawler = TextCrawler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        presenter.bindView(this)
//        presenter.getPost(intent)
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onDestroy() {
        super.onDestroy()
        textCrawler.cancel()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.in_front, R.anim.slide_out_right)
    }

    override fun initWidgets() {
        setContentView(R.layout.activity_post)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar.setTitle(R.string.activity_post)

        comment.setOnClickListener {
            startActivity(Intent(this, CommentActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            finish()
            true
        }  else if(id == R.id.action_post){
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return true
    }

    override fun showProgress() {
        Log.d(TAG, "showProgress")
    }

    override fun hideProgress() {
        Log.d(TAG, "hideProgress")
    }

    override fun showError(message: String?) {
        message?.let { coordinator_layout.showSnackBar(it) }
    }

    override fun showPost(post: PostModel) {
        Log.d(TAG, "post: $post")
        val linkPreviewCallback = object : LinkPreviewCallback {
            override fun onPre() {
                link_preview_loading.visibility = View.VISIBLE
            }

            override fun onPos(sourceContent: SourceContent, isNull: Boolean) {
                link_preview_loading.visibility = View.GONE
                if (isNull) return

                link_preview.visibility = View.VISIBLE
                link_preview.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(sourceContent.finalUrl)))
                }
                link_title.text = sourceContent.title
                link_desc.text = sourceContent.description
                Glide.with(link_preview_image)
                        .load(sourceContent.images[0])
                        .into(link_preview_image)
            }
        }

        time.text = dateUtils.getTimeString(post.updatedAt, R.array.time_decor)
        content.text = post.content
        post.content.getWebUrl()?.let { textCrawler.makePreview(linkPreviewCallback, it) }
        post_channel.text = post.channels.reduce { acc, s ->  "$acc #$s" }

        post_title.let {
            it.text = getString(R.string.post_title, post.userName, post.placeName)
            it.movementMethod = LinkTouchMovementMethod
            val userClickableSpan = it.generateClickableSpan {
                startActivity(Intent(this, InfoActivity::class.java).apply {
                    putExtra(InfoActivity.KEY_INFO_TYPE, InfoActivity.InfoType.PROFILE)
                    putExtra(InfoActivity.KEY_INFO_ID, post.userId)
                    putExtra(InfoActivity.KEY_INFO_TITLE, post.userName)
                }, ActivityOptions.makeCustomAnimation(this,
                        R.anim.slide_in_right, R.anim.out_back).toBundle())
            }
            val placeClickableSpan = it.generateClickableSpan {
                startActivity(Intent(this, InfoActivity::class.java).apply {
                    if (post.portalId.isNotBlank()) {
                        putExtra(InfoActivity.KEY_INFO_TYPE, InfoActivity.InfoType.PORTAL)
                        putExtra(InfoActivity.KEY_INFO_ID, post.portalId)
                        putExtra(InfoActivity.KEY_INFO_TITLE, post.placeName)
                    } else if (post.placeId.isNotBlank()) {
                        putExtra(InfoActivity.KEY_INFO_TYPE, InfoActivity.InfoType.PLACE)
                        putExtra(InfoActivity.KEY_INFO_ID, post.placeId)
                        putExtra(InfoActivity.KEY_INFO_TITLE, post.placeName)
                    }
                }, ActivityOptions.makeCustomAnimation(this,
                        R.anim.slide_in_right, R.anim.out_back).toBundle())
            }
            post.userName.setSpan(it.text, userClickableSpan)
            post.placeName.setSpan(it.text, placeClickableSpan)
        }

        image.let {
            val image = post.images.firstOrNull() ?: return
            Glide.with(it)
                    .load(image)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable,
                                                     transition: Transition<in Drawable>?) {
                            it.visibility = View.VISIBLE
                            Glide.with(it)
                                    .load(resource)
                                    .into(it)
                        }
                    })
        }
    }

    override fun createComponent(): GroupComponent =
            DaggerGroupComponent.builder()
                    .appComponent(appComponent)
                    .build()
}
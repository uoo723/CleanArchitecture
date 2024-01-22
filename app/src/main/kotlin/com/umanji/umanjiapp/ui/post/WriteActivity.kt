package com.umanji.umanjiapp.ui.post

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.getBundleKey
import com.umanji.umanjiapp.common.util.getWebUrl
import com.umanji.umanjiapp.common.util.showSnackBar
import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.link_preview_small.*
import kotlinx.android.synthetic.main.toolbar_progress.*
import javax.inject.Inject


class WriteActivity : BaseActivity<PostComponent>(), WriteView {

    companion object {
        val KEY_SUCCEED_WRITE = "succeed_write".getBundleKey()
        val KEY_CREATE_TYPE = "create_type".getBundleKey()
        val KEY_PLACE_TYPE = "place_type".getBundleKey()
        val KEY_PLACE_ID = "place_id".getBundleKey()
        val KEY_PLACE_GOOGLE_ID = "place_google_id".getBundleKey()
    }

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = WriteActivity::class.java.simpleName

    @Inject lateinit var presenter: WritePresenter

    private val textCrawler = TextCrawler()
    private var isLinkPreview = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        presenter.bindView(this)
        presenter.getMe()
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.in_front, R.anim.slide_out_bottom)
    }

    override fun onDestroy() {
        super.onDestroy()
        textCrawler.cancel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_write -> {
                presenter.createPost(isLogin, intent, content.text.toString())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_write, menu)
        return true
    }

    override fun initWidgets() {
        setContentView(R.layout.activity_write)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar.setTitle(R.string.activity_write)
        photo.setOnClickListener {}
        scroll_view_container.setOnClickListener {
            content.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            imm.showSoftInput(content, InputMethodManager.SHOW_IMPLICIT)
        }
        btn_camera.setOnClickListener { presenter.takeCamera(this) }
        btn_photo.setOnClickListener { presenter.takeGallery(this) }
        btn_photo_cancel.setOnClickListener { presenter.clearPhoto() }
        btn_link_preview_cancel.setOnClickListener {
            isLinkPreview = false
            clearAnimation(link_preview)
        }

        val linkPreviewCallback = object : LinkPreviewCallback {
            override fun onPre() {
                link_preview_loading.visibility = View.VISIBLE
            }

            override fun onPos(sourceContent: SourceContent, isNull: Boolean) {
                link_preview_loading.visibility = View.GONE
                if (isNull) return

                isLinkPreview = true

                link_preview.visibility = View.VISIBLE
                link_title.text = sourceContent.title
                link_desc.text = sourceContent.description
                Glide.with(link_preview_image)
                        .load(sourceContent.images[0])
                        .into(link_preview_image)
            }
        }

        content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty() && s.last().isWhitespace() && !isLinkPreview) {
                    s.getWebUrl()?.let {
                        textCrawler.makePreview(linkPreviewCallback, it)
                    }
                }
            }
        })
    }

    override fun showProgress() {
        progress.bringToFront()
        progress.show()
    }

    override fun hideProgress() {
        progress.hide()
    }

    override fun showError(message: String?) {
        message?.let { constraint_layout.showSnackBar(it) }
    }

    override fun showMe(user: UserModel) {
        tv_name.text = user.userName
    }

    override fun succeedCreatePost() {
        setResult(RESULT_OK,
                Intent().apply { putExtra(KEY_SUCCEED_WRITE, true) })
        finish()
    }

    override fun showPhoto(uri: Uri, fromCamera: Boolean) {
        photo_container.visibility = View.VISIBLE
        Glide.with(this)
                .load(uri)
                .into(photo)
    }

    override fun showClearPhoto() {
        clearAnimation(photo_container)
    }

    private fun clearAnimation(view: View) {
        view.let {
            it.pivotX = it.width.toFloat()
            it.pivotY = 0.0f

            AnimatorSet().apply {
                duration = resources.getInteger(R.integer.anim_duration).toLong()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        photo.setImageDrawable(null)
                        it.visibility = View.GONE
                        it.scaleX = 1.0f
                        it.scaleY = 1.0f
                    }
                })
                playTogether(
                        ObjectAnimator.ofFloat(it, "scaleX", 0.0f),
                        ObjectAnimator.ofFloat(it, "scaleY", 0.0f)
                )
            }.start()
        }
    }

    override fun createComponent(): PostComponent =
            DaggerPostComponent.builder()
                    .appComponent(appComponent)
                    .build()
}
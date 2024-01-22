package com.umanji.umanjiapp.ui.group

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
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.getBundleKey
import com.umanji.umanjiapp.common.util.getWebUrl
import com.umanji.umanjiapp.common.util.showSnackBar
import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.link_preview_small.*
import kotlinx.android.synthetic.main.toolbar_progress.*
import javax.inject.Inject


class CreateGroupActivity : BaseActivity<GroupComponent>(), CreateGroupView {

    companion object {
        val KEY_SUCCEED_WRITE = "succeed_write".getBundleKey()
        val KEY_CREATE_TYPE = "create_type".getBundleKey()
        val KEY_PLACE_TYPE = "place_type".getBundleKey()
        val KEY_PLACE_ID = "place_id".getBundleKey()
        val KEY_PLACE_GOOGLE_ID = "place_google_id".getBundleKey()
    }

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = CreateGroupActivity::class.java.simpleName

    @Inject lateinit var presenter: CreateGroupPresenter

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_write -> {
                presenter.createGroup(isLogin, intent, create_group_title.text.toString())
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
        setContentView(R.layout.activity_create_group)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar.setTitle(R.string.activity_create_group)
        photo.setOnClickListener {}
        scroll_view_container.setOnClickListener {
            create_group_title.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            imm.showSoftInput(create_group_title, InputMethodManager.SHOW_IMPLICIT)
        }
        btn_camera.setOnClickListener { presenter.takeCamera(this) }
        btn_photo.setOnClickListener { presenter.takeGallery(this) }
        btn_photo_cancel.setOnClickListener { presenter.clearPhoto() }
    }

    override fun showProgress() {
        progress.bringToFront()
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showError(message: String?) {
        message?.let { constraint_layout.showSnackBar(it) }
    }

    override fun showMe(user: UserModel) {
//        tv_name.text = user.userName
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
        photo_container.let {
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

    override fun createComponent(): GroupComponent =
            DaggerGroupComponent.builder()
                    .appComponent(appComponent)
                    .build()
}
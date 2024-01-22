package com.umanji.umanjiapp.ui.post

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.umanji.umanjiapp.R
import kotlinx.android.synthetic.main.activity_comment.*

/**
 * Created by lentes on 2018. 1. 9..
 */
class CommentActivity : AppCompatActivity() {
    private val toolbar: Toolbar get() = toolbar_comment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeButtonEnabled(true)

        toolbar.setTitle(R.string.activity_comment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }
}
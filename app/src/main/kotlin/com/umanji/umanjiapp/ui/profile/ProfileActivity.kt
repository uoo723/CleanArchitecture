package com.umanji.umanjiapp.ui.profile

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.ui.profile.adapter.ProfilePagerAdapter
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    @Suppress("PrivatePropertyName")
    private val TAG = ProfileActivity::class.java.simpleName

    private val mToolbar: Toolbar get() = toolbar_profile
    private val mCollapsingToolbar: CollapsingToolbarLayout get() = collapsing_profile
    private val mTabLayout: TabLayout get() = tab_profile
    private val mViewPager: ViewPager get() = viewpager_profile
    private val mPagerAdapter: ProfilePagerAdapter
            get() =  ProfilePagerAdapter(supportFragmentManager, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        Log.d(TAG, "onCreate")

        mTabLayout.setupWithViewPager(mViewPager)
        mCollapsingToolbar.setTitle("사용자 이름")

        mViewPager.adapter = mPagerAdapter
        mViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
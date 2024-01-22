package com.umanji.umanjiapp.ui.profile.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.ui.info.fragment.post.BpostListFragment

/**
 * Created by lentes on 2018. 1. 8..
 */

class ProfilePagerAdapter(fm: FragmentManager, private val mContext: Context) : FragmentStatePagerAdapter(fm) {
    private var m1Fragment: BpostListFragment = BpostListFragment()
    private var m2Fragment: BpostListFragment = BpostListFragment()

    private val mTabTitle = intArrayOf(R.string.title_post, R.string.title_property)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> m1Fragment
            1 -> m2Fragment
            else -> throw IllegalArgumentException("position is illegal")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.getString(mTabTitle[position])
    }

    override fun getCount(): Int {
        return TAB_COUNT
    }

    companion object {
        private val TAB_COUNT = 2
    }
}


package com.umanji.umanjiapp.ui.main.fragment.main.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.ui.fragment.post.PostListFragment
import android.os.Parcelable



class MainPagerAdapter(
        fm: FragmentManager,
        private val context: Context
) : FragmentStatePagerAdapter(fm) {
    private val mTownFragment: PostListFragment = PostListFragment()
    private val mDistrictFragment: PostListFragment = PostListFragment()
    private val mProvinceFragment: PostListFragment = PostListFragment()
    private val mNationFragment: PostListFragment = PostListFragment()
    private val mWorldFragment: PostListFragment = PostListFragment()

    private val mTabTitle = intArrayOf(R.string.title_fragment_town, R.string.title_fragment_district, R.string.title_fragment_province, R.string.title_fragment_nation, R.string.title_fragment_world)

    init {
        mTownFragment.arguments = Bundle().apply { putInt("num", 1) }
        mDistrictFragment.arguments = Bundle().apply { putInt("num", 2) }
        mProvinceFragment.arguments = Bundle().apply { putInt("num", 3) }
        mNationFragment.arguments = Bundle().apply { putInt("num", 4) }
        mWorldFragment.arguments = Bundle().apply { putInt("num", 5) }
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> mTownFragment
            1 -> mDistrictFragment
            2 -> mProvinceFragment
            3 -> mNationFragment
            4 -> mWorldFragment
            else -> throw IllegalStateException("position is illegal")
        }
    }

    override fun getItemPosition(`object`: Any?): Int {
        return super.getItemPosition(`object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(mTabTitle[position])
    }

    override fun getCount(): Int {
        return TAB_COUNT
    }

    companion object {
        private const val TAB_COUNT = 5
    }

    override fun saveState(): Parcelable? {
        return null
    }
}

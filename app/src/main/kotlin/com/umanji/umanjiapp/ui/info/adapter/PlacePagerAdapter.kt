package com.umanji.umanjiapp.ui.info.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter


class PlacePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    val fragments = mutableListOf<Pair<String, Fragment>>()

    override fun getItem(position: Int): Fragment {
        return fragments[position].second
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragments[position].first
    }

    override fun getCount(): Int = fragments.size
}

package com.umanji.umanjiapp.ui.main.fragment

import com.umanji.umanjiapp.common.FragmentScope
import com.umanji.umanjiapp.ui.main.MainComponent
import com.umanji.umanjiapp.ui.main.fragment.main.MainFragment
import com.umanji.umanjiapp.ui.main.fragment.map.MapFragment
import dagger.Component


@FragmentScope
@Component(dependencies = [MainComponent::class])
interface MainFragmentComponent {
    fun inject(target: MainFragment)
    fun inject(target: MapFragment)
}

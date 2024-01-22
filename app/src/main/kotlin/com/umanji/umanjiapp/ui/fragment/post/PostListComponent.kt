package com.umanji.umanjiapp.ui.fragment.post

import com.umanji.umanjiapp.common.FragmentScope
import com.umanji.umanjiapp.ui.ActivityComponent
import dagger.Component


@FragmentScope
@Component(dependencies = [ActivityComponent::class])
interface PostListComponent {
    fun inject(target: PostListFragment)
}

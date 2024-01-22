package com.umanji.umanjiapp.ui.post

import com.umanji.umanjiapp.AppComponent
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.ui.ActivityComponent
import dagger.Component


@ActivityScope
@Component(dependencies = [AppComponent::class])
interface PostComponent : ActivityComponent {
    fun inject(target: PostActivity)
    fun inject(target: WriteActivity)
}

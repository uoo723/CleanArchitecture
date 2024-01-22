package com.umanji.umanjiapp.ui.group

import com.umanji.umanjiapp.AppComponent
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.ui.ActivityComponent
import dagger.Component


@ActivityScope
@Component(dependencies = [AppComponent::class])
interface GroupComponent : ActivityComponent {
    fun inject(target: GroupActivity)
    fun inject(target: CreateGroupActivity)
}

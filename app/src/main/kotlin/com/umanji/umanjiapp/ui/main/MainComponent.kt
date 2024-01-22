package com.umanji.umanjiapp.ui.main

import com.umanji.umanjiapp.AppComponent
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.ui.ActivityComponent
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface MainComponent : ActivityComponent {
    fun inject(target: MainActivity)
}

package com.umanji.umanjiapp.ui.sign

import com.umanji.umanjiapp.AppComponent
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.ui.ActivityComponent
import dagger.Component


@ActivityScope
@Component(dependencies = [AppComponent::class])
interface SignComponent : ActivityComponent {
    fun inject(target: SigninActivity)
    fun inject(target: SignupActivity)
}

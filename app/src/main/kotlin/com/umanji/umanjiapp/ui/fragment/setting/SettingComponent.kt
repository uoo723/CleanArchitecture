package com.umanji.umanjiapp.ui.fragment.setting

import com.umanji.umanjiapp.common.FragmentScope
import com.umanji.umanjiapp.ui.ActivityComponent
import dagger.Component

/**
 * Created by paulhwang on 24/03/2018.
 */

@FragmentScope
@Component(dependencies = [ActivityComponent::class])
interface SettingComponent {
    fun inject(target: SettingFragment)
}
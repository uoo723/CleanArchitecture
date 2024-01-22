package com.umanji.umanjiapp.ui

import com.umanji.umanjiapp.AppComponent
import com.umanji.umanjiapp.common.ActivityScope
import dagger.Component


@ActivityScope
@Component(dependencies = [AppComponent::class])
interface ActivityComponent : AppComponent

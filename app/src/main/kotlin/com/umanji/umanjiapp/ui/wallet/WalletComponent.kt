package com.umanji.umanjiapp.ui.wallet

import com.umanji.umanjiapp.AppComponent
import com.umanji.umanjiapp.common.ActivityScope
import com.umanji.umanjiapp.ui.wallet.payment.PaymentActivity
import dagger.Component


@ActivityScope
@Component(dependencies = [AppComponent::class])
interface WalletComponent : AppComponent {
    fun inject(target: WalletActivity)
    fun inject(target: PaymentActivity)
}

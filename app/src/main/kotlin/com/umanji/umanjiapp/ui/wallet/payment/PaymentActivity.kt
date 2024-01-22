package com.umanji.umanjiapp.ui.wallet.payment

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.MoneyTextWatcher
import com.umanji.umanjiapp.common.util.moneyView
import com.umanji.umanjiapp.model.UserModel
import com.umanji.umanjiapp.ui.BaseActivity
import com.umanji.umanjiapp.ui.wallet.DaggerWalletComponent
import com.umanji.umanjiapp.ui.wallet.WalletComponent
import kotlinx.android.synthetic.main.activity_payment.*
import javax.inject.Inject


class PaymentActivity : BaseActivity<WalletComponent>(), PaymentView {


    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = PaymentActivity::class.java.simpleName

    @Inject
    lateinit var presenter: PaymentPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        presenter.getMoney()
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun initWidgets() {
        setContentView(R.layout.activity_payment)
        setSupportActionBar(toolbar_payment)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar_payment.setTitle(R.string.activity_payment)

        et_send_amount.addTextChangedListener(MoneyTextWatcher(et_send_amount))

        btn_search_receiver.setOnClickListener {
            presenter.searchUser(et_receiver.text.toString())
        }

        btn_money_send.setOnClickListener {
            val description: String = et_pay_content.text.toString()
            presenter.sendMoney(et_send_amount.text.toString(), description)
        }

        btn_user_confirm.setOnClickListener{
            et_send_amount.visibility = View.VISIBLE
            et_pay_content.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            Log.d("Paul", "$TAG payment back clicked")
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun showMoney(money: Double) {
        txt_balance.text = moneyView(money.toInt())
    }

    override fun showProgress() {
        Log.d(TAG, "showProgress")    }

    override fun hideProgress() {
        Log.d(TAG, "hideProgress")    }

    override fun showError(message: String?) {
        Log.d(TAG, "showError: $message")    }

    override fun showError(message: String, type: PaymentView.FormType) {
        Log.d(TAG, "showError: $message")    }

    override fun showRequiringLogin() {

        Log.d(TAG, "Login")    }

    override fun showReceiver(user: UserModel?) {
        layout_receiver_empty.visibility = View.INVISIBLE
        layout_receiver_holder.visibility = View.VISIBLE
        txt_receiver.text = user?.userName
//        Log.d(TAG, "user data : $user")     // UserModel(id=#21:0, email=taco@naver.com, userName=taco Bell, money=10080.0, images=[])
    }

    override fun showSuccessfulSending() {
        Log.d(TAG, "submit data")
        finish()
    }

    override fun createComponent(): WalletComponent =
            DaggerWalletComponent.builder()
                    .appComponent(appComponent)
                    .build()
}

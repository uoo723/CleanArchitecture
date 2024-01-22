package com.umanji.umanjiapp.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.listener.EndlessRecyclerScrollListener
import com.umanji.umanjiapp.common.util.moneyView
import com.umanji.umanjiapp.common.util.showToast
import com.umanji.umanjiapp.model.BankTransactionModel
import com.umanji.umanjiapp.ui.BaseActivity
import com.umanji.umanjiapp.ui.wallet.adapter.LedgerAdapter
import com.umanji.umanjiapp.ui.wallet.payment.PaymentActivity
import kotlinx.android.synthetic.main.activity_wallet.*
import javax.inject.Inject

class WalletActivity : BaseActivity<WalletComponent>(), WalletView {
    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = WalletActivity::class.java.simpleName

    @Inject
    lateinit var presenter: WalletPresenter

    private lateinit var scrollListener: EndlessRecyclerScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        presenter.bindView(this)
        presenter.getMoney()
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(getIntent())
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun initWidgets() {
        setContentView(R.layout.activity_wallet)
        setSupportActionBar(toolbar_wallet)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar_wallet.setTitle(R.string.activity_wallet)

        btn_money_send.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }

            R.id.action_buy_dw -> {
                startActivity(Intent(this, BuyDWActivity::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            R.id.action_cash_out -> {
                showToast("Sorry! We are Ready to Run. ^^;")
            }

            else -> super.onOptionsItemSelected(item)
        }
        return  true

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_wallet, menu)
        return true
    }

    override fun showProgress() = Unit

    override fun showMoney(money: Double) {
        tv_money.text = moneyView(money.toInt())
    }

    override fun showLedger(ledger: List<BankTransactionModel>) {

        rv_transaction.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        var adapter = LedgerAdapter(ledger as ArrayList<BankTransactionModel>)
        rv_transaction.adapter = adapter

        scrollListener = object : EndlessRecyclerScrollListener(rv_transaction.layoutManager
                as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemCount: Int, view: RecyclerView) {
//                Log.d("$TAG$num", "page: $page, totalItemCount: $totalItemCount")
//                params?.let {
//                    if (it is GetPosts.Params1) {
//                        presenter.getPosts(it.copy(offset = page), true)
//                    }
//                }
            }
        }

        rv_transaction.addOnScrollListener(scrollListener)

    }

    override fun hideProgress() = Unit

    override fun showError(message: String?) {
        Log.d(TAG, "showError: $message")
//        message?.let { coordinator_layout.showSnackBar(it) }
    }

    override fun showRequiringLogin() {
        Log.d(TAG, "showRequiringLogin :")
    }

    override fun createComponent(): WalletComponent =
            DaggerWalletComponent.builder()
                    .appComponent(appComponent)
                    .build()
}

package com.umanji.umanjiapp.ui.wallet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.MoneyTextWatcher
import kotlinx.android.synthetic.main.activity_buy_dw.*
import kotlinx.android.synthetic.main.activity_payment.*

/**
 * Created by paulhwang on 27/02/2018.
 */

class BuyDWActivity : AppCompatActivity() {
    private val TAG: String = BuyDWActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_dw)
        setSupportActionBar(toolbar_buy_dw)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toolbar_buy_dw.setTitle(R.string.activity_buy_dw)

        et_buy_dw_amount.addTextChangedListener(MoneyTextWatcher(et_buy_dw_amount))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            Log.d("Paul", "payment back clicked")
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            true
        } else super.onOptionsItemSelected(item)
    }


}
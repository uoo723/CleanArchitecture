package com.umanji.umanjiapp.ui.wallet.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.*
import com.umanji.umanjiapp.model.BankTransactionModel
import kotlinx.android.synthetic.main.item_wallet.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by paulhwang on 20/03/2018.
 */


class LedgerAdapter(val ledgerList: ArrayList<BankTransactionModel>): RecyclerView.Adapter<LedgerAdapter.ViewHolder>() {

    private val TAG: String = LedgerAdapter::class.java.simpleName

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val inputDate: Date = ledgerList[position].updatedAt
        Log.d(TAG+"Paul", "inputDate answer is : $inputDate")
        val formatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd" )
        val myTime: String = formatter.format(inputDate)
        val decimalMoney: String = moneyView(ledgerList[position].amount.toInt())
//        Log.d(TAG+"Paul", "answer is : $decimalMoney")

        holder.txtContent.text = ledgerList[position].description
        holder.txtSender.text = ledgerList[position].senderName
        holder.send_amount.text = decimalMoney
        holder.txt_date.text = myTime
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_wallet, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ledgerList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtSender: TextView get() = itemView.txt_person
        val txtContent: TextView get() = itemView.transaction_content
        val send_amount: TextView get() = itemView.txt_amount
        val txt_date: TextView get() = itemView.txt_date
    }

}
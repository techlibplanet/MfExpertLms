package net.rmitsolutions.mfexpert.lms.repayment.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.network.IRepayment
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentModel
import javax.inject.Inject

class ClientDetailAdapter : RecyclerView.Adapter<ClientViewHolder>() {

    var items: List<RepaymentModel> = emptyList()
    private lateinit var context: Context
    private lateinit var names: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.repayment_recycler_row, parent, false)
        return ClientViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bindView(context, items[position], position)
    }

    fun filterList(filterdNames: ArrayList<RepaymentModel>) {
        this.items = filterdNames
        notifyDataSetChanged()
    }
}
package net.rmitsolutions.mfexpert.lms.repayment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.rmitsolutions.mfexpert.lms.databinding.RepaymentActivityBinding
import net.rmitsolutions.mfexpert.lms.viewmodels.Repayment

class ClientAdapter : RecyclerView.Adapter<ClientViewHolder>() {

    var items: List<Repayment.RepaymentModel> = emptyList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val dataBinding = RepaymentActivityBinding.inflate(inflater, parent, false)
        return ClientViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val repaymentData = items[position]
        holder.bind(context, holder, repaymentData)
    }
}
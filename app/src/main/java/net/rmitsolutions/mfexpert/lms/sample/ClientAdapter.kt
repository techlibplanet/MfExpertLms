package net.rmitsolutions.mfexpert.lms.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.rmitsolutions.mfexpert.lms.databinding.RepaymentActivityBinding
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentModel

class ClientAdapter : RecyclerView.Adapter<ClientHolder>() {

    var items: List<RepaymentModel> = emptyList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val dataBinding = RepaymentActivityBinding.inflate(inflater, parent, false)
        return ClientHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ClientHolder, position: Int) {
        val repaymentData = items[position]
        holder.bind(holder,repaymentData)

    }
}
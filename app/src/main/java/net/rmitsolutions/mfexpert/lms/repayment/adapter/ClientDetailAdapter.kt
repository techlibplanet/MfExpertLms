package net.rmitsolutions.mfexpert.lms.repayment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsViewHolder
import net.rmitsolutions.mfexpert.lms.viewmodels.ClientViewModels
import net.rmitsolutions.mfexpert.lms.viewmodels.SyncViewModels

class ClientDetailAdapter : RecyclerView.Adapter<ClientViewHolder>() {

    var items: List<ClientViewModels> = emptyList()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.repayment_recycler_row, parent, false)
        return ClientViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bindView(context,items[position], position)
    }
}
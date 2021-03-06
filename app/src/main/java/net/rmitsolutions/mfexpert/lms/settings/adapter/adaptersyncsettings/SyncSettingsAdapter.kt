package net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.viewmodels.SyncViewModels

class SyncSettingsAdapter : RecyclerView.Adapter<SyncSettingsViewHolder>() {

    var items: List<SyncViewModels> = emptyList()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncSettingsViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.sync_settings_layout, parent, false)
        return SyncSettingsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size }

    override fun onBindViewHolder(holder: SyncSettingsViewHolder, position: Int) {
        holder.bindView(context,items[position], position)
    }

}
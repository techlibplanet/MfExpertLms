package net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersettings

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.viewmodels.SettingViewModel

class SettingsAdapter : RecyclerView.Adapter<SettingsViewHolder>() {

    var items: List<SettingViewModel> = emptyList()
    lateinit var  context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.setting_layout_rows, parent, false)
        return SettingsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bindView(context, items[position], position)
    }

}
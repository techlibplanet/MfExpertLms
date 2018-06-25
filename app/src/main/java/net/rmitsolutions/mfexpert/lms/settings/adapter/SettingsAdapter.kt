package net.rmitsolutions.mfexpert.lms.settings.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.viewmodels.SettingViewModel

class SettingsAdapter : RecyclerView.Adapter<SettingsViewHolder>() {

    private val TAG = SettingsAdapter::class.java.simpleName

    var items: List<SettingViewModel> = emptyList()
    lateinit var  context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.setting_layout_rows, parent, false)
        v.setOnClickListener(object :View.OnClickListener{
            override fun onClick(view: View?) {

            }

        })
        return SettingsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bindView(items[position])

    }
}
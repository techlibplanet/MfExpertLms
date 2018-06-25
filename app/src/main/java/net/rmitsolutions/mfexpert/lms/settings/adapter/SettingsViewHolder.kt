package net.rmitsolutions.mfexpert.lms.settings.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.viewmodels.SettingViewModel


class SettingsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    init {
        itemView.setOnClickListener(View.OnClickListener {
            Toast.makeText(itemView.context, "${itemView} Item Clicked", Toast.LENGTH_SHORT).show()
        })
    }

    fun bindView(settingViewModel: SettingViewModel){
        val textViewHeader = itemView.findViewById<TextView>(R.id.setting_header_name)
        val imageViewIcon = itemView.findViewById<ImageView>(R.id.setting_icon)

        textViewHeader.text = settingViewModel.header
        imageViewIcon.setImageResource(settingViewModel.icon)
    }



}
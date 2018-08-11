package net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersettings

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings.SyncSettingsFragment
import net.rmitsolutions.mfexpert.lms.viewmodels.SettingViewModel
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.toast


class SettingsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    private var context : Context? = null

    fun bindView(context : Context,settingViewModel: SettingViewModel, position : Int){
        this.context = context
        val textViewHeader = itemView.findViewById<TextView>(R.id.setting_header_name)
        val imageViewIcon = itemView.findViewById<ImageView>(R.id.setting_icon)
        textViewHeader.text = settingViewModel.header
        imageViewIcon.setImageResource(settingViewModel.icon)

        itemView.setOnClickListener{
            when(position){
                0 -> context.toast("Profile settings clicked.")
                1 -> showDataAndSyncFragment(it)
            }
        }
    }

    private fun showDataAndSyncFragment(it: View) {
        val manager = (context as AppCompatActivity).supportFragmentManager
        val syncSettingFragment = SyncSettingsFragment()
        switchToFragment(syncSettingFragment, manager)
    }

    // Switch UI to the given fragment
    private fun switchToFragment(newFrag: Fragment, manager: FragmentManager) {
        manager.beginTransaction().replace(R.id.fragment_container, newFrag)
                .commit()

    }

}
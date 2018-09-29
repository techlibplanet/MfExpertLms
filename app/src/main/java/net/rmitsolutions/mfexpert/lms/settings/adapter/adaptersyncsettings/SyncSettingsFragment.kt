package net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_BANKS_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_CASTE_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_DISTRICT_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_HOUSE_OWNERSHIP_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_INCOME_PROOF_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_LITERACY_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_LOAN_PURPOSE_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_OCCUPATION_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_PRIMARY_KYC_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_RELATION_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_RELIGION_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_SECONDARY_KYC_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.getPref
import net.rmitsolutions.mfexpert.lms.viewmodels.SyncViewModels

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SyncSettingsFragment : Fragment() {

    private lateinit var syncSettingRecyclerView : RecyclerView
    val adapter: SyncSettingsAdapter by lazy { SyncSettingsAdapter() }
    private lateinit var modelList: MutableList<SyncViewModels>

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_sync_settings, container, false)
        syncSettingRecyclerView = view.findViewById(R.id.setting_recycler_view)
        syncSettingRecyclerView.layoutManager = LinearLayoutManager(activity)
        syncSettingRecyclerView.setHasFixedSize(true)
        syncSettingRecyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))

        syncSettingRecyclerView.adapter = adapter
        modelList = mutableListOf<SyncViewModels>()
        setSettingsItem()
        return view
    }

    private fun setSettingsItem() {
        modelList.clear()
        modelList.add(SyncViewModels("District", activity?.getPref(SP_DISTRICT_SYNC_TIME,"")!!))
        modelList.add(SyncViewModels("Relation",activity?.getPref(SP_RELATION_SYNC_TIME,"")!!))
        modelList.add(SyncViewModels("Occupation",activity?.getPref(SP_OCCUPATION_SYNC_TIME,"")!!))
        modelList.add(SyncViewModels("Literacy",activity?.getPref(SP_LITERACY_SYNC_TIME,"")!!))
        modelList.add(SyncViewModels("Primary Kyc",activity?.getPref(SP_PRIMARY_KYC_SYNC_TIME,"")!!))
        modelList.add(SyncViewModels("Secondary Kyc",activity?.getPref(SP_SECONDARY_KYC_SYNC_TIME,"")!!))
        modelList.add(SyncViewModels("Loan Purpose",activity?.getPref(SP_LOAN_PURPOSE_SYNC_TIME,"")!!))
        modelList.add(SyncViewModels("Bank Details", activity?.getPref(SP_BANKS_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Caste Details", activity?.getPref(SP_CASTE_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("House Ownership", activity?.getPref(SP_HOUSE_OWNERSHIP_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Income Proof", activity?.getPref(SP_INCOME_PROOF_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Religion", activity?.getPref(SP_RELIGION_SYNC_TIME, "")!!))
        setRecyclerViewAdapter(modelList)
    }

    private fun setRecyclerViewAdapter(list: List<SyncViewModels>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SyncSettingsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}

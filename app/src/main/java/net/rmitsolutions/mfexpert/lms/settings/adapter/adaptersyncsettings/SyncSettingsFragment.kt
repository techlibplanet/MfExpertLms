package net.rmitsolutions.mfexpert.lms.settings.adapter.adaptersyncsettings

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_ASSIGN_CATEGORY_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_BANKS_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_CASTE_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_DISTRICT_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_HOUSE_OWNERSHIP_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_INCOME_PROOF_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_KYC_DETAILS_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_LITERACY_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_LOAN_CLOSE_TYPE_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_LOAN_PURPOSE_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_LOAN_REJECTION_REASON_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_MEMBER_REJECTION_REASON_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_NATIONALITY_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_OCCUPATION_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_PRIMARY_KYC_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_PRODUCTS_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_RELATION_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_RELIGION_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys.SP_SECONDARY_KYC_SYNC_TIME
import net.rmitsolutions.mfexpert.lms.helpers.getPref
import net.rmitsolutions.mfexpert.lms.viewmodels.SyncViewModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SyncSettingsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SyncSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SyncSettingsFragment : Fragment() {

    private lateinit var syncSettingRecyclerView : RecyclerView
    val adapter: SyncSettingsAdapter by lazy { SyncSettingsAdapter() }
    private lateinit var modelList: MutableList<SyncViewModels>

    // TODO: Rename and change types of parameters
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
        modelList.add(SyncViewModels("Kyc Details", activity?.getPref(SP_KYC_DETAILS_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Assign Category", activity?.getPref(SP_ASSIGN_CATEGORY_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Bank Details", activity?.getPref(SP_BANKS_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Caste Details", activity?.getPref(SP_CASTE_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("House Ownership", activity?.getPref(SP_HOUSE_OWNERSHIP_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Income Proof", activity?.getPref(SP_INCOME_PROOF_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Loan Close Type", activity?.getPref(SP_LOAN_CLOSE_TYPE_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Loan Rejection Reasons", activity?.getPref(SP_LOAN_REJECTION_REASON_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Member Rejection Reasons", activity?.getPref(SP_MEMBER_REJECTION_REASON_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Nationality", activity?.getPref(SP_NATIONALITY_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Products", activity?.getPref(SP_PRODUCTS_SYNC_TIME, "")!!))
        modelList.add(SyncViewModels("Religion", activity?.getPref(SP_RELIGION_SYNC_TIME, "")!!))
        setRecyclerViewAdapter(modelList)
    }

    private fun setRecyclerViewAdapter(list: List<SyncViewModels>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SyncSettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
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

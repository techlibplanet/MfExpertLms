package net.rmitsolutions.mfexpert.lms.repayment.adapter

import android.content.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.repayment.RepaymentDialogTabs
import net.rmitsolutions.mfexpert.lms.viewmodels.ClientViewModels
import org.jetbrains.anko.find

class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RepaymentDialogTabs.OnCompleteListener {

    private var context: Context? = null
    private lateinit var repaymentAmount : String


    fun bindView(context: Context, clientViewModels: ClientViewModels, position: Int) {
        this.context = context
        val clientId = itemView.find<TextView>(R.id.client_id)
        val clientName = itemView.find<TextView>(R.id.client_name)
        val editTextRepaymentAmount = itemView.find<EditText>(R.id.edit_text_repayment_amount)
        val cashChequeSwitch = itemView.find<Switch>(R.id.switch_cheque_cash)

        clientId.text = clientViewModels.clientId
        clientName.text = clientViewModels.clientName

        val repaymentButton = itemView.find<Button>(R.id.button_repayment)
        cashChequeSwitch.setOnClickListener {
            if (cashChequeSwitch.isChecked){
                cashChequeSwitch.text = "Cash"
            }else {
                cashChequeSwitch.text = "Cheque"
            }
        }

        repaymentButton.setOnClickListener {
            val manager = (context as AppCompatActivity).supportFragmentManager
            val ft = manager.beginTransaction()
            val prev = manager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            // Create and show the dialog.
            val dialogFragment = RepaymentDialogTabs()
            dialogFragment.show(ft, "dialog")
        }

    }

    override fun onComplete(textAmount: String) {
        logD("ClientViewHolder", "AmountText - $textAmount")
    }



//===================================================================================================================================
    // # Example
//    fun simpleDialog(){
//        repaymentButton.setOnClickListener {
//            val inflater = LayoutInflater.from(context)
//            val myCustomView = inflater.inflate(R.layout.repayment_tab_layout, null)
////            val textInputAmount = myCustomView.find<TextInputEditText>(R.id.textInputAmount)
////            repaymentAmount = editTextRepaymentAmount.text.toString()
////            textInputAmount.setText(repaymentAmount)
//            val alertDialog = AlertDialog.Builder(context).setTitle("Repayment").setView(myCustomView).setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
//                //                repaymentAmount = textInputAmount.text.toString()
////                editTextRepaymentAmount.setText(repaymentAmount)
//
//            }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
//                dialog.dismiss()
//            })
//            alertDialog.show()
//
//        }
//    }
//====================================================================================================================================



}
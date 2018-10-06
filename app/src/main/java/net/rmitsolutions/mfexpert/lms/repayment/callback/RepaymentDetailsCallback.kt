package net.rmitsolutions.mfexpert.lms.repayment.callback

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.helpers.apiAccessToken
import net.rmitsolutions.mfexpert.lms.helpers.processRequest
import net.rmitsolutions.mfexpert.lms.network.IRepayment
import org.jetbrains.anko.toast

class RepaymentDetailsCallback {

    private lateinit var listener: RepaymentDetailsListener
    private lateinit var compositeDisposable: CompositeDisposable

    fun setListener(repaymentDetailsListener: RepaymentDetailsListener) {
        this.listener = repaymentDetailsListener
        compositeDisposable = CompositeDisposable()
    }

    fun fetchMemberLoanDetails(context: Context, clientId: Long, repayService: IRepayment) {
        compositeDisposable.add(repayService.getMemberLoanDetails(context.apiAccessToken, clientId)
                .processRequest(context, { response ->
                    if (response.isSuccess!!) {
                        listener.onSuccess(response, clientId)
                    } else {
                        context.toast("${response.message}")
                    }
                }, { err ->
                    context.toast("Error - $err")
                }))
    }
}
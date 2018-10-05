package net.rmitsolutions.mfexpert.lms.viewmodels

import android.view.View

class ClientViewModels(val clientId:Long, val clientCode: String, val clientName : String)

interface ClientHandler{
    fun onClick(view: View)
}
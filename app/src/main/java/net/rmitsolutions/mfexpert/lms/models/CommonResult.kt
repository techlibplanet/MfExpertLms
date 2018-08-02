package net.rmitsolutions.mfexpert.lms.models

open class CommonResult {

    var isSuccess: Boolean? = null
    lateinit var message: String

    constructor()

    constructor(isSuccess: Boolean?, message: String) {
        this.isSuccess = isSuccess
        this.message = message
    }
}
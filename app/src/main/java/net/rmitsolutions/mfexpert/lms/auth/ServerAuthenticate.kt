package net.rmitsolutions.mfexpert.lms.auth

interface ServerAuthenticate {
    fun userSignUp(name: String, userName: String, pass: String, authType: String): String
    fun userSignIn(userName: String, pass: String, authType: String): String
}
package net.rmitsolutions.mfexpert.lms.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.accounts.AccountManager.KEY_BOOLEAN_RESULT
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.LoginActivity
import timber.log.Timber


class MfExpertAccountAuthenticator constructor(val context: Context) : AbstractAccountAuthenticator(context) {
    override fun getAuthTokenLabel(authTokenType: String?): String {
        if (Constants.AUTH_TOKEN_TYPE_FULL_ACCESS.equals(authTokenType)) {
            return Constants.AUTH_TOKEN_TYPE_FULL_ACCESS_LABEL;
        } else {
            return "$authTokenType (Label)";
        }
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        if (!authTokenType.equals(Constants.AUTH_TOKEN_TYPE_FULL_ACCESS)) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType")
            return result
        }

        val am = AccountManager.get(context)
        var authToken = am.peekAuthToken(account, authTokenType)
        Timber.d("peekAuthToken returned - " + authToken)

        if (TextUtils.isEmpty(authToken)) {
            val password = am.getPassword(account)
            if (password != null) {
                try {
                    Timber.d("re-authenticating with the existing password")
                    //authToken = sServerAuthenticate.userSignIn(account.name, password, authTokenType)
                    authToken = ""
                } catch (e: Exception) {
                }
            }
        }

        if (!TextUtils.isEmpty(authToken)) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return result
        }

        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(Constants.ARG_ACCOUNT_TYPE, account?.type)
        intent.putExtra(Constants.ARG_AUTH_TYPE, authTokenType)
        intent.putExtra(Constants.ARG_ACCOUNT_NAME, account?.name)
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        val result = Bundle()
        result.putBoolean(KEY_BOOLEAN_RESULT, false)
        return result
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String?, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        val intent = Intent(context, LoginActivity::class.java)
        intent.putExtra(Constants.ARG_ACCOUNT_TYPE, accountType)
        intent.putExtra(Constants.ARG_AUTH_TYPE, authTokenType)
        intent.putExtra(Constants.ARG_IS_ADDING_NEW_ACCOUNT, true)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? {
        return null;
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle? {
        return null;
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle? {
        return null;
    }
}
package net.rmitsolutions.mfexpert.lms.helpers

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import net.rmitsolutions.mfexpert.lms.R

/**
 * Created by Madhu on 14-Jun-2017.
 */

object SharedPrefKeys {
    const val SP_TOKEN_KEY = "sp_token_key"
    const val SP_ID_TOKEN_KEY = "sp_id_token_key"
    const val SP_ACCESS_TOKEN_KEY = "sp_access_token_key"
    var SP_ENCRYPTED_TOKEN_KEY = "sp_encrypted_token_key"
    var SP_ENCRYPTED_IV = "sp_encrypted_iv"
    const val SP_USER_KEY = "sp_user_key"
    const val SP_DISTRICT_SYNC_TIME = "district_sync_time"
    const val SP_RELATION_SYNC_TIME = "relation_sync_time"
    const val SP_OCCUPATION_SYNC_TIME = "occupation_sync_time"
    const val SP_LITERACY_SYNC_TIME = "literacy_sync_time"
    const val SP_PRIMARY_KYC_SYNC_TIME = "primary_kyc_sync_time"
    const val SP_SECONDARY_KYC_SYNC_TIME = "secondary_kyc_sync_time"
    const val SP_LOAN_PURPOSE_SYNC_TIME = "loan_purpose_sync_time"
    const val SP_KYC_DETAILS_SYNC_TIME = "kyc_details_sync_time"
    const val SP_ASSIGN_CATEGORY_SYNC_TIME = "assign_category_sync_time"
    const val SP_BANKS_SYNC_TIME = "assign_category_sync_time"
    const val SP_CASTE_SYNC_TIME = "caste_sync_time"
    const val SP_HOUSE_OWNERSHIP_SYNC_TIME = "house_ownership_sync_time"
    const val SP_INCOME_PROOF_SYNC_TIME = "income_proof_sync_time"
    const val SP_LOAN_CLOSE_TYPE_SYNC_TIME = "loan_close_type_sync_time"
    const val SP_LOAN_REJECTION_REASON_SYNC_TIME = "loan_rejection_reason_sync_time"
    const val SP_MEMBER_REJECTION_REASON_SYNC_TIME = "member_rejection_reason_sync_time"
    const val SP_NATIONALITY_SYNC_TIME = "nationality_sync_time"
    const val SP_PRODUCTS_SYNC_TIME = "products_sync_time"
    const val SP_RELIGION_SYNC_TIME = "religion_sync_time"
}

val Context.defaultSharedPreferences: SharedPreferences
    get() {
        return getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

fun Context.clearPrefs() {
    applyPref(prefEditor().clear())
}

fun <T> Context.putPref(key: String, value: T) {
    val editor = prefEditor()
    applyPref(addToPrefEditor(editor, key, value))
}

fun <T> Context.addToPrefEditor(editor: SharedPreferences.Editor, key: String, value: T): SharedPreferences.Editor {
    when (value) {
        is String -> editor.putString(key, value)
        is Boolean -> editor.putBoolean(key, value)
        is Float -> editor.putFloat(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        else -> throw Throwable("Type is not valid.")
    }
    return editor
}


fun Context.removePref(key: String) {
    applyPref(prefEditor().remove(key))
}

fun Context.removePref(vararg keys: String) {
    val editor = prefEditor()
    for (k in keys) {
        editor.remove(k)
    }
    applyPref(editor)
}

fun <T> Context.getPref(key: String, defaultValue: T): T {
    val prefs = this.defaultSharedPreferences
    val value: Any = when (defaultValue) {
        is String -> prefs.getString(key, defaultValue)
        is Boolean -> prefs.getBoolean(key, defaultValue)
        is Float -> prefs.getFloat(key, defaultValue)
        is Int -> prefs.getInt(key, defaultValue)
        is Long -> prefs.getLong(key, defaultValue)
        else -> throw Throwable("Type is not valid.")
    }
    return value as T
}

fun Context.prefEditor(): SharedPreferences.Editor {
    return defaultSharedPreferences.edit()
}

inline fun Context.addBulkPrefs(func: (editor: SharedPreferences.Editor) -> Unit) {
    val editor = prefEditor()
    func(editor)
    applyPref(editor)
}

fun Context.applyPref(editor: SharedPreferences.Editor) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
        editor.commit()
    } else {
        editor.apply()
    }
}

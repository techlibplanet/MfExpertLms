package net.rmitsolutions.mfexpert.lms.keystore

import android.content.Context
import android.os.Build
import android.security.keystore.KeyProperties
import android.security.keystore.KeyGenParameterSpec
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import android.util.Base64
import net.rmitsolutions.mfexpert.lms.helpers.SharedPrefKeys
import net.rmitsolutions.mfexpert.lms.helpers.putPref
import java.io.IOException
import java.security.*
import javax.crypto.*


internal class EnCryptor(private var context: Context) {

    var encryption: ByteArray? = null
        private set
    var iv: ByteArray? = null
        //private set

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(UnrecoverableEntryException::class, NoSuchAlgorithmException::class, KeyStoreException::class, NoSuchProviderException::class, NoSuchPaddingException::class, InvalidKeyException::class, IOException::class, InvalidAlgorithmParameterException::class, SignatureException::class, BadPaddingException::class, IllegalBlockSizeException::class)
    fun encryptText(alias: String, textToEncrypt: String): ByteArray {

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias))

        iv = cipher.iv
        encryption = cipher.doFinal(textToEncrypt.toByteArray(charset("UTF-8")))
        val key = Base64.encodeToString(encryption, Base64.DEFAULT)
        context.putPref(SharedPrefKeys.SP_ENCRYPTED_TOKEN_KEY, key)
        context.putPref(SharedPrefKeys.SP_ENCRYPTED_IV, Base64.encodeToString(iv, Base64.DEFAULT))
        return encryption as ByteArray
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @NonNull
    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    private fun getSecretKey(alias: String): SecretKey {

        val keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

        keyGenerator.init(KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build())

        return keyGenerator.generateKey()
    }

    companion object {
        private val TRANSFORMATION = "AES/GCM/NoPadding"
        private val ANDROID_KEY_STORE = "AndroidKeyStore"
    }
}
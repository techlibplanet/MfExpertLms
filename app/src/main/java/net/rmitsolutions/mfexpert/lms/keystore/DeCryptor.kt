package net.rmitsolutions.mfexpert.lms.keystore

import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.libcam.Constants.logE
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec


internal class DeCryptor @Throws(CertificateException::class, NoSuchAlgorithmException::class, KeyStoreException::class, IOException::class)
constructor() {

    private var keyStore: KeyStore? = null

    init {
        initKeyStore()
    }

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class)
    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore!!.load(null)
    }

    @Throws(UnrecoverableEntryException::class, NoSuchAlgorithmException::class, KeyStoreException::class, NoSuchProviderException::class, NoSuchPaddingException::class, InvalidKeyException::class, IOException::class, BadPaddingException::class, IllegalBlockSizeException::class, InvalidAlgorithmParameterException::class)
    fun decryptData(alias: String, encryptedData: ByteArray, encryptionIv: ByteArray): String? {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        if (encryptionIv.isNotEmpty()){
            val spec = GCMParameterSpec(128, encryptionIv)
            if (getSecretKey(alias)!=null){
                cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)
                return String(cipher.doFinal(encryptedData), charset("UTF-8"))
            }
            logE("DeCryptor", "Key is null! not saved in shared preference!")
            return null
        }
        logD("Decryptor","EncryptedIv lenghth is empty")
        return null

    }

    @Throws(NoSuchAlgorithmException::class, UnrecoverableEntryException::class, KeyStoreException::class)
    private fun getSecretKey(alias: String): SecretKey? {
        var secretKey = keyStore?.getEntry(alias, null)
        if (secretKey!=null){
            return (keyStore?.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
        }
        return null
    }


    companion object {
        private val TRANSFORMATION = "AES/GCM/NoPadding"
        private val ANDROID_KEY_STORE = "AndroidKeyStore"
    }
}
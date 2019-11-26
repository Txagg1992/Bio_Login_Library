package com.curiousca.biometriclibrary

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

@RequiresApi(Build.VERSION_CODES.M)
open class BiometricManagerV23(var context: Context){

    private val KEY_NAME = UUID.randomUUID().toString()

    private var cipher: Cipher? = null
    private var keyStore: KeyStore? = null
    private var keyGenerator: KeyGenerator? = null
    private var cryptoObject: FingerprintManagerCompat.CryptoObject? = null

    var title:String? = null
    var subTitle:String? = null
    var description:String? = null
    var negativeButtonText:String? = null

    private var biometricDialogV23: BiometricDialogV23? = null
    var mCancellationSignalV23 = androidx.core.os.CancellationSignal()

    fun displayBiometricPromptV23(biometricCallback: BiometricCallback){
        generateKey()

        if(initCipher()){
            cryptoObject = cipher?.let { FingerprintManagerCompat.CryptoObject(it) }
            val fingerprintManagerCompat = FingerprintManagerCompat.from(context)

            fingerprintManagerCompat.authenticate(
                cryptoObject, 0, mCancellationSignalV23,
                object : FingerprintManagerCompat.AuthenticationCallback(){
                    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                        super.onAuthenticationError(errMsgId, errString)
                        updateStatus(errString.toString())
                        if (errString != null){
                            biometricCallback.onAuthenticationError(errMsgId, errString as String)
                        }
                    }

                    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                        super.onAuthenticationHelp(helpMsgId, helpString)
                        updateStatus(helpString.toString())
                        if (helpString != null){
                            biometricCallback.onAuthenticationHelp(helpMsgId, helpString as String)
                        }
                    }

                    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                        super.onAuthenticationSucceeded(result)
                        dismissDialog()
                        biometricCallback.onAuthenticationSuccessful()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        updateStatus("Fingerprint not recognized")
                        biometricCallback.onAuthenticationFailed()
                    }
                }, null
            )
            displayBiometricDialog(biometricCallback)
        }
    }

    private fun initCipher(): Boolean{
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7)

        }catch (e: NoSuchAlgorithmException){
            throw RuntimeException("Failed to get Cipher", e)
        }catch (e: NoSuchPaddingException){
            throw RuntimeException("Failed to get Cipher", e)
        }

        return try {
            keyStore?.load(null)
            val key = keyStore?.getKey(KEY_NAME, null)as SecretKey
            cipher?.init(Cipher.ENCRYPT_MODE, key)
            true
        }catch (e: KeyPermanentlyInvalidatedException){
            false
        }catch (e: Exception){
            when (e){
                is KeyStoreException, is CertificateException, is UnrecoverableKeyException,
                    is IOException, is NoSuchAlgorithmException, is InvalidKeyException -> {
                    }else -> throw RuntimeException("Failed to Cipher", e)
            }
            return false
        }

    }

    private fun updateStatus(status : String){
        if (biometricDialogV23 != null){
            biometricDialogV23?.updateStatus(status)
        }
    }

    private fun dismissDialog(){
        if (biometricDialogV23 != null){
            biometricDialogV23?.dismiss()
        }
    }

    private fun displayBiometricDialog(biometricCallback: BiometricCallback){
        biometricDialogV23 = BiometricDialogV23(context, biometricCallback)
        biometricDialogV23?.setTitle(title!!)
        biometricDialogV23?.setSubtitle(subTitle!!)
        biometricDialogV23?.setDescription(description!!)
        biometricDialogV23?.setButtonText(negativeButtonText!!)
        biometricDialogV23?.setFingerImg()
        biometricDialogV23?.show()
    }

    private fun generateKey(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore?.load(null)

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator?.init(KeyGenParameterSpec.Builder(
                KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
            )

            keyGenerator?.generateKey()

        }catch (exc: Exception){
            when(exc){
                is KeyStoreException, is NoSuchAlgorithmException, is NoSuchProviderException,
                    is InvalidAlgorithmParameterException, is CertificateException, is IOException ->{
                }else -> exc.printStackTrace()
            }

        }
    }
}
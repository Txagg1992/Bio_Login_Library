package com.curiousca.biometriclibrary

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

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
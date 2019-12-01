package com.curiousca.biometriclibrary

import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.annotation.RequiresApi

class BiometricManager(biometricBuilder: BiometricBuilder, context: Context) :
    BiometricManagerV23(context) {

    private var mCancellationSignal = android.os.CancellationSignal()

    init {
        this.context = biometricBuilder.context
        this.title = biometricBuilder.title
        this.subTitle = biometricBuilder.subtitle
        this.description = biometricBuilder.description
        this.negativeButtonText = biometricBuilder.negativeButtonText
    }

    fun authenticate(biometricCallback: BiometricCallback) {

        if (title == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog Title cannot be null")
            return
        }
        if (subTitle == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog Subtitle cannot be null")
            return
        }
        if (description == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog Description cannot be null")
            return
        }
        if (negativeButtonText == null) {
            biometricCallback.onBiometricAuthenticationInternalError("Biometric Dialog Negative Button Text cannot be null")
            return
        }
        if (!BiometricUtilities.isSdkVersionSupported()) {
            biometricCallback.onSDKVersionNotSupported()
            return
        }
        if (!BiometricUtilities.isPermissionGranted(context)) {
            biometricCallback.onBiometricAuthenticationPermissionNotGranted()
            return
        }
        if (!BiometricUtilities.isHardwareSupported(context)) {
            biometricCallback.onBiometricAuthenticationNotSupported()
            return
        }
        if (!BiometricUtilities.isFingerprintAvailable(context)) {
            biometricCallback.onBiometricAuthenticationNotAvailable()
            return
        }

        displayBiometricDialog(biometricCallback)
    }

    private fun displayBiometricDialog(biometricCallback: BiometricCallback) {
        if (BiometricUtilities.isBiometricEnabled()) {
            displayBiometricPrompt(biometricCallback)
        } else {
            displayBiometricPromptV23(biometricCallback)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun displayBiometricPrompt(biometricCallback: BiometricCallback) {
        BiometricPrompt.Builder(context)
            .setTitle(title.toString())
            .setSubtitle(subTitle.toString())
            .setDescription(description.toString())
            .setNegativeButton(
                negativeButtonText.toString(), context.mainExecutor,
                DialogInterface.OnClickListener { dialogInterface, i -> biometricCallback.onAuthenticationCancelled() })
            .build()
            .authenticate(
                mCancellationSignal, context.mainExecutor,
                BiometricCallbackV28(biometricCallback)
            )
    }

    fun cancelAuthentication() {
        if (BiometricUtilities.isBiometricEnabled()) {
            if (!mCancellationSignal.isCanceled)
                mCancellationSignal.cancel()
        } else {
            if (!mCancellationSignalV23.isCanceled)
                mCancellationSignalV23.cancel()
        }
    }

    class BiometricBuilder(val context: Context) {

        var title: String? = null
        var subtitle: String? = null
        var description: String? = null
        var negativeButtonText: String? = null

        fun setTitle(title: String): BiometricBuilder {
            this.title = title
            return this
        }

        fun setSubtitle(subtitle: String): BiometricBuilder {
            this.subtitle = subtitle
            return this
        }

        fun setdescription(description: String): BiometricBuilder {
            this.description = description
            return this
        }

        fun setNegativeButtonText(negativeButtonText: String): BiometricBuilder {
            this.negativeButtonText = negativeButtonText
            return this
        }

        fun build(): BiometricManager {
            return BiometricManager(this, context)
        }
    }
}
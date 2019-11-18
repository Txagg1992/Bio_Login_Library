package com.curiousca.biometriclibrary

interface BiometricCallback {

    fun onSDKVersionNotSupported()

    fun onBiometricAuthenticationNotSupported()

    fun onBiometricAuthenticationNotAvailable()

    fun onBiometricAuthenticationPermissionNotGranted()

    fun onBiometricAuthenticationInternalError(error: String)

    fun onAuthenticationFailed()

    fun onAuthenticationCancelled()

    fun onAuthenticationSuccessful()

    fun onAuthenticationHelp(helpCode: Int, helpString: String)

    fun onAuthenticationError(errorCode: Int, errString: String)
}
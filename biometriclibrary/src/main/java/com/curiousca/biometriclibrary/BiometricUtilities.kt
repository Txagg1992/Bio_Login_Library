package com.curiousca.biometriclibrary

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

object BiometricUtilities {

    /*
    Check if biometrics is enabled on the current os version : version P
     */
    fun isBiometricEnabled(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }
    /*
    Conditional check 1: Check that the os is higher that Android 6.0 or Marshmallow
     */
    fun isSdkVersionSupported(): Boolean{
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
    /*
    Conditional check 2: Check that device has fingerprint sensor
     */
    fun isHardwareSupported(context: Context): Boolean{
        val fingerprintManager = FingerprintManagerCompat.from(context)
        return fingerprintManager.isHardwareDetected
    }
    /*
    Conditional check 3: Check that user has fingerprint registered
     */
    fun isFingerprintAvailable(context: Context): Boolean{
        val fingerprintManager = FingerprintManagerCompat.from(context)
        return fingerprintManager.hasEnrolledFingerprints()
    }
    /*
    Conditional check 4: Check that fingerprint permission has been granted
     */
    fun isPermissionGranted(context: Context): Boolean{
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.USE_FINGERPRINT) ==
                PackageManager.PERMISSION_GRANTED
    }
}
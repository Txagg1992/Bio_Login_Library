package com.curiousca.bio_library

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.curiousca.biometriclibrary.BiometricCallback
import com.curiousca.biometriclibrary.BiometricManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BiometricCallback {

    private lateinit var mBiometricManager: BiometricManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews(){

        log_in_button.setOnClickListener {

            mBiometricManager = BiometricManager.BiometricBuilder(this@MainActivity)
                .setTitle("Your Title Here")
                .setSubtitle("Your Subtitle Here")
                .setdescription("Your Description Here")
                .setNegativeButtonText("Cancel/ Use Password")
                .build()

            mBiometricManager.authenticate(this@MainActivity)
        }
    }

    override fun onSDKVersionNotSupported() {
        Toast.makeText(applicationContext, "SDK version not supported.", Toast.LENGTH_LONG).show()
    }

    override fun onBiometricAuthenticationNotSupported() {
        Toast.makeText(applicationContext, "Device does not support biometric authentication.", Toast.LENGTH_LONG).show()
    }

    override fun onBiometricAuthenticationNotAvailable() {
        Toast.makeText(applicationContext, "Fingerprint is not registered on this device.", Toast.LENGTH_LONG).show()
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(applicationContext, "Permission is not granted by user", Toast.LENGTH_LONG).show()
    }

    override fun onBiometricAuthenticationInternalError(error: String) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationFailed() {
       // Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationCancelled() {
        Toast.makeText(applicationContext, "Please enter your password.", Toast.LENGTH_LONG).show()
        mBiometricManager.cancelAuthentication()
    }

    override fun onAuthenticationSuccessful() {
        Toast.makeText(applicationContext, "Login success.", Toast.LENGTH_LONG).show()

        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: String) {
        Toast.makeText(applicationContext, helpString, Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationError(errorCode: Int, errString: String) {
        //Toast.makeText(applicationContext, errString, Toast.LENGTH_LONG).show()
    }
}

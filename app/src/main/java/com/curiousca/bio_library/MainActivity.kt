package com.curiousca.bio_library

import android.os.Bundle
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
                .setdescription("Your Decsription Here")
                .setNegativeButtonText("Cancel/ Use Password")
                .build()

            mBiometricManager.authenticate(this@MainActivity)
        }
    }

    override fun onSDKVersionNotSupported() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBiometricAuthenticationNotSupported() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBiometricAuthenticationNotAvailable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBiometricAuthenticationInternalError(error: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthenticationFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthenticationCancelled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthenticationSuccessful() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAuthenticationError(errorCode: Int, errString: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

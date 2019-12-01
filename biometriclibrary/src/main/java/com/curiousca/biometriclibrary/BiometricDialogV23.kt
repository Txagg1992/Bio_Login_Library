package com.curiousca.biometriclibrary

import android.content.Context
import android.view.View
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.view_bottom_sheet.*

class BiometricDialogV23(@NonNull context: Context, biometricCallback: BiometricCallback)
    : BottomSheetDialog(context, R.style.BottomSheetDialogTheme), View.OnClickListener{

    private var biometricCallback:BiometricCallback? = null
    init {
        this.biometricCallback = biometricCallback
        setDialogView()
    }

    private fun setDialogView(){
        val bottomSheetView: View = layoutInflater.inflate(R.layout.view_bottom_sheet, null)
        setContentView(bottomSheetView)

        button_cancel.setOnClickListener(this)

        updateLogo()
        setFingerImg()
    }

    fun setTitle(title: String){
        item_title.text = title
    }

    fun setSubtitle(subtitle: String){
        item_subtitle.text = subtitle
    }

    fun setDescription(description: String){
        item_description.text = description
    }

    fun updateStatus(status: String){
        item_status.text = status
    }

    fun setButtonText(negativeButtonText: String){
        button_cancel.text = negativeButtonText
    }

    fun setFingerImg(){
        if (image_fingerprint != null){
            try {
                this.image_fingerprint.setImageResource(R.drawable.ic_fingerprint)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateLogo(){
        try {
            this.image_logo.setImageResource(R.drawable.ic_droid)
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    fun updateFingerImg(){
        this.image_fingerprint.setImageResource(R.drawable.ic_error_outline)
        Thread.sleep(2000)
        this.image_fingerprint.setImageResource(R.drawable.ic_fingerprint)
    }

    override fun onClick(p0: View?) {
        dismiss()
        biometricCallback?.onAuthenticationCancelled()
    }
}
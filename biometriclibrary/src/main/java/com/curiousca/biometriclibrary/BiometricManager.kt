package com.curiousca.biometriclibrary

import android.content.Context

class BiometricManager(biometricBuilder : BiometricBuilder,  context: Context) : BiometricManagerV23(context) {

     class BiometricBuilder(val context: Context){

         var title: String? = null
         var subtitle: String? = null
         var description: String? = null
         var negativeButtonText: String? = null

         fun setTitle(title: String): BiometricBuilder{
             this.title = title
             return this
         }
         fun setSubtitle(subtitle: String): BiometricBuilder{
             this.subtitle = subtitle
             return this
         }
         fun setdescription(description: String): BiometricBuilder{
             this.description = description
             return this
         }
         fun setNegativeButtonText(negativeButtonText: String): BiometricBuilder{
             this.negativeButtonText = negativeButtonText
             return this
         }
         fun build(): BiometricManager{
             return BiometricManager(this, context)
         }
     }
}
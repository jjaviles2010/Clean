package com.fiap18Mob.clean.view.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel (val mAuth: FirebaseAuth): ViewModel() {

    var isResetOk: MutableLiveData<Boolean> = MutableLiveData()

    fun forgotPassword(emailAddress: String) {

        mAuth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                isResetOk.value = task.isSuccessful
            }
    }
}
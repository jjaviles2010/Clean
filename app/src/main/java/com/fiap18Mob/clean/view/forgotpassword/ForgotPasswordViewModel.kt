package com.fiap18Mob.clean.view.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiap18Mob.clean.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {

    private lateinit var mAuth: FirebaseAuth
    var isResetOk: MutableLiveData<Boolean> = MutableLiveData()

    fun forgotPassword(emailAddress: String) {
        mAuth = FirebaseAuth.getInstance()

        mAuth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                isResetOk.value = task.isSuccessful
            }
    }
}
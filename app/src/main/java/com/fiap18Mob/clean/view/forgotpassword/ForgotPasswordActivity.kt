package com.fiap18Mob.clean.view.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18mob.mylib.CustomToast
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.koin.android.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : BaseActivity() {

    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModel()
    private val customToast = CustomToast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotPasswordViewModel.isResetOk.observe(this, Observer {
            var msg: String
            if (it) {
                msg = getString(R.string.resetEmailSent)
            } else {
                msg = getString(R.string.errorTryAgain)
            }

            customToast.showToast(this@ForgotPasswordActivity, msg, CustomToast.ERROR)

            if (it) {
                val handle = Handler()
                handle.postDelayed({
                    finish()
                }, 2000)
            }
        })

        btResetPassword.setOnClickListener {
            if (edEmailReset.text.toString().trim().isNotEmpty()) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(edEmailReset.text.toString().trim()).matches()) {
                    forgotPasswordViewModel.forgotPassword(edEmailReset.text.toString().trim())
                } else {
                    customToast.showToast(this@ForgotPasswordActivity, getString(R.string.invalidEmail), CustomToast.ERROR)
                }
            } else {
                customToast.showToast(this@ForgotPasswordActivity, getString(R.string.emailRequired), CustomToast.ERROR)
            }
        }
    }
}

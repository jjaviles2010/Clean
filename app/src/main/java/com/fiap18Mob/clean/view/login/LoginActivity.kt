package com.fiap18Mob.clean.view.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.utils.DatabaseUtil
import com.fiap18Mob.clean.utils.RemoteConfig
import com.fiap18Mob.clean.view.forgotpassword.ForgotPasswordActivity
import com.fiap18Mob.clean.view.main.MainActivity
import com.fiap18Mob.clean.view.signup.SignUpActivity
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val newUserRequestCode = 1
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel.initialAuth()
        loginViewModel.alreadyAuth.observe(this, Observer {
            if (it) {
                goToMain()
            }
        })

        loginViewModel.messageError.observe(this, Observer {
            if (it != null) {
                Toast.makeText(
                    this@LoginActivity, it,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        btEnter.setOnClickListener {
            if (edEmail.text.toString().trim().isNotEmpty() && edPassword.text.toString().trim().isNotEmpty()) {
                loginViewModel.auth(edEmail.text.toString().trim(), edPassword.text.toString().trim())
                loginViewModel.authorized.observe(this, Observer {
                    if (it) {
                        goToMain()
                    }
                })
            } else {
                Toast.makeText(
                    this@LoginActivity, getString(R.string.emailPasswordRequired), Toast.LENGTH_SHORT
                ).show()
            }
        }

        btSingupClient.setOnClickListener {
            goToSignUp("CLIENT")
        }

        btSingupCleaner.setOnClickListener {
            goToSignUp("CLEANER")
        }

        tvForgotPassword.setOnClickListener {
            goResetPassword()
        }

        RemoteConfig.remoteConfigFetch()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    RemoteConfig.getFirebaseRemoteConfig().activateFetched()

                    val isClientRegistrationActive = RemoteConfig.getFirebaseRemoteConfig()
                        .getLong("is_client_registration_active")
                        .toInt()
                    if (isClientRegistrationActive != 1) {
                        btSingupClient.visibility = View.INVISIBLE
                    }

                    val isCleanerRegistrationActive = RemoteConfig.getFirebaseRemoteConfig()
                        .getLong("is_cleaner_registration_active")
                        .toInt()
                    if (isCleanerRegistrationActive != 1) {
                        btSingupCleaner.visibility = View.INVISIBLE
                    }

                    if (isCleanerRegistrationActive != 1 && isClientRegistrationActive != 1) {
                        tvFirstTimeHere.visibility = View.INVISIBLE
                    }
                }
            }

    }

    private fun goToSignUp(userType: String) {
        val signUpIntent = Intent(this, SignUpActivity::class.java)
        signUpIntent.putExtra("USER_TYPE", userType)
        startActivityForResult(signUpIntent, newUserRequestCode)
    }

    private fun goToMain() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) {
                instanceIdResult ->
            val newToken = instanceIdResult.token
            DatabaseUtil.saveToken(newToken)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun goResetPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newUserRequestCode && resultCode == Activity.RESULT_OK) {
            edEmail.setText(data?.getStringExtra("email"))
        }
    }
}

package com.fiap18Mob.clean.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.crashlytics.android.Crashlytics
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.utils.CleanTracker
import com.fiap18Mob.clean.view.forgotpassword.ForgotPasswordActivity
import com.fiap18Mob.clean.view.main.MainActivity
import com.fiap18Mob.clean.view.signup.SignUpActivity
import com.fiap18mob.mylib.CustomToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.include_loading.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private val newUserRequestCode = 1
    private val loginViewModel: LoginViewModel by viewModel()
    private val customToast = CustomToast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel.initialAuth()

        configureObservers()
        configureListeners()

        loginViewModel.checkRemoteConfig()
    }

    private fun configureObservers() {
        loginViewModel.isLoading.observe(this, Observer {
            if (it == true) {
                containerLoading.visibility = View.VISIBLE
            } else {
                containerLoading.visibility = View.GONE
            }
        })

        loginViewModel.alreadyAuth.observe(this, Observer {
            if (it) {
                goToMain()
            }
        })

        loginViewModel.messageError.observe(this, Observer {
            if (it != null) {
                customToast.showToast(this@LoginActivity, it, CustomToast.ERROR)
            }
        })

        loginViewModel.clientRegistrationActive.observe(this, Observer {
            if (it != null && !it) {
                btSingupClient.visibility = View.INVISIBLE
                if (btSingupCleaner.visibility == View.INVISIBLE) {
                    tvFirstTimeHere.visibility = View.INVISIBLE
                }
            }
        })

        loginViewModel.cleanerRegistrationActive.observe(this, Observer {
            if (it != null && !it) {
                btSingupCleaner.visibility = View.INVISIBLE
                if (btSingupClient.visibility == View.INVISIBLE) {
                    tvFirstTimeHere.visibility = View.INVISIBLE
                }
            }
        })

        loginViewModel.authorized.observe(this, Observer {
            if (it) {
                //Grava o usuário que fez o login.
                val bundle = Bundle()
                bundle.putString("EVENT_NAME", "LOGIN")
                bundle.putString("USER", edEmail.text.toString());
                CleanTracker.trackEvent(this, bundle)
                goToMain()
            }
        })

    }

    private fun configureListeners() {
        btEnter.setOnClickListener {
            if (edEmail.text.toString().trim().isNotEmpty() && edPassword.text.toString().trim().isNotEmpty()) {
                loginViewModel.auth(edEmail.text.toString().trim(), edPassword.text.toString().trim())
            } else {
                customToast.showToast(this@LoginActivity, getString(R.string.emailPasswordRequired), CustomToast.ERROR)
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

        btCrash.setOnClickListener {
            Crashlytics.log("Erro na tela de login.")
            Crashlytics.getInstance().crash()
        }
    }

    private fun goToSignUp(userType: String) {
        val signUpIntent = Intent(this, SignUpActivity::class.java)
        signUpIntent.putExtra("USER_TYPE", userType)
        startActivityForResult(signUpIntent, newUserRequestCode)
    }

    private fun goToMain() {
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

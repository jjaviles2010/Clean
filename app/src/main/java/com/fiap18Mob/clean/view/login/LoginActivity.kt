package com.fiap18Mob.clean.view.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.view.main.MainActivity
import com.fiap18Mob.clean.view.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private val newUserRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            //goToMain()
        }
        btEnter.setOnClickListener {
            mAuth.signInWithEmailAndPassword(
                edEmail.text.toString(),
                edPassword.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    goToMain()
                } else {
                    Toast.makeText(
                        this@LoginActivity, it.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btSingupClient.setOnClickListener {
            goToSignUp("CLIENT")
        }

        btSingupCleaner.setOnClickListener {
            goToSignUp("CLEANER")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newUserRequestCode && resultCode == Activity.RESULT_OK) {
            edEmail.setText(data?.getStringExtra("email"))
        }
    }
}

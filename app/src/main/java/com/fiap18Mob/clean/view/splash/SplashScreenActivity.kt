package com.fiap18Mob.clean.view.splash

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.view.login.LoginActivity
import com.fiap18Mob.clean.view.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handle = Handler()
        handle.postDelayed(Runnable { mostrarLogin() }, 5000)

    }

    public fun mostrarLogin() {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}

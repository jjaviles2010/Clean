package com.fiap18Mob.clean.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.view.about.AboutActivity
import com.fiap18Mob.clean.view.cleanerDetail.CleanerDetailActivity
import com.fiap18Mob.clean.view.listServices.ListServicesActivity
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btSchedules.setOnClickListener {
            val intent = Intent(this@MainActivity, ListServicesActivity::class.java)
            startActivity(intent)
        }
    }


    fun showAbout(view: View) {
        startActivity(Intent(this@MainActivity, AboutActivity::class.java))
    }

}
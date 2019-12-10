package com.fiap18Mob.clean.view.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.utils.PermissionUtils
import com.fiap18Mob.clean.view.about.AboutActivity
import com.fiap18Mob.clean.view.usersList.UsersListViewActivity
import com.fiap18Mob.clean.view.listServices.ListServicesActivity
import com.fiap18Mob.clean.view.maps.MapsActivity
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Solicita as permissões
            PermissionUtils.validate(this, 0,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        btSearch.setOnClickListener {
            startActivity(Intent(this@MainActivity, UsersListViewActivity::class.java))
        }

        btSchedules.setOnClickListener {
            val intent = Intent(this@MainActivity, ListServicesActivity::class.java)
            startActivity(intent)
        }

        btViewMap.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }

        fun showAbout(view: View) {
            startActivity(Intent(this@MainActivity, AboutActivity::class.java))
        }

    }
}
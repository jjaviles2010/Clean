package com.fiap18Mob.clean.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.fiap18Mob.clean.BaseActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.utils.PermissionUtils
import com.fiap18Mob.clean.view.about.AboutActivity
import com.fiap18Mob.clean.view.usersList.UsersListViewActivity
import com.fiap18Mob.clean.view.listServices.ListServicesActivity
import com.fiap18Mob.clean.view.maps.MapsActivity
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : BaseActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()


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

        btViewAbout.setOnClickListener {
            val intent = Intent(this@MainActivity, AboutActivity::class.java)
            startActivity(intent)
        }

        fun showAbout(view: View) {
            startActivity(Intent(this@MainActivity, AboutActivity::class.java))
        }

    }


    override fun onDestroy() {
         mAuth.signOut()
        super.onDestroy()
    }

}
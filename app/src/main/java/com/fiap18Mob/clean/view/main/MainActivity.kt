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

class MainActivity : BaseActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    var timeMillisId: Long = 0

    private var mGoogleApiClient: GoogleApiClient? = null

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAuth = FirebaseAuth.getInstance()

        // Configura o objeto GoogleApiClient
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        // Solicita as permissões
        PermissionUtils.validate(
            this, 0,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

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


        val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("geo/providers")

        val geofire = GeoFire(db)

        //### set Location

        // Atualiza a localização atual
        fun setMapLocation(l: Location?) {
            if (l != null) {
                val Lat = l.latitude
                val Lng = l.longitude

                timeMillisId = System.currentTimeMillis()

                geofire.setLocation(
                    timeMillisId.toString(),
                    GeoLocation(Lat, Lng),
                    GeoFire.CompletionListener { key, error ->
                        if (error == null)
                            Log.i("TAG", "geo added successful: " + key)
                        else
                            Log.i("TAG", "geo added error: " + error.message)
                    })
            }


            db.child(timeMillisId.toString()).onDisconnect().removeValue()
        }


        // Listener para monitorar o GPS
        val locationCallback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                // Nova localizaçao: atualiza a interface
                setMapLocation(location)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Conecta no Google Play Services
        mGoogleApiClient?.connect()
    }

    override fun onStop() {
        // Para o GPS
        stopLocationUpdates()
        // Desconecta
        mGoogleApiClient?.disconnect()
        super.onStop()
    }

    override fun onConnected(bundle: Bundle?) {
        // Inicia o GPS
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates()")
        val locRequest = LocationRequest.create()
        locRequest.interval = 10000
        locRequest.fastestInterval = 5000
        locRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //val locClient = LocationServices.getFusedLocationProviderClient(this)
        //locClient.requestLocationUpdates(locRequest, locationCallback, Looper.myLooper())
    }

    fun stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates()")
        //val locClient = LocationServices.getFusedLocationProviderClient(this)
        //locClient.removeLocationUpdates(locationCallback)
    }

    //### listener for disconnect
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                alertAndFinish()
                return
            }
        }
        // Se chegou aqui está OK :-)
    }

    private fun alertAndFinish() {
        run {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name)
                .setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.")
            // Add the buttons
            builder.setPositiveButton("OK") { dialog, id -> finish() }
            val dialog = builder.create()
            dialog.show()
        }
    }


    override fun onConnectionSuspended(cause: Int) {
        toast("Conexão interrompida.")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        toast("Erro ao conectar: " + connectionResult)
    }

    private fun toast(s: String) {
        Toast.makeText(baseContext, s, Toast.LENGTH_SHORT).show()
    }


    companion object {
        private val TAG = "Clean"
    }


    override fun onDestroy() {
         mAuth.signOut()
        super.onDestroy()
    }

}
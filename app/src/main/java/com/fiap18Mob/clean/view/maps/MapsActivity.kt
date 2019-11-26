package com.fiap18Mob.clean.view.maps
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.utils.PermissionUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import java.text.DateFormat
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private var map: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        setContentView(R.layout.activity_maps)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)

        // Configura o objeto GoogleApiClient
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        // Solicita as permissões
        PermissionUtils.validate(this, 0,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
            builder.setTitle(R.string.app_name).setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.")
            // Add the buttons
            builder.setPositiveButton("OK") { dialog, id -> finish() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        Log.d(TAG, "onMapReady: " + map)
        this.map = map

        // Configura o tipo do mapa
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
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
        toast("Conectado no Google Play Services!")

        // Inicia o GPS
        startLocationUpdates()
    }

    override fun onConnectionSuspended(cause: Int) {
        toast("Conexão interrompida.")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        toast("Erro ao conectar: " + connectionResult)
    }

    // Atualiza a coordenada do mapa
    private fun setMapLocation(l: Location?) {
        if (map != null && l != null) {
            val latLng = LatLng(l.latitude, l.longitude)
            val update = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
            map?.animateCamera(update)

            Log.d(TAG, "setMapLocation: " + l)

            // Desenha uma bolinha vermelha
            val circle = CircleOptions().center(latLng)
            circle.fillColor(Color.RED)
            circle.radius(25.0) // Em metros
            map?.clear()
            map?.addCircle(circle)
        }
    }

    private fun toast(s: String) {
        Toast.makeText(baseContext, s, Toast.LENGTH_SHORT).show()
    }

    // Listener para monitorar o GPS
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            // Nova localizaçao: atualiza a interface
            setMapLocation(location)

        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates()")
        val locRequest = LocationRequest.create()
        locRequest.interval = 10000
        locRequest.fastestInterval = 5000
        locRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locClient = LocationServices.getFusedLocationProviderClient(this)
        locClient.requestLocationUpdates(locRequest, locationCallback, Looper.myLooper())
    }

    private fun stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates()")
        val locClient = LocationServices.getFusedLocationProviderClient(this)
        locClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private val TAG = "Clean"
    }
}

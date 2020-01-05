package com.fiap18Mob.clean.view.maps
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fiap18Mob.clean.R
import com.fiap18Mob.clean.model.User
import com.fiap18Mob.clean.utils.PermissionUtils
import com.fiap18mob.mylib.CustomToast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import java.text.DateFormat
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private var map: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    val mapsViewModel: MapsViewModel by viewModel()
    val customToast: CustomToast by inject()

    private lateinit var locCallback: LocationCallback
    private lateinit var locRequest: LocationRequest
    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    lateinit var cleanerList: List<User>

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

        configureObservers()

        createLocationRequest()
    }

    private fun configureObservers() {
        mapsViewModel.users.observe(this, Observer {
            cleanerList = it
            setupMarkForCleaner()
        })
    }

    private fun setupMarkForCleaner() {
        cleanerList.forEach {
            map?.addMarker(
                MarkerOptions().position(LatLng(it.latitude, it.longitude))
                    .title("${it.nome}, tel:${it.phoneNumber}")
                    .icon(bitmapDescriptorFromVector(this, R.drawable.ic_person_pin_circle_black_24dp))
            )
        }
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
        this.map = map

        // Configura o tipo do mapa
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        mapsViewModel.getUserList()
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

            // Desenha uma bolinha vermelha
            val circle = CircleOptions().center(latLng)
            circle.fillColor(Color.RED)
            circle.radius(25.0) // Em metros
            map?.clear()
            map?.addCircle(circle)

            if (mapsViewModel.users.value != null)
                setupMarkForCleaner()
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
        val locClient = LocationServices.getFusedLocationProviderClient(this)
        locClient.requestLocationUpdates(locRequest, locationCallback, Looper.myLooper())
    }

    private fun createLocationRequest() {
        locRequest = LocationRequest.create()
        locRequest.interval = 60000
        locRequest.fastestInterval = 5000
        locRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@MapsActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }


    private fun stopLocationUpdates() {
        val locClient = LocationServices.getFusedLocationProviderClient(this)
        locClient.removeLocationUpdates(locationCallback)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

}

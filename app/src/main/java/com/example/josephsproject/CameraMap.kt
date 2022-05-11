package com.example.josephsproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson


class CameraMap : AppCompatActivity(), OnMapReadyCallback {

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var map: GoogleMap? = null

    private var locationPermissionGranted = false

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var cameraPosition: CameraPosition? = null



    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_map)

        fusedLocationProviderClient = getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }


            }
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        val queue = Volley.newRequestQueue(this)
        val url = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2"
        var gson = Gson()


        val cm = getSystemService(ConnectivityManager::class.java)
        val info = cm.activeNetworkInfo

        val avail = info?.isAvailable
        val connected = info?.isConnected
        var camList = ArrayList<Camera>()

        if (info != null && avail == true && connected == true) {

            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    val camJsonArray = response.getJSONArray("Features")
                    for (i in 0 until camJsonArray.length()) {
                        var jsonEle = camJsonArray.getJSONObject(i)
                        var camcoord = jsonEle.getJSONArray("PointCoordinate")
                        var camjson = jsonEle.getJSONArray("Cameras").getJSONObject(0)
                        var camId = camjson.getString("Id")
                        var camDesc = camjson.getString("Description")
                        var camUrl = camjson.getString("ImageUrl")
                        var camType = camjson.getString("Type")

                        if (camType.equals("sdot")) {
                            camUrl = "https://www.seattle.gov/trafficcams/images/" + camUrl
                        } else {
                            camUrl = "https://images.wsdot.wa.gov/nw/" + camUrl
                        }


                        var tempCam = Camera(camId, camDesc, camUrl, camType, camcoord)
                        camList.add(tempCam)
                        Log.e("Traffic", "Camera added to list")


                    }
                    for (i in 0 until camList.size)
                    {
                        var tempCam = camList.get(i)
                        var pos = LatLng(tempCam.Coords[0] as Double, tempCam.Coords[1] as Double)
                        map.addMarker(
                            MarkerOptions()
                                .position(pos)
                                .title(tempCam.Description)
                        )
                        map.moveCamera(CameraUpdateFactory.newLatLng(pos))

                    }
//        val sydney = LatLng(-33.852, 151.211)
//        map.addMarker(
//            MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney")
//        )
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                    // ...

                    // Turn on the My Location layer and the related control on the map.

                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                }
            )








// Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)

        }
        else{
            Toast.makeText(this,"No internet/connection available - try again!", Toast.LENGTH_LONG).show()
        }

        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }







    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            var locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }


    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }
}
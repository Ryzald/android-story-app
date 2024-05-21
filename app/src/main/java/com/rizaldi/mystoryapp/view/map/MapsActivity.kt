package com.rizaldi.mystoryapp.view.map

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.rizaldi.mystoryapp.R
import com.rizaldi.mystoryapp.ViewModelFactory
import com.rizaldi.mystoryapp.data.Result
import com.rizaldi.mystoryapp.data.response.StoryItem
import com.rizaldi.mystoryapp.databinding.ActivityMapsBinding
import com.rizaldi.mystoryapp.util.TextMessage
import com.rizaldi.mystoryapp.view.main.MainViewModel

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: com.rizaldi.mystoryapp.databinding.ActivityMapsBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupMenuClickListener()
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
//        addManyMarker2()

        // Menambahkan observer untuk daftar cerita
        viewModel.getStories().observe(this){
            when(it){
                is Result.Error -> {
                    TextMessage.setText(this, it.error)
                    Log.e("getstoryError", it.error)
                }
                is Result.Loading -> {
                    Log.e("getstoryError","Loading")
                }
                is Result.Success -> {
                    Log.d("getStoryData", it.data.toString())
                    addManyMarker(it.data)
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.map_options, menu)
//        return true
//    }
    private fun setupMenuClickListener() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.normal_type -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                    true
                }

                R.id.satellite_type -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                    true
                }

                R.id.terrain_type -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                    true
                }

                R.id.hybrid_type -> {
                    mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                    true
                }

                else -> {
                    false
                }

            }


        }
    }

    private fun addManyMarker(storyData: List<StoryItem>) {


        storyData.forEach {
            Log.d("markerMaps", it.name)
            Log.d("markerMaps", it.lat.toString())
            Log.d("markerMaps", it.lon.toString())
            val latLng = LatLng(it.lat as Double, it.lon as Double)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(it.name)
            )
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }
}

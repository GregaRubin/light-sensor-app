package com.example.lightsensorapp

import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.lightsensorapp.databinding.ActivityMapBinding
import com.example.lightsensorapp.databinding.ActivitySettingsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale


class MapActivity : AppCompatActivity(), OnMapReadyCallback  {
    private lateinit var binding: ActivityMapBinding
    private  lateinit var googleMap: GoogleMap
    private lateinit var geocoder:Geocoder
    lateinit var sharedPref: SharedPreferences
    private lateinit var marker: Marker
    var curLat: Float = 0f
    var curLong: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        geocoder = Geocoder(this, Locale.getDefault())
        sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)
        var mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.buttonSelectLocation.setOnClickListener { selectClick() }
    }

    private fun selectClick() {
        sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putFloat("long", curLong)
            putFloat("lat", curLat)
            apply()
        }
        finish()
    }


    override fun onMapReady(p0: GoogleMap) {
        sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)
        var long = sharedPref.getFloat("long", 15.638392f)
        var lat = sharedPref.getFloat("lat", 46.558777f)
        curLat = lat
        curLong = long
        googleMap = p0
        var pos: LatLng = LatLng(lat.toDouble(),long.toDouble())
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 9f))

        marker = googleMap.addMarker(
            MarkerOptions().position(pos).title("Lokacija")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        )!!

        googleMap.setOnMapClickListener(OnMapClickListener { point -> //save current location

            var latLng = point
            Log.d("kappa", "You clicked point ${point.toString()}")

            marker.remove()

            marker = googleMap.addMarker(
                MarkerOptions().position(point).title("Lokacija")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )!!

            curLat = latLng.latitude.toFloat()
            curLong = latLng.longitude.toFloat()
        })
    }
}
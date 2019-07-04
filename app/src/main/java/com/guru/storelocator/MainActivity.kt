package com.guru.storelocator

import com.guru.storelocator.R
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var text:TextView
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.title)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    // ...
                    text.text = "lat "+ location?.latitude.toString()+" longitude "+ location?.longitude.toString()

                }
            }
        }
        checkPermission()

        text.setOnClickListener {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    location : Location? ->
                if(location?.latitude  ==null)
                {
                    fusedLocationProviderClient.requestLocationUpdates(
                        LocationRequest().setInterval(50000),
                        locationCallback,
                        null /* Looper */)
                }
                else
                text.text = "lat "+ location?.latitude.toString()+" longitude "+ location?.longitude.toString()
            }
        }
    }


    fun checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),500)

        }else{
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                location : Location? ->
                 text.text = "lat "+ location?.latitude.toString()+" longitude "+ location?.longitude.toString()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 500)
        {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    location : Location? ->
                text.text = "lat "+ location?.latitude.toString()+" longitude "+ location?.longitude.toString()
            }
        }
    }
}

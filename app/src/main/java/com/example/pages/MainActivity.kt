package com.example.pages

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getLocationPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    showLocation()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    showLocation()
                }
                else -> {
                    Toast.makeText(this, "goodbye", Toast.LENGTH_SHORT).show()
                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun showLocation() {
        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if(!isLocationEnabled()){
            Toast.makeText(this, "turn on your location", Toast.LENGTH_SHORT).show()
            Log.d("location", "turn on your location")
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                location?.let{
                    Toast.makeText(this, "latitude" + it.latitude + " , long=" + it.longitude, Toast.LENGTH_LONG).show()
                    Log.d("location lastLocation" , "latitude" + it.latitude + " , long=" + it.longitude)
                }
        }
        fusedLocationClient.getCurrentLocation(	PRIORITY_HIGH_ACCURACY , null).addOnSuccessListener{
            location : Location? ->
            location?.let{
                Toast.makeText(this, "latitude" + it.latitude + " , long=" + it.longitude, Toast.LENGTH_LONG).show()
                Log.d("location currentLocation" , "latitude" + it.latitude + " , long=" + it.longitude)
            }
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }
}
/*
*
* */
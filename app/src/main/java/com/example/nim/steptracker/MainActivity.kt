package com.example.nim.steptracker

import android.content.Context
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val minimum_time_between_updates = 1L
        val minimum_distance_between_updates = 0.0f

        var myListener:MyLocationListener = MyLocationListener()

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minimum_time_between_updates, minimum_distance_between_updates, myListener)
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                minimum_time_between_updates,
//                minimum_distance_between_updates,
//                MyLocationListener()
//        );
    }
}

package com.example.nim.steptracker

import android.content.Context
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity()
{

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double
    {
        val latA = Math.toRadians(lat1)
        val lonA = Math.toRadians(lon1)
        val latB = Math.toRadians(lat2)
        val lonB = Math.toRadians(lon2)
        val cosAng = Math.cos(latA) * Math.cos(latB) * Math.cos(lonB - lonA) + Math.sin(latA) * Math.sin(latB)
        val ang = Math.acos(cosAng)
        return ang * 6371
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val minimum_time_between_updates = 0L
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

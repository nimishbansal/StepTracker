package com.example.nim.steptracker

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log

class MyLocationListener: LocationListener
{
    override fun onLocationChanged(location: Location?)
    {
        Log.i("MYTAG", "x:"+ location?.latitude?.toString()+","+"y:"+location?.longitude?.toString())
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?)
    {
        Log.i("MYTAG","status changed to " + status.toString())
    }

    override fun onProviderEnabled(provider: String?)
    {
        Log.i("MYTAG","provider enabled to " + provider.toString())
    }

    override fun onProviderDisabled(provider: String?)
    {
        Log.i("MYTAG","provider disabled to " + provider.toString())
    }

}

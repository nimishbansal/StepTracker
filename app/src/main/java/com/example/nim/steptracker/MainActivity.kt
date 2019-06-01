package com.example.nim.steptracker

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{

    private var currentTime:Long = 0


    public var running: Boolean=false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val minimumTimeBetweenUpdates = 2L
        val minimumDistanceBetweenUpdates = 0.5f

        val myListener = MyLocationListener(this)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minimumTimeBetweenUpdates, minimumDistanceBetweenUpdates, myListener)
        start.setOnClickListener {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            currentTime = System.currentTimeMillis()
            myListener.initialLocation = location
            myListener.distanceTravelled = 0.toDouble()
            start.isEnabled=false
            stop.isEnabled=true
            running=true

            val showTimerThread = object:Thread(){
                override fun run()
                {
                    while (true)
                    {
                        if (running)
                        {
                            runOnUiThread {
                                timeTaken.text = ((System.currentTimeMillis()-currentTime)/1000).toString() + "s"
                            }

                        }
                        Thread.sleep(1000)
                    }

                }
            }
            showTimerThread.start()
        }

        stop.setOnClickListener {
            start.isEnabled=true
            stop.isEnabled=false
            Log.i("MYTAG", "total distance travelled= " + myListener.distanceTravelled.toString()+ ", in  "+ (System.currentTimeMillis()-currentTime).toString())
            running=false
        }


        restart.setOnClickListener {
            stop.isEnabled=false
            start.isEnabled=true
            distanceTravelled.text = "0 m"
            timeTaken.text="0 s"
            stepsTaken.text="0"
            running=false
        }
    }
}

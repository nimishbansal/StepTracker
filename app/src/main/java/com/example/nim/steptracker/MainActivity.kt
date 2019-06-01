package com.example.nim.steptracker

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener



class MainActivity : AppCompatActivity()
{

    private var currentTime:Long = 0
    public var running: Boolean=false
    public var sseekBarSize=50

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val minimumTimeBetweenUpdates = 2L
        val minimumDistanceBetweenUpdates = 0.5f
        val myListener = MyLocationListener(this)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minimumTimeBetweenUpdates, minimumDistanceBetweenUpdates, myListener)

        scalingFactor.max = 100
        scalingFactor.progress = 50

        scalingFactor.setOnSeekBarChangeListener(object : OnSeekBarChangeListener
        {

            override fun onStopTrackingTouch(seekBar: SeekBar)
            {}

            override fun onStartTrackingTouch(seekBar: SeekBar)
            { }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
            {
                // TODO Auto-generated method stub
                distancePerStep.text = (progress.toFloat()/sseekBarSize.toFloat()).toString() + "mts/step"
            }
        })

        start.setOnClickListener {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            currentTime = System.currentTimeMillis()
            myListener.initialLocation = location
            myListener.distanceTravelled = 0.toDouble()
            start.isEnabled=false
            stop.isEnabled=true
            scalingFactor.isEnabled=false
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
            scalingFactor.isEnabled=true
        }

        restart.setOnClickListener {
            stop.isEnabled=false
            start.isEnabled=true
            distanceTravelled.text = "0 m"
            timeTaken.text="0 s"
            stepsTaken.text="0"
            running=false
            scalingFactor.isEnabled=true
        }
    }
}

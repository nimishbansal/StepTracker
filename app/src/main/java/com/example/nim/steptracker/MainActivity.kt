package com.example.nim.steptracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import java.lang.Math.abs


class MainActivity : AppCompatActivity()
{

    private var currentTime:Long = 0
    public var running: Boolean=false
    public var sseekBarSize=50
    public var currentSquareSum=0.0

    public var difference: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val minimumTimeBetweenUpdates = 1L
        val minimumDistanceBetweenUpdates = 0.2f
        val myListener = MyLocationListener(this)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minimumTimeBetweenUpdates, minimumDistanceBetweenUpdates, myListener)

        scalingFactor.max = 100
        scalingFactor.progress = 50


        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val gyroSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gyroscopeSensorListener = object : SensorEventListener
        {
            override fun onSensorChanged(sensorEvent: SensorEvent)
            {
                var a= 1.0f
                var b= 1.0f
                var c= 1.0f
//                Log.i("MYTAG", sensorEvent.values[0].toString()+","+sensorEvent.values[1].toString()+","+sensorEvent.values[2].toString())
                a = sensorEvent.values[0]*sensorEvent.values[0]
                b = sensorEvent.values[0]*sensorEvent.values[0]
                c = sensorEvent.values[0]*sensorEvent.values[0]
                val current = (a + b + c).toDouble()
                difference = abs(current - currentSquareSum)
                currentSquareSum = current
                Log.i("MYTAG",(difference).toString())




            }

            override fun onAccuracyChanged(sensor: Sensor, i: Int)
            {
                Log.i("MYTAG", sensor.vendor.toString())
            }
        }

        sensorManager.registerListener(gyroscopeSensorListener, gyroSensor, 0)
//        sensorManager.registerListener(gyroscopeSensorListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL)


        setListners(myListener, locationManager)


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
    }

    private fun setListners(myListener: MyLocationListener, locationManager: LocationManager)
    {
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

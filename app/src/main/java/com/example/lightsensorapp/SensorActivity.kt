package com.example.lightsensorapp

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.health.connect.datatypes.ExerciseRoute.Location
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.lightsensorapp.databinding.ActivitySensorBinding
import com.example.lightsensorapp.databinding.ActivitySettingsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale
import java.util.Random
import java.util.logging.Handler

class SensorActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var handler: android.os.Handler
    private lateinit var mqttHelper: MqttHelper
    lateinit var sharedPref: SharedPreferences
    private var brightness: Sensor?= null
    private var simMode: Boolean = false
    private var long: Float = 0f
    private var light: Float = 0f
    private var realLong: Float = 0f
    private var realLat: Float = 0f
    private var lat: Float = 0f
    private var freq: Float = 5f
    private var  lower: Float = 10f
    private var upper: Float = 100f
    private var running: Boolean = false
    private lateinit var binding: ActivitySensorBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        handler =  android.os.Handler()
        super.onCreate(savedInstanceState)
        binding = ActivitySensorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mqttHelper = MqttHelper(applicationContext)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initSensorData()
        if(!simMode) {getLocation()}
        startSensor()

        binding.buttonStart.setOnClickListener(){
            if(!running){
                binding.buttonStart.text = "Stop"
                binding.buttonStart.setTextColor(Color.RED)
                running = true
                handler.postDelayed(object : Runnable {
                    override fun run() {

                        Log.d("kappa", "sending sensor data ${1000  * freq.toLong()}")

                        sendData()

                        handler.postDelayed(this, 5000 * freq.toLong() )
                    }
                }, 0)
            } else{
                Log.d("kappa", "stoped")
                running = false
                binding.buttonStart.setTextColor(Color.WHITE)
                handler.removeCallbacksAndMessages(null)
                binding.buttonStart.text = "Start"
            }
        }
    }

    private fun sendData(){
        var sensorData: Float = 0f
        var lo = 0f
        var la = 0f
        if(simMode){
            sensorData = kotlin.random.Random.nextLong(lower.toLong(), upper.toLong()).toFloat()
            lo = long
            la = lat
        } else{
            sensorData = light
            lo = realLong
            la = realLat
        }
        var time = System.currentTimeMillis()
        var msg: String = """{"longitude": $lo, "latitude": $la, "value": $sensorData, "time": $time}"""
        mqttHelper.publish("sensor/light", msg)
        Log.d("kappa", msg)
    }

    fun getLocation()  {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it!=null){

                Log.d("kappa", "location: ${it.latitude}")
                realLong = it.longitude.toFloat()
                realLat = it.latitude.toFloat()

                binding.textLong.text = ("$realLong")
                binding.textLat.text = ("$realLat")
            }
        }
    }

    private fun initSensorData(){
        sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)

        freq = sharedPref.getFloat("frequency", 30.0f)
        simMode = sharedPref.getBoolean("simMode", false)

        if(simMode){
            lower = sharedPref.getFloat("lower", 100.0f)
            upper = sharedPref.getFloat("upper", 500.0f)
            long = sharedPref.getFloat("long", 15.638392f)
            lat = sharedPref.getFloat("lat", 46.558777f)
            Log.d("kappa", "inited fake location : $long $lat")
            binding.textLong.text = ("$long")
            binding.textLat.text = ("$lat")
            binding.textType.text = ("Sim mode ($lower - $upper)")

        } else{
            binding.textType.text = ("Real data");

        }
        binding.textFrequency.text = ("$freq min")

    }

    private fun startSensor() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        var maxValue = brightness?.maximumRange
        binding.circularProgressBar.progressMax = maxValue!! / 5
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0?.sensor?.type == Sensor.TYPE_LIGHT){
            light = p0.values[0]

            binding.textLightValue.text = "Sensor: $light"
            binding.circularProgressBar.setProgressWithAnimation(light)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        if(handler!=null) handler.removeCallbacksAndMessages(null)
        sensorManager.unregisterListener(this)
    }

}
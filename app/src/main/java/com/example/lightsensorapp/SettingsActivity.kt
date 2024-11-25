package com.example.lightsensorapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lightsensorapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    lateinit var sharedPref: SharedPreferences
    var long: Float = 0.0f
    var  lat: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)

        var frequency: Float = sharedPref.getFloat("frequency", 30.0f)
        var simMode: Boolean = sharedPref.getBoolean("simMode", false)
        var lower: Float = sharedPref.getFloat("lower", 100.0f)
        var upper: Float = sharedPref.getFloat("upper", 500.0f)
        long = sharedPref.getFloat("long", 15.638392f)
        lat = sharedPref.getFloat("lat", 46.558777f)

        binding.frequencySlider.value = frequency
        binding.switchSimMode.isChecked = simMode
        binding.rangeSlider.values = mutableListOf(lower, upper)
        binding.textLocation.setText("Long: $long   Lat: $lat")

        binding.buttonSelectLocation.setOnClickListener { openMap() }

        binding.buttonSaveSettings.setOnClickListener {
            with (sharedPref.edit()) {

                frequency = binding.frequencySlider.value
                simMode = binding.switchSimMode.isChecked
                lower = binding.rangeSlider.values[0]
                upper = binding.rangeSlider.values[1]

                Log.d("kappa", "Saving freq")
                putFloat("frequency", frequency)
                putBoolean("simMode", simMode)
                putFloat("lower", lower)
                putFloat("upper", upper)
                putFloat("long", long)
                putFloat("lat", lat)
                apply()
            }
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        long = sharedPref.getFloat("long", 15.638392f)
        lat = sharedPref.getFloat("lat", 46.558777f)
        binding.textLocation.setText("Long: $long   Lat: $lat")
    }

    fun openMap(){
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }
}
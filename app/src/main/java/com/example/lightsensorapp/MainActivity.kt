package com.example.lightsensorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lightsensorapp.databinding.ActivityMainBinding
const val MY_SP_FILE_NAME = "myshared.data"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonSettings.setOnClickListener{ openSettings()}
        binding.buttonSensor.setOnClickListener { openSensor() }
        binding.buttonMessage.setOnClickListener { openMessage() }
    }

    private fun openMessage(){
        val intent = Intent(this, MessageActivity::class.java)
        startActivity(intent)
    }

    private fun openSensor() {
        val intent = Intent(this, SensorActivity::class.java)
        startActivity(intent)
    }

    fun openSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
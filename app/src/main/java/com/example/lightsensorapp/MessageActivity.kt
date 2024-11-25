package com.example.lightsensorapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import com.example.lightsensorapp.databinding.ActivityMessageBinding
import com.example.lightsensorapp.databinding.ActivitySettingsBinding

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var mqttHelper: MqttHelper
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)

        var long = sharedPref.getFloat("long", 15.638392f)
        var lat = sharedPref.getFloat("lat", 46.558777f)

        binding.messageLocation.text = "Long: " + long.toString() + ", Lat: " +lat.toString()

        mqttHelper = MqttHelper(applicationContext)
        binding.buttonAdd.setOnClickListener { sendMsg() }
    }

    private fun sendMsg(){
        binding.buttonAdd.setOnClickListener {
            val desc = binding.inputDescription.text.toString()
            val topic = binding.groupTopic.checkedRadioButtonId;


            if (title.isNotEmpty() && desc.isNotEmpty() && topic > -1) {
                val topicBtn = findViewById<RadioButton>(topic);
                Log.d("kappa", "Sending msg: $desc, ${topicBtn.text.toString()}")
                var time = System.currentTimeMillis()
                sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)

                var long = sharedPref.getFloat("long", 15.638392f)
                var lat = sharedPref.getFloat("lat", 46.558777f)

                var msg: String = """{"category": "${topicBtn.text.toString().toLowerCase()}", "longitude": $long, "latitude": $lat, "message": "$desc", "time": $time}"""

                mqttHelper.publish("event/${topicBtn.text.toString().toLowerCase()}", msg)
                finish()
            }
        }
    }
}
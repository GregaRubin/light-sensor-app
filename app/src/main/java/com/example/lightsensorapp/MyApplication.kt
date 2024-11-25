package com.example.lightsensorapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.io.File
import java.io.IOException
import java.util.UUID

class MyApplication: Application() {
    lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        initShared()
        if (!containsID()) {
            saveID(UUID.randomUUID().toString().replace("-", ""))

            var frequency: Float = 30.0f
            var simMode: Boolean = false
            var lower: Float = 100.0f
            var upper: Float = 500.0f
            var long: Float = 15.638392f
            var lat: Float = 46.558777f

            with (sharedPref.edit()) {
                putFloat("frequency", frequency)
                putBoolean("simMode", simMode)
                putFloat("lower", lower)
                putFloat("upper", upper)
                putFloat("long", long)
                putFloat("lat", lat)
                apply()
            }

        }
        Log.d(
            "kappa",
            "Inited data"
        )
    }

    fun initShared() {
        sharedPref = getSharedPreferences( MY_SP_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun saveID(id:String) {
        with (sharedPref.edit()) {
            putString("ID", id)
            apply()
        }
    }

    fun containsID():Boolean {
        return sharedPref.contains("ID")
    }

    fun getID(): String? {
        return sharedPref.getString("ID","DefaultNoData")
    }
}
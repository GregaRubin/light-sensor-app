package com.example.lightsensorapp

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttHelper (context: Context) {

    public var mqttClient: MqttAndroidClient

    companion object {
        val TAG = "AndroidMqttClient"
        const val SERVER_URI = "ssl://y1505205.ala.eu-central-1.emqxsl.com:8883"
        //val SERVER_URI = "tcp://broker.emqx.io:1883"
        const val SENSOR_TOPIC = "sensor/light"
        const val MESSAGE_TOPIC = "event/"
        val clientId: String = MqttClient.generateClientId()
    }

    init {
        val options = MqttConnectOptions()
        options.userName = "grega"
        options.password = "password".toCharArray()
        mqttClient = MqttAndroidClient(context, SERVER_URI, clientId)
        Log.d("kappa", "Trying to connect to $SERVER_URI")
        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("kappa", "Connection success")

                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }


    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            if(mqttClient.isConnected) {
                mqttClient.publish(topic, message, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d("kappa", "$msg published to $topic")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d("kappa", "Failed to publish $msg to $topic")
                    }
                })
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}
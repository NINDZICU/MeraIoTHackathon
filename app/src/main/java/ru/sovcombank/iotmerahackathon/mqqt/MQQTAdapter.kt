package ru.sovcombank.iotmerahackathon.mqqt

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import ru.sovcombank.iotmerahackathon.MainActivity
import ru.sovcombank.iotmerahackathon.entity.Location
import ru.sovcombank.iotmerahackathon.entity.MyBeacon
import ru.sovcombank.iotmerahackathon.network.model.Coordinate

class MQQTAdapter {

    companion object {
        private const val mqttHost = "broker.hivemq.com"
        private const val mqttPort = 1883
        private const val LOCATION_TOPIC = "gc-coordinates"
        private const val BEACON_DATA_TOPIC = "gc-beacon-data"
    }

    private lateinit var client: MqttAndroidClient
    private lateinit var context: Context


    fun initialize(context: Context) {
        this.context = context
        val clientId = MqttClient.generateClientId()
        val brokerUrl = "tcp://$mqttHost:$mqttPort"
        client = MqttAndroidClient(context, brokerUrl, clientId)

        client.connect()?.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                // We are connected
                Log.d("Success Connect", "success")
                subscribe()
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                // Something went wrong e.g. connection timeout or firewall problems
                Log.d("Mqtt connect failure %s", exception.message)

            }
        }
    }

    fun subscribe() {
        val qos = 1
        client.subscribe(
            LOCATION_TOPIC, qos
        ) { topic, message ->
            val location = Gson().fromJson(message.toString(), Location::class.java)
            if(context is MainActivity) (context as MainActivity).showCoordinates(location.x, location.y)
            Log.d("MESSAGE", message.toString()) }
    }

    fun publish(list: MutableMap<Int, MyBeacon>) {
        if (client.isConnected) {
            val gson = Gson().toJson(list)
            client.publish("gc-beacon-data", MqttMessage(gson.toByteArray()))

        }
    }

    fun isConnected(): Boolean {
        return client.isConnected
    }
}
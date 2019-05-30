package com.example.witcher4.sensors_data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager

class ProximityData(context: Context) : SensorData(context) {

    private val proximitySensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    var distance = Int.MAX_VALUE
    var significantChangeOccurred = false
    var lastChangeTime:Long = 0

    companion object {
        const val MIN_TIME_BETWEEN_CHANGES = 150
        const val proximityThresholdCM = 1
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_PROXIMITY) {
            distance = event.values[0].toInt()
            if(distance < proximityThresholdCM)
                if(System.currentTimeMillis() - lastChangeTime >= MIN_TIME_BETWEEN_CHANGES) {
                    significantChangeOccurred = true
                    lastChangeTime = System.currentTimeMillis()
            }
        }
    }

    fun registerListener(){
        manager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_GAME)
    }
}
package com.example.witcher4.sensors_data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

abstract class SensorData(context: Context) : SensorEventListener {
    val manager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun pause(){
        manager.unregisterListener(this)
    }
}
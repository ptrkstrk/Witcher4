package com.example.witcher4.sensors_data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager

class OrientationData(context: Context) : SensorData(context) {

    private val accelerometer:Sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magnetometer:Sensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var accelOutput: FloatArray? = null
    private var magOutput: FloatArray? = null

    val orientation = FloatArray(3)
    var startOrientation: FloatArray? = null

    fun setUp(){
        startOrientation = null
    }

    fun registerListeners(){
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelOutput = event.values
        }
        else if(event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magOutput = event.values
        }
        if(accelOutput != null && magOutput != null){
            val rotationMatrix = FloatArray(9)
            val inclinationMatrix = FloatArray(9)
            val receivedRotationMatrix = SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, accelOutput, magOutput)
            if(receivedRotationMatrix) {
                SensorManager.getOrientation(rotationMatrix, orientation)
                if (startOrientation == null) {
                    startOrientation = FloatArray(orientation.size)
                    System.arraycopy(orientation, 0, startOrientation, 0, orientation.size)
                }
            }
        }
    }

}
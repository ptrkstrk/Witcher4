package com.example.witcher4.scenes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent

abstract class Scene {

    val paint = Paint()

    init {
        paint.textAlign = Paint.Align.CENTER
    }

    abstract val backgroundBmp: Bitmap
    abstract fun update()
    abstract fun draw(canvas: Canvas)
    abstract fun receiveTouch(event: MotionEvent?)
    open fun prepareGame(){}
}
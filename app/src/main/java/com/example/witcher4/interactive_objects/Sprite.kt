package com.example.witcher4.interactive_objects

import android.graphics.*

abstract class Sprite(val width: Int, startX:Int, startY:Int, val height:Int) : GameObject {

    val hitbox = Rect(startX, startY, startX + width, startY + height)
    abstract val imageToHitboxRatioWidth: Float
    abstract val imageToHitboxRatioHeight: Float
    abstract var image: Bitmap

    override fun draw(canvas: Canvas) {
        val left = ((hitbox.right - imageToHitboxRatioWidth * width) + hitbox.left) / 2
        val top = ((hitbox.bottom - imageToHitboxRatioHeight * height) + hitbox. top) / 2
        canvas.drawBitmap(image, left, top, null)
    }
}
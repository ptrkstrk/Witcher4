package com.example.witcher4.interactive_objects

import android.graphics.Bitmap
import android.graphics.Canvas

class Bonus(width: Int, startX:Int, startY:Int, height:Int, bmp: Bitmap) :
            InteractiveElement(width, startX, startY, height) {

    override val imageToHitboxRatioWidth = 1F
    override val imageToHitboxRatioHeight = 1F
    override var image:Bitmap = Bitmap.createScaledBitmap(bmp, (imageToHitboxRatioWidth * hitbox.width()).toInt(),
        (imageToHitboxRatioHeight * hitbox.height()).toInt(), false)

    override fun draw(canvas: Canvas) {
        if (visible) {
            canvas.drawBitmap(image, hitbox.left.toFloat(), hitbox.top.toFloat(), null)
        }
    }
}
package com.example.witcher4.interactive_objects

import android.graphics.Bitmap

class Obstacle(width: Int, startX:Int, startY:Int, height:Int, bmp: Bitmap) :
    InteractiveElement(width, startX, startY, height) {

    override val imageToHitboxRatioWidth = 1.28F
    override val imageToHitboxRatioHeight = 1.1F

    override var image = Bitmap.createScaledBitmap(bmp, (imageToHitboxRatioWidth * hitbox.width()).toInt(),
        (imageToHitboxRatioHeight * hitbox.height()).toInt(), false)
}
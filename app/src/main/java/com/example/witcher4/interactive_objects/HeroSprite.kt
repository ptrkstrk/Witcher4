package com.example.witcher4.interactive_objects

import android.graphics.*

class HeroSprite(startX:Int, startY:Int, bmp:Bitmap)
    : Sprite(HERO_WIDTH, startX, startY, HERO_HEIGHT) {

    override val imageToHitboxRatioWidth = 1.45F
    override val imageToHitboxRatioHeight = 1.1F

    companion object {
        const val HERO_WIDTH = 110
        const val HERO_HEIGHT = 300
    }

    override var image = Bitmap.createScaledBitmap(bmp, (imageToHitboxRatioWidth * hitbox.width()).toInt(),
                    (imageToHitboxRatioHeight* hitbox.height()).toInt(), false)!!

    fun update(point: Point){
        hitbox.set(point.x - hitbox.width()/2, point.y - hitbox.height()/2,
            point.x + hitbox.width()/2, point.y + hitbox.height()/2)
    }
}
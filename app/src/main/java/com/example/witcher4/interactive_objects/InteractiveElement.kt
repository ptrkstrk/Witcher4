package com.example.witcher4.interactive_objects

abstract class InteractiveElement(width: Int, startX:Int, startY:Int, height:Int) :
    Sprite(width, startX, startY, height) {

    var visible = true
    var interactionOccurred = false

    fun playerInteracted(hero: HeroSprite) : Boolean {
        val interaction = hitbox.intersect(hero.hitbox)
        if(interaction)
            interactionOccurred = true
        return interaction
    }

    fun decreaseX(x: Int){
        hitbox.left -= x
        hitbox.right -= x
    }
}
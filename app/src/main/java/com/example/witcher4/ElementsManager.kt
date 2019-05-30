package com.example.witcher4

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.example.witcher4.GameView.Companion.INIT_TIME
import com.example.witcher4.GameView.Companion.TOTAL_TIME_PAUSED
import com.example.witcher4.MainActivity.Companion.SCREEN_HEIGHT
import com.example.witcher4.MainActivity.Companion.SCREEN_WIDTH
import com.example.witcher4.interactive_objects.*
import java.util.*
import kotlin.random.Random.Default.nextBoolean

class ElementsManager(context: Context){

    companion object {
        const val BONUS_INTERACTION_CODE = 1
        const val OBSTACLE_INTERACTION_CODE = 2
        const val NO_INTERACTION_CODE = 0
        const val ELEMENTS_GAP = 350
        const val OBSTACLE_WIDTH = 130
        const val OBSTACLE_HEIGHT = 310
        const val BONUS_WIDTH = 120
        const val BONUS_HEIGHT = 130
        const val CROSS_SCREEN_INITIAL_TIME = 10000F
        const val SPEED_CALCULATION_DIVIDER = 4000.0
    }

    private val obstacleImg = BitmapFactory.decodeResource(context.resources, R.drawable.creeper)
    private val bonusImg = BitmapFactory.decodeResource(context.resources, R.drawable.diamond)
    private val elements: ArrayList<InteractiveElement> = ArrayList()
    private var lastUpdateTime: Long = System.currentTimeMillis()
    private val initialSpeed:Float = SCREEN_WIDTH / CROSS_SCREEN_INITIAL_TIME
    var isPaused = false

    init {
        populateElements()
    }

    fun heroInteracted(hero: HeroSprite) : Int{
        for(el: InteractiveElement in elements)
            if(!el.interactionOccurred && el.playerInteracted(hero)){
                return when (el) {
                    is Obstacle -> OBSTACLE_INTERACTION_CODE
                    is Bonus -> {
                        el.visible = false
                        BONUS_INTERACTION_CODE
                    }
                    else -> NO_INTERACTION_CODE
                }
            }
        return NO_INTERACTION_CODE
    }

    private fun populateElements(){
        var currX = 2 * SCREEN_WIDTH
        var currEl: InteractiveElement
        while(currX > SCREEN_WIDTH){
            currEl = createNextElement(currX)
            elements.add(currEl)
            currX -= currEl.width + ELEMENTS_GAP
        }
    }

    private fun createNextElement(x: Int): InteractiveElement {
        val isObstacle = nextBoolean()
        val yStart: Int
        return if (isObstacle) {
            yStart = (Math.random() * (SCREEN_HEIGHT - OBSTACLE_HEIGHT)).toInt()
            Obstacle(OBSTACLE_WIDTH, x, yStart, OBSTACLE_HEIGHT, obstacleImg)
        }
        else {
            yStart = (Math.random() * (SCREEN_HEIGHT - BONUS_HEIGHT)).toInt()
            Bonus(BONUS_WIDTH, x, yStart, BONUS_HEIGHT, bonusImg)
        }
    }

    fun update(){
        val elapsedTime: Int = (System.currentTimeMillis() - lastUpdateTime).toInt()
        lastUpdateTime = System.currentTimeMillis()
        val timePassed = lastUpdateTime - INIT_TIME - TOTAL_TIME_PAUSED
        val speed = (Math.sqrt(1 + timePassed / SPEED_CALCULATION_DIVIDER)).toFloat() * initialSpeed

        for (el: InteractiveElement in elements)
            el.decreaseX((speed * elapsedTime).toInt())

        if (elements[elements.size - 1].hitbox.right <= 0) {
            val nextEl = createNextElement(elements[0].hitbox.right + elements[0].width + ELEMENTS_GAP)
            elements.add(0, nextEl)
            elements.removeAt(elements.size - 1)
        }
    }

    fun draw(canvas: Canvas){
        for(el: InteractiveElement in elements)
            el.draw(canvas)
    }

    fun resume() {
        isPaused = false
        lastUpdateTime = System.currentTimeMillis()
    }

    fun swapElements() {
        var currRect: Rect
        for (i in elements.indices) {
            if(elements[i].visible){
                if (elements[i] is Obstacle) {
                    currRect = elements[i].hitbox
                    elements[i] = Bonus(
                        BONUS_WIDTH, currRect.left, currRect.top,
                        BONUS_HEIGHT, bonusImg)
                }
                else if(elements[i] is Bonus){
                    currRect = elements[i].hitbox
                    elements[i] = Obstacle(OBSTACLE_WIDTH, currRect.left, currRect.top,
                        OBSTACLE_HEIGHT, obstacleImg)
                }
            }
        }
    }
}
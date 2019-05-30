package com.example.witcher4.scenes

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.view.MotionEvent

class SceneManager(context: Context, resources: Resources) {
    private val scenes:HashMap<Int, Scene> = HashMap()

    companion object {
        const val GAMEPLAY_SCENE_KEY = 1
        const val MENU_SCENE_KEY = 2
        var ACTIVE_SCENE:Int = 0
    }

    init{
        ACTIVE_SCENE =
            MENU_SCENE_KEY
        scenes[GAMEPLAY_SCENE_KEY] =
            GameplayScene(context, resources)
        scenes[MENU_SCENE_KEY] =
            MenuScene(context, resources)
    }

    fun receiveTouch(event: MotionEvent?){
        scenes[ACTIVE_SCENE]!!.receiveTouch(event)
        if(ACTIVE_SCENE == MENU_SCENE_KEY) {
            ACTIVE_SCENE =
                GAMEPLAY_SCENE_KEY
            scenes[ACTIVE_SCENE]!!.prepareGame()
        }
    }

    fun update(){
        scenes[ACTIVE_SCENE]!!.update()
    }

    fun draw(canvas: Canvas){
        scenes[ACTIVE_SCENE]!!.draw(canvas)
    }

    fun notifyAboutDestruction() {
        val currScene: GameplayScene
        if(scenes[ACTIVE_SCENE] is GameplayScene) {
            currScene = scenes[ACTIVE_SCENE] as GameplayScene
            if(!currScene.gameFinished && !currScene.gamePaused)
                currScene.pauseGame()
        }
    }
}
package com.example.witcher4

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.witcher4.scenes.SceneManager

class GameView(context: Context, resources: Resources) : SurfaceView(context), SurfaceHolder.Callback{

    private lateinit var thread: MainThread
    private val sceneManager = SceneManager(context, resources)

    companion object {
        const val SCREEN_BOTTOM_DEAD_ZONE = 100
        const val PLAYER_MOVEMENT_DEAD_ZONE = 3
        const val STATE_CHANGED_INFO_TEXT_SIZE = 200F
        const val MAIN_MENU_TEXT_SIZE = 65F
        const val SCORE_TEXT_SIZE = 125F
        const val FIRST_TEXT_POS = 310F
        const val TEXT_OFFSET = 125F
        const val MILLIS_IN_SEC = 1000F
        const val X_SPEED_INCREASE_FACTOR = 2.5
        const val Y_SPEED_INCREASE_FACTOR = 2
        var TOTAL_TIME_PAUSED:Long = 0
        var INIT_TIME:Long = 0
    }

    init{
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread = MainThread(this.holder, this)
        thread.isRunning = true
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        sceneManager.notifyAboutDestruction()
        var retry = true
        while(retry){
            try{
                thread.isRunning = false
                thread.join()
            }
            catch(e: InterruptedException){
                e.printStackTrace()
            }
            retry = false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        sceneManager.receiveTouch(event)
        return true
    }

    fun update() {
        sceneManager.update()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        sceneManager.draw(canvas)
    }
}

package com.example.witcher4

import android.graphics.Canvas
import android.view.SurfaceHolder
import java.lang.Exception

class MainThread(private val surfaceHolder: SurfaceHolder, private val gameView:GameView) : Thread() {
    var isRunning: Boolean = false

    companion object {
        const val TARGET_FPS = 60
        var canvas:Canvas? = null
    }
    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime: Long = (1000 / TARGET_FPS).toLong()
        while (isRunning) {
            startTime = System.nanoTime()
            canvas = null
            try {
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    gameView.update()
                    gameView.draw(canvas!!)
                }
            } catch (e: Exception) { }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime)/ 1000000
            waitTime = targetTime - timeMillis
            try{
                if(waitTime > 0)
                    sleep(waitTime)
            }
            catch(e:Exception){ }
        }
    }

}

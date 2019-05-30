package com.example.witcher4

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager

class MainActivity : Activity() {

    companion object {
        var SCREEN_WIDTH: Int = 0
        var SCREEN_HEIGHT: Int = 0
        var HIGH_SCORE = 0
        const val HIGH_SCORE_SP_KEY: String = "High score"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        SCREEN_WIDTH = dm.widthPixels
        SCREEN_HEIGHT = dm.heightPixels
        setContentView(GameView(this, resources))
        retrieveHighScore()
    }



    override fun onPause(){
        super.onPause()
        saveHighScore()
  }

    private fun saveHighScore() {
        val scoreSP = getSharedPreferences(HIGH_SCORE_SP_KEY, Context.MODE_PRIVATE)
        val prefsEditor = scoreSP.edit()
        prefsEditor.putInt(HIGH_SCORE_SP_KEY, HIGH_SCORE)
        prefsEditor.apply()
    }

    private fun retrieveHighScore(){
        val scoreSP = getSharedPreferences(HIGH_SCORE_SP_KEY, Context.MODE_PRIVATE)
        HIGH_SCORE = scoreSP.getInt(HIGH_SCORE_SP_KEY, 0)
    }
}

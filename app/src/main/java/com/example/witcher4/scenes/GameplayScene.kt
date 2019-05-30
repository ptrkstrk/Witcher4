package com.example.witcher4.scenes

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.MotionEvent
import com.example.witcher4.ElementsManager
import com.example.witcher4.GameView.Companion.INIT_TIME
import com.example.witcher4.GameView.Companion.MILLIS_IN_SEC
import com.example.witcher4.GameView.Companion.PLAYER_MOVEMENT_DEAD_ZONE
import com.example.witcher4.GameView.Companion.SCORE_TEXT_SIZE
import com.example.witcher4.GameView.Companion.SCREEN_BOTTOM_DEAD_ZONE
import com.example.witcher4.GameView.Companion.STATE_CHANGED_INFO_TEXT_SIZE
import com.example.witcher4.GameView.Companion.TEXT_OFFSET
import com.example.witcher4.GameView.Companion.TOTAL_TIME_PAUSED
import com.example.witcher4.GameView.Companion.X_SPEED_INCREASE_FACTOR
import com.example.witcher4.GameView.Companion.Y_SPEED_INCREASE_FACTOR
import com.example.witcher4.MainActivity
import com.example.witcher4.MainActivity.Companion.HIGH_SCORE
import com.example.witcher4.MainActivity.Companion.SCREEN_HEIGHT
import com.example.witcher4.MainActivity.Companion.SCREEN_WIDTH
import com.example.witcher4.R
import com.example.witcher4.interactive_objects.HeroSprite
import com.example.witcher4.sensors_data.OrientationData
import com.example.witcher4.sensors_data.ProximityData
import kotlin.math.roundToInt

class GameplayScene(private val context: Context, resources: Resources): Scene() {

    private var heroSprite = HeroSprite(0, 0, BitmapFactory.decodeResource(resources,
        R.drawable.hero_img))
    private lateinit var heroPoint: Point
    private lateinit var elementsManager: ElementsManager
    private var lastPauseTime:Long = 0
    private var lastUpdateTime: Long = 0
    override val backgroundBmp = BitmapFactory.decodeResource(resources,
        R.drawable.game_background)!!
    private val scrMidWidth = (SCREEN_WIDTH / 2).toFloat()
    private val scrMidHeight = (SCREEN_HEIGHT / 2).toFloat()
    private val orientationData: OrientationData = OrientationData(context)
    private val proximityData: ProximityData = ProximityData(context)
    var gameFinished = false
    var gamePaused = false
    private var playerScore = 0

    init{
        orientationData.registerListeners()
        proximityData.registerListener()
        moveHeroToStartPosition()
    }

    override fun prepareGame() {
        INIT_TIME = System.currentTimeMillis()
        TOTAL_TIME_PAUSED = 0
        lastUpdateTime = System.currentTimeMillis()
        orientationData.setUp()
        moveHeroToStartPosition()
        setElementsManager()
        playerScore = 0
    }

    override fun receiveTouch(event: MotionEvent?) {
        if (event!!.action == MotionEvent.ACTION_DOWN)
            when {
                gameFinished ->{
                    prepareGame()
                    gameFinished = false
                }
                gamePaused -> resumeGame()
                else -> pauseGame()
            }
    }

    private fun moveHeroToStartPosition() {
        heroPoint = Point((MainActivity.SCREEN_WIDTH * 0.25).roundToInt(),
                            (0.5 * MainActivity.SCREEN_HEIGHT).roundToInt())
        heroSprite.update(heroPoint)
    }

    private fun setElementsManager() {
        elementsManager = ElementsManager(context)
    }

    private fun resumeGame() {
        proximityData.registerListener()
        orientationData.setUp()
        elementsManager.resume()
        gamePaused = false
        lastUpdateTime = System.currentTimeMillis()
        val lastPauseLength = System.currentTimeMillis() - lastPauseTime
        TOTAL_TIME_PAUSED += lastPauseLength
    }

    fun pauseGame() {
        gamePaused = true
        elementsManager.isPaused = true
        lastPauseTime = System.currentTimeMillis()
        proximityData.pause()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(backgroundBmp, null,
            RectF(0F,0F, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat()), null)
        heroSprite.draw(canvas)
        elementsManager.draw(canvas)
        paint.textSize = SCORE_TEXT_SIZE
        paint.color = Color.MAGENTA
        canvas.drawText("" + playerScore, SCREEN_WIDTH - 160F, 150F, paint)
        if (gameFinished)
            drawGameOverScreen(canvas)
        if (gamePaused) {
            drawPauseScreen(canvas)
        }
    }

    private fun drawPauseScreen(canvas: Canvas) {
        paint.textSize = STATE_CHANGED_INFO_TEXT_SIZE
        paint.color = Color.YELLOW
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(context.getString(R.string.game_paused_info), scrMidWidth, scrMidHeight, paint)
    }

    private fun drawGameOverScreen(canvas: Canvas) {
        paint.textSize = STATE_CHANGED_INFO_TEXT_SIZE
        paint.color = Color.RED
        canvas.drawText(context.getString(R.string.game_over_info), scrMidWidth, scrMidHeight, paint)
        paint.textSize = SCORE_TEXT_SIZE
        paint.color = Color.YELLOW
        canvas.drawText(context.getString(R.string.player_score_label) + " " + playerScore,
            scrMidWidth, scrMidHeight + TEXT_OFFSET, paint)
        canvas.drawText( context.getString(R.string.high_score_label)+ " " + HIGH_SCORE,
            scrMidWidth, scrMidHeight + 2 * TEXT_OFFSET, paint)
    }

    override fun update() {
        if (!gameFinished && !gamePaused) {
            val elapsedTime = (System.currentTimeMillis() - lastUpdateTime).toInt()
            lastUpdateTime = System.currentTimeMillis()
            if (orientationData.startOrientation != null)
                movePlayer(elapsedTime)
            if(proximityData.significantChangeOccurred) {
                elementsManager.swapElements()
                proximityData.significantChangeOccurred = false
            }

            ensureHeroIsOnScreen()

            heroSprite.update(heroPoint)
            elementsManager.update()
            val interactionCode = elementsManager.heroInteracted(heroSprite)
            if (interactionCode == ElementsManager.OBSTACLE_INTERACTION_CODE) {
                gameFinished = true
                if(playerScore > HIGH_SCORE)
                    HIGH_SCORE = playerScore
            }
            else if (interactionCode == ElementsManager.BONUS_INTERACTION_CODE)
                playerScore++
        }
    }

    private fun ensureHeroIsOnScreen() {
        if (heroPoint.x < SCREEN_BOTTOM_DEAD_ZONE)
            heroPoint.x = SCREEN_BOTTOM_DEAD_ZONE
        if (heroPoint.x > SCREEN_WIDTH)
            heroPoint.x = SCREEN_WIDTH
        if (heroPoint.y < 0)
            heroPoint.y = 0
        if (heroPoint.y > SCREEN_HEIGHT)
            heroPoint.y = SCREEN_HEIGHT
    }

    private fun movePlayer(elapsedTime: Int) {
        val movementX = orientationData.orientation[1] - orientationData.startOrientation!![1]
        val movementY = orientationData.orientation[2] - orientationData.startOrientation!![2]
        val xSpeed = X_SPEED_INCREASE_FACTOR * movementX * SCREEN_WIDTH / MILLIS_IN_SEC
        val ySpeed = Y_SPEED_INCREASE_FACTOR * movementY * SCREEN_HEIGHT / MILLIS_IN_SEC

        val xNumOfMovingPixels = xSpeed * elapsedTime
        val yNumOfMovingPixels = ySpeed * elapsedTime

        if (Math.abs(xNumOfMovingPixels) > PLAYER_MOVEMENT_DEAD_ZONE) {
            heroPoint.x += xNumOfMovingPixels.toInt()
        }
        if (Math.abs(yNumOfMovingPixels) > PLAYER_MOVEMENT_DEAD_ZONE) {
            heroPoint.y += yNumOfMovingPixels.toInt()
        }
    }
}
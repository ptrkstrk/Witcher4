package com.example.witcher4.scenes

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.MotionEvent
import com.example.witcher4.GameView.Companion.FIRST_TEXT_POS
import com.example.witcher4.GameView.Companion.MAIN_MENU_TEXT_SIZE
import com.example.witcher4.GameView.Companion.TEXT_OFFSET
import com.example.witcher4.MainActivity
import com.example.witcher4.R

class MenuScene(private val context: Context, resources: Resources): Scene() {

    override val backgroundBmp: Bitmap = BitmapFactory.decodeResource(resources,
        R.drawable.menu_background
    )
    override fun receiveTouch(event: MotionEvent?) {}

    override fun draw(canvas: Canvas) {
        val scrMidWidth = (MainActivity.SCREEN_WIDTH / 2).toFloat()
        canvas.drawBitmap(backgroundBmp, null,
            RectF(0F,0F, MainActivity.SCREEN_WIDTH.toFloat(), MainActivity.SCREEN_HEIGHT.toFloat()), null)

        paint.textSize = 1.5F * MAIN_MENU_TEXT_SIZE
        paint.color = Color.RED
        var offsetMultiplier = 1
        canvas.drawText(context.getString(R.string.high_score_label) + "" +
                " " + MainActivity.HIGH_SCORE, scrMidWidth, FIRST_TEXT_POS, paint)

        paint.color = Color.YELLOW
        paint.textSize = MAIN_MENU_TEXT_SIZE
        canvas.drawText(context.getString(R.string.game_rules), scrMidWidth,
            FIRST_TEXT_POS + (++offsetMultiplier) * TEXT_OFFSET, paint )
        canvas.drawText(context.getString(R.string.proximity_info), scrMidWidth,
            FIRST_TEXT_POS + (++offsetMultiplier) * TEXT_OFFSET, paint)
        canvas.drawText(context.getString(R.string.recalibrating_info), scrMidWidth,
            FIRST_TEXT_POS + (++offsetMultiplier) * TEXT_OFFSET, paint)
        canvas.drawText(context.getString(R.string.start_game_info), scrMidWidth,
            FIRST_TEXT_POS + (++offsetMultiplier) * TEXT_OFFSET, paint)
    }

    override fun update() {}
}
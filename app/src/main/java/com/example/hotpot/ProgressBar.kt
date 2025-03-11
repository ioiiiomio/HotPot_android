package com.example.hotpot

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var maxProgress = 2000f

    private val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 40f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

      
        canvas.drawColor(Color.parseColor("#F4EDE6"))


        val radius = viewWidth * 0.4f
        val centerX = viewWidth / 2
        val centerY = viewHeight / 2

        val rectF = RectF(
            centerX - radius, centerY - radius,
            centerX + radius, centerY + radius
        )

        val shader = LinearGradient(
            0f, viewHeight, viewWidth, 0f,
            intArrayOf(Color.GREEN, Color.parseColor("#A5D6A7")),
            null,
            Shader.TileMode.CLAMP
        )
        progressPaint.shader = shader

        canvas.drawArc(rectF, 180f, 180f, false, outlinePaint)

        val sweepAngle = (progress / maxProgress) * 180f
        canvas.drawArc(rectF, 180f, sweepAngle, false, progressPaint)

        canvas.drawText("HotPot", centerX, centerY - radius - 30, titlePaint)

        val progressText = "${progress.toInt()} kcal / ${maxProgress.toInt()} kcal"
        canvas.drawText(progressText, centerX, centerY + 80, textPaint)
    }

    fun setProgress(calories: Float) {
        progress = calories.coerceIn(0f, maxProgress)
        invalidate()
    }
}

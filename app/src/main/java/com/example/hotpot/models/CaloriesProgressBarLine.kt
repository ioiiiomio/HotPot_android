package com.example.hotpot.models

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CaloriesProgressBarLine @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFD9D9D9.toInt() // light gray
        style = Paint.Style.FILL
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF00B600.toInt() // default fill color
        style = Paint.Style.FILL
    }

    private var progress = 0f

    fun setProgress(value: Float) {
        progress = value.coerceAtLeast(0f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = height / 2f
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), radius, radius, backgroundPaint)

        fillPaint.color = if (progress <= 1f) 0xFF00B600.toInt() else 0xFFF6B400.toInt()


        val fillWidth = (progress.coerceAtMost(1f)) * width
        canvas.drawRoundRect(0f, 0f, fillWidth, height.toFloat(), radius, radius, fillPaint)
    }
}

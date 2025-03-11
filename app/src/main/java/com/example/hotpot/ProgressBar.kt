package com.example.hotpot

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var maxProgress = 2000f

    private val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY // Outline color
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height.toFloat()

        // Ensure the radius fits inside the given width and height
        val radius = width * 0.4f

        val rectF = RectF(
            centerX - radius,     // Left
            centerY - radius * 2, // Top
            centerX + radius,     // Right
            centerY               // Bottom
        )

        // Dynamically create shader based on actual dimensions
        progressPaint.shader = LinearGradient(
            rectF.left, rectF.top, rectF.right, rectF.bottom,
            intArrayOf(Color.GREEN, Color.parseColor("#A5D6A7")),
            null,
            Shader.TileMode.CLAMP
        )

        // Draw the arc outline
        canvas.drawArc(rectF, 250f, 250f, false, outlinePaint)

        // Draw progress arc
        val sweepAngle = (progress / maxProgress) * 250f
        canvas.drawArc(rectF, 250f, sweepAngle, false, progressPaint)
    }

    fun setProgress(calories: Float) {
        progress = calories.coerceIn(0f, maxProgress)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Make sure height is smaller than width (half-circle effect)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = (width * 0.5).toInt() // Half of width

        setMeasuredDimension(width, height)
    }
}

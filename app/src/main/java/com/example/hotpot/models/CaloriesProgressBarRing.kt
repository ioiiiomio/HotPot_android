package com.example.hotpot.models

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider

class CaloriesProgressBarRing @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Daily norm and current calories – update these as needed.
    var dailyNorm: Float = 2000f
    var currentCalories: Float = 0f
        set(value) {
            field = value
            invalidate() // Redraw when value changes.
        }

    // Define stroke widths.
    private val ringStrokeWidth = 65f        // Overall ring thickness (used for background & outline)
    private val progressStrokeWidth = 40f    // Thickness for the colored progress arc

    // Paint for the background arc (white).
    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.WHITE
        strokeWidth = ringStrokeWidth
    }

    // Paint for the progress arc outline (white border).
    private val paintProgressOutline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.WHITE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = ringStrokeWidth
    }

    // Paint for the colored progress arc.
    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = progressStrokeWidth
    }

    // Rectangle bounds for the arc.
    private val rectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Adjust drawing bounds to account for the maximum stroke width.
        val padding = ringStrokeWidth / 2
        rectF.set(padding, padding, w - padding, h - padding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Clip the canvas to the upper half.
        canvas.save()
        canvas.clipRect(0f, 0f, width.toFloat(), height / 1.7f)

        // Draw the background arc.
        canvas.drawArc(rectF, 180f, 180f, false, paintBackground)

        // Calculate the progress sweep angle.
        val progressRatio = currentCalories / dailyNorm
        val sweepAngle = 180f * progressRatio.coerceAtMost(1f)

        // Prepare the progress arc’s paint (gradient if within limit, or red otherwise).
        if (currentCalories <= dailyNorm * 1.05) {
            val gradient = SweepGradient(
                rectF.centerX(), rectF.centerY(),
                intArrayOf(Color.GREEN, Color.parseColor("#66FF66")),
                null
            )
            val matrix = Matrix()
            matrix.preRotate(180f, rectF.centerX(), rectF.centerY())
            gradient.setLocalMatrix(matrix)
            paintProgress.shader = gradient
        } else {
            paintProgress.shader = null
            paintProgress.color = Color.RED
        }

        // Draw the progress arc outline first.
        canvas.drawArc(rectF, 180f, sweepAngle, false, paintProgressOutline)
        // Then draw the colored progress arc on top.
        canvas.drawArc(rectF, 180f, sweepAngle, false, paintProgress)

        // Restore canvas clipping.
        canvas.restore()
    }
}

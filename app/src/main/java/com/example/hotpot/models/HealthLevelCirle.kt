package com.example.hotpot.models


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.hotpot.R
import kotlin.math.min

class HealthLevelCirlce @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 60f
    }

    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 60f
        strokeCap = Paint.Cap.ROUND
    }
    val typefaceCustom = ResourcesCompat.getFont(context, R.font.inria_serif_regular)

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
        typeface = typefaceCustom
    }

    private val rectF = RectF()
    private var animatedProgress = 0f

    var progress = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            animateProgress()
        }

    private fun animateProgress() {
        val animator = ValueAnimator.ofFloat(animatedProgress, progress)
        animator.duration = 800
        animator.addUpdateListener {
            animatedProgress = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = 60f
        val size = min(width, height) - padding * 2
        rectF.set(padding, padding, padding + size, padding + size)

        val startAngle = 126f
        val totalSweep = 288f

        // Draw background arc
        canvas.drawArc(rectF, startAngle, totalSweep, false, paintBackground)

        // Prepare gradient shader
        val gradient = SweepGradient(
            rectF.centerX(), rectF.centerY(),
            intArrayOf(Color.rgb(255, 172, 28), Color.YELLOW, Color.GREEN),
            null
        )
        val matrix = Matrix()
        matrix.preRotate(startAngle - 5f, rectF.centerX(), rectF.centerY())
        gradient.setLocalMatrix(matrix)
        paintProgress.shader = gradient

        // Draw progress arc
        canvas.drawArc(rectF, startAngle, totalSweep * animatedProgress, false, paintProgress)

        // Draw text on path
        val path = Path()
        path.addArc(rectF, startAngle, totalSweep)
        val displayText = "off-peak               imbalanced               good               great              optimal"
        canvas.drawTextOnPath(displayText, path, 0f, -20f, paintText)

    }
}

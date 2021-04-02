package com.udacity.button

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.udacity.R
import kotlin.properties.Delegates

/**Created by
Author: Ankush Bose
Date: 31,March,2021
 **/
class AnimatingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Styling for button & button text
    private var buttonBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
    private var buttonTextColor = Color.WHITE

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var widthSize = 0
    private var heightSize = 0

    // animator
    private var valueAnimator: ValueAnimator

    // For progress
    @Volatile
    private var progress: Double = 0.0

    // for button states
    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
    }


    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()

        invalidate()
        requestLayout()
    }

    init {
        isClickable = true

        valueAnimator = AnimatorInflater.loadAnimator(
            context, R.animator.loading_animation
        ) as ValueAnimator

        valueAnimator.addUpdateListener(updateListener)

        val attr =
            context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)

        try {
            buttonBackgroundColor = attr.getColor(
                R.styleable.LoadingButton_bgColor,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )

            buttonTextColor = attr.getColor(
                R.styleable.LoadingButton_textColor,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
        } finally {
            attr.recycle()
        }
    }

    fun startLoading() {
        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
        valueAnimator.start()
    }

    fun stopLoading() {
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        invalidate()
        requestLayout()
    }

    private val rect = RectF(
        750f,
        50f,
        810f,
        110f
    )


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeWidth = 0f
        paint.color = buttonBackgroundColor
        /*canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)*/
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 20f, 20f, paint)


        if (buttonState == ButtonState.Loading) {
            paint.color = Color.parseColor("#004349")
            canvas.drawRoundRect(
                0f, 0f,
                (width * (progress / 100)).toFloat(), height.toFloat(), 20f, 20f, paint
            )
            paint.color = Color.parseColor("#F9A825")
            canvas.drawArc(rect, 0f, (360 * (progress / 100)).toFloat(), true, paint)
        }
        val buttonText =
            if (buttonState == ButtonState.Loading)
                resources.getString(R.string.loading)
            else resources.getString(R.string.download)

        paint.color = buttonTextColor
        canvas.drawText(
            buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(),
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}

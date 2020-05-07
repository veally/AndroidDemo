package com.example.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View


/**
 *
 * @author veally
 * Create at 2020-04-14
 */
class DynamicScoreView : View {

    private var textSize: Int = 17 //6sp
    private var textColor: Int = Color.BLACK
    private var endColor: Int = Color.GRAY
    private var defaultColor: Int = Color.GRAY
    private var startColor: Int = Color.GRAY
    private var textPadding: Int = 0
    private var chartWidth: Float = 5f
    private var progress: Float = 0.0f
    private var max: Float = 0.0f

    private var text: String = ""
    private var paintText = Paint()
    private val corner = 100
    private var tempProgress: Float = 0f
    private var current: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet? = null) : this(
        context,
        attributeSet,
        0
    )

    constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        handleAttrs(attributeSet)
    }

    private fun handleAttrs(attributeSet: AttributeSet?) {
        attributeSet?.apply {
            val typeArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.DynamicScoreView)
            progress = typeArray.getFloat(R.styleable.DynamicScoreView_progress, 0f)
            max = typeArray.getFloat(R.styleable.DynamicScoreView_max, 0f)
            startColor = typeArray.getColor(R.styleable.DynamicScoreView_startColor, Color.GRAY)
            endColor = typeArray.getColor(R.styleable.DynamicScoreView_endColor, Color.GRAY)
            defaultColor = typeArray.getColor(R.styleable.DynamicScoreView_defaultColor, Color.GRAY)
            textColor = typeArray.getColor(R.styleable.DynamicScoreView_textColor, Color.BLACK)
            textSize = typeArray.getDimensionPixelSize(R.styleable.DynamicScoreView_textSize, 6)
            textPadding =
                typeArray.getDimensionPixelSize(R.styleable.DynamicScoreView_textPadding, 0)
            chartWidth =
                typeArray.getDimensionPixelSize(R.styleable.DynamicScoreView_chartWidth, 5)
                    .toFloat()
            typeArray.recycle()
        }

        paintText.color = textColor
        paintText.isAntiAlias = true
        paintText.textSize = textSize.toFloat()
        // todo
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
//        canvas?.apply {
//            drawRect(RectF(0f, 0f, width.toFloat(), height.toFloat()),
//                Paint().apply
//                { color = Color.LTGRAY })
//        }
        val textX = width * 0.5f - paintText.measureText(text) * 0.5f
        val textH = px2dp(paintText.descent() - paintText.ascent())
        if (progress == 0f) {
            canvas?.apply {
                drawText(text, textX, height - textH - textPadding, paintText)
            }
        } else {
            val per = progress / max
            tempProgress = if (tempProgress < progress) tempProgress + per else progress.toFloat()
            val canH = height - dp2px(textH.toInt()) - textPadding //计算柱形图可绘制高度
            val drawH = canH / max.toFloat() * tempProgress
//            canvas?.apply {
//                drawRect(RectF(0f, height - canH, width.toFloat(), height.toFloat()),
//                    Paint().apply
//                    { color = Color.RED })
//            }
//            Log.i(
//                "ScoreView",
//                ">>>>>>>> progress:$tempProgress height:$height  textH:$textH canH:$canH drawH:$drawH textPadding:$textPadding cH:${height - drawH}"
//            )

            val chartPaint = Paint().apply {
                if (current) {
                    shader = LinearGradient(
                        width * 0.5f - chartWidth * 0.5f,
                        height - drawH,
                        width * 0.5f + chartWidth * 0.5f,
                        height.toFloat(),
                        endColor,
                        startColor,
                        Shader.TileMode.CLAMP
                    )
                } else {
                    color = defaultColor
                }
                isAntiAlias = true
            }
            val rectF = RectF(
                width * 0.5f - chartWidth * 0.5f,
                height - drawH,
                width * 0.5f + chartWidth * 0.5f,
                height.toFloat()
            )
            canvas?.apply {
                drawRoundRect(rectF, dp2px(corner), dp2px(corner), chartPaint)
                drawText(
                    text,
                    width * 0.5f - paintText.measureText(text) * 0.5f,
                    height - textH - drawH - textPadding,
                    paintText
                )
            }
            if (tempProgress < progress) {
                postInvalidate()
            }
        }

    }

    fun setText(text: String, progress: Float, max: Float, current: Boolean) {
        this.text = text
        this.progress = if (progress > max) max else progress
        this.max = max
        this.current = current
        postInvalidate()
    }

    private fun dp2px(value: Int): Float {
        val v = context.resources.displayMetrics.density
        return (v * value + 0.5f)
    }

    private fun px2dp(value: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (value / scale + 0.5f)
    }

    fun sp2px(spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}
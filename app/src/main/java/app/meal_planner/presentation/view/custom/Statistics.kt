package app.meal_planner.presentation.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import app.meal_planner.R
import timber.log.Timber

class Statistics: View {

    constructor(context: Context)
            : super(context)
    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        getColor(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        getColor(context, attrs)
    }

    private var rect: Rect = Rect()
    private lateinit var paint: Paint
    private val sevenDays = 7
    private val thirtyDays = 30
    var values = listOf<Int>()
    var type: String = ""

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var max = 0

        values.forEach {
            if(it > max){
                max = it
            }
        }

        if(max > height){
            max /= height
        }

        if(max == 0){ return }

        var begin = 0
        val heightOffset = height / max
        var rightOffset = width / sevenDays - (sevenDays - 1)
        val bottom = height

        if (values.size > thirtyDays / 2){
            rightOffset = width / thirtyDays - (sevenDays - 1)
        }
//        var begin = calculateBeginning(rightOffset, values.size)

        values.forEach {
            val left = begin
            val value: Int = if(it > height){ it / height }else{ it }
            val top = height - heightOffset * value
            val right = left + rightOffset
            begin = right + sevenDays
            rect.set(left, top, right, bottom)
            canvas?.drawRect(rect, paint)
        }
    }

    private fun getColor(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Statistics)
        val barColor = typedArray.getColor(R.styleable.Statistics_barColor, 0)
        typedArray.recycle()
        paint = Paint().also {
            it.isAntiAlias = true
            it.color = barColor
            it.style = Paint.Style.FILL
        }
    }

    private fun calculateBeginning(rightOffset: Int, len: Int): Int{
        return (width / 2) - ((len * rightOffset + ((len - 1) * sevenDays) ) / 2)
    }
}
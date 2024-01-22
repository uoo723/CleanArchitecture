package com.umanji.umanjiapp.common.util

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.Selection
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.umanji.umanjiapp.R
import java.text.NumberFormat


fun moneyView(amount: Int) : String{
    val strMoney = NumberFormat.getIntegerInstance().format(amount)
    return strMoney
}

fun TextView.generateClickableSpan(
        updateDrawState: (ds: TextPaint) -> Unit = {
            it.typeface = Typeface.DEFAULT_BOLD
            it.textSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, 15f, resources.displayMetrics)
        },
        normalTextColor: Int = ContextCompat.getColor(context, R.color.black),
        pressedTextColor: Int = ContextCompat.getColor(context, R.color.black),
        pressedBackgroundColor: Int = ContextCompat.getColor(context, R.color.colorAccent),
        onClick: () -> Unit): TouchableSpan {
    return object : TouchableSpan(normalTextColor, pressedTextColor, pressedBackgroundColor) {
        override fun onClick(widget: View?) {
            onClick.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            updateDrawState.invoke(ds)
        }
    }
}

fun String.setSpan(spannable: CharSequence, span: Any,
                   flags: Int = Spannable.SPAN_INCLUSIVE_EXCLUSIVE) {
    if (spannable !is Spannable) return
    val start = spannable.indexOf(this)
    spannable.setSpan(span, start, start + length, flags)
}

abstract class TouchableSpan(
        private val normalTextColor: Int,
        private val pressedTextColor: Int,
        private val pressedBackgroundColor: Int
) : ClickableSpan() {

    var isPressed: Boolean = false

    override fun updateDrawState(ds: TextPaint) {
        ds.color = if (isPressed) pressedTextColor else normalTextColor
        ds.bgColor = if (isPressed) pressedBackgroundColor else 0xffffffff.toInt()
    }
}

object LinkTouchMovementMethod : LinkMovementMethod() {
    private var pressedSpan: TouchableSpan? = null

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressedSpan = getPressedSpan(widget, buffer, event)
                pressedSpan?.let {
                    it.isPressed = true
                    Selection.setSelection(buffer, buffer.getSpanStart(it), buffer.getSpanEnd(it))
                }
            }
            MotionEvent.ACTION_MOVE -> {
                pressedSpan?.let {
                    if (it != getPressedSpan(widget, buffer, event)) {
                        it.isPressed = false
                        pressedSpan = null
                        Selection.removeSelection(buffer)
                    }
                }
            }
            else -> {
                if (pressedSpan != null) {
                    pressedSpan?.isPressed = false
                    super.onTouchEvent(widget, buffer, event)
                }
                pressedSpan = null
                Selection.removeSelection(buffer)
            }
        }
        return true
    }

    private fun getPressedSpan(textView: TextView, spannable: Spannable, event: MotionEvent)
            : TouchableSpan? {
        val x: Int = (event.x - textView.totalPaddingLeft + textView.scrollX).toInt()
        val y: Int = (event.y - textView.totalPaddingTop + textView.scrollY).toInt()
        val position: Int = textView.layout.let {
            it.getOffsetForHorizontal(it.getLineForVertical(y), x.toFloat())
        }

        val link: Array<TouchableSpan> = spannable.getSpans(position, position, TouchableSpan::class.java)

        return if (link.isNotEmpty() && positionWithinTag(position, spannable, link[0])) {
            link[0]
        } else {
            null
        }
    }

    private fun positionWithinTag(position: Int, spannable: Spannable, tag: Any): Boolean {
        return position >= spannable.getSpanStart(tag) && position <= spannable.getSpanEnd(tag)
    }
}

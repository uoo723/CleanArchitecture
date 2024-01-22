package com.umanji.umanjiapp.common.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View


fun View.getBitmap(): Bitmap {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    layout(0, 0, measuredWidth, measuredHeight)
    buildDrawingCache()

    return Bitmap.createBitmap(measuredWidth, measuredHeight,
            Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        background?.draw(canvas)
        this@getBitmap.draw(canvas)
    }
}

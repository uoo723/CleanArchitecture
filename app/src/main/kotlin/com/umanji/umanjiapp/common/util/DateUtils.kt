package com.umanji.umanjiapp.common.util

import android.content.Context
import android.support.annotation.ArrayRes
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class DateUtils @Inject constructor(private val context: Context) {

    object TimeMaximum {
        const val SEC = 60
        const val MIN = 60
        const val HOUR = 24
        const val DAY = 30
        const val MONTH = 12
    }

    open fun getTimeString(date: Date, @ArrayRes decorStringsRes: Int): String {
        val curTime: Long = Calendar.getInstance().timeInMillis
        var diffTime: Long = (curTime - date.time) / 1000
        val decorStrings: Array<String> = context.resources.getStringArray(decorStringsRes)

        return when {
            diffTime < TimeMaximum.SEC -> decorStrings[0]
            { diffTime /= TimeMaximum.SEC; diffTime }() < TimeMaximum.MIN ->
                "$diffTime ${decorStrings[1]}"
            { diffTime /= TimeMaximum.MIN; diffTime }() < TimeMaximum.HOUR ->
                "$diffTime ${decorStrings[2]}"
            { diffTime /= TimeMaximum.HOUR; diffTime }() < TimeMaximum.DAY ->
                "$diffTime ${decorStrings[3]}"
            { diffTime /= TimeMaximum.DAY; diffTime }() < TimeMaximum.MONTH ->
                "$diffTime ${decorStrings[4]}"
            else -> "$diffTime ${decorStrings[5]}"
        }
    }
}

package com.xwjr.track.attend.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


fun secondToTime(time: Int): String {
    val timeStr: String?
    val hour: Int
    var minute: Int
    val second: Int
    if (time <= 0)
        return "00:00"
    else {
        minute = time / 60
        if (minute < 60) {
            second = time % 60
//            timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second)
            timeStr = "00:" + unitFormat(minute)
        } else {
            hour = minute / 60
            if (hour > 99)
                return "99:59:59"
            minute %= 60
            second = time - hour * 3600 - minute * 60
//            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            timeStr = unitFormat(hour) + ":" + unitFormat(minute)
        }
    }
    return timeStr
}

fun unitFormat(i: Int): String {
    return if (i in 0..9)
        "0" + Integer.toString(i)
    else
        "" + i
}


/**
 * 初始化日期布局
 */
@SuppressLint("SimpleDateFormat", "SetTextI18n")
fun String.getWeekData(): String {
    val calendar = Calendar.getInstance()
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    calendar.time = simpleDateFormat.parse(this)
    val str1 = arrayOf("", "日", "一", "二", "三", "四", "五", "六")
    return "星期" + str1[calendar.get(Calendar.DAY_OF_WEEK)]
}
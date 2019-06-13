package com.xwjr.track.attend.extension


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
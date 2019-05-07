package com.xwjr.track.attend.extension

import android.content.Context
import android.content.IntentFilter
import com.xwjr.track.attend.broadcast.AutoSignReceiver

const val AUTO_SIGN_RECEIVER = "attend.broadcast.AutoSignReceiver"

fun Context.registerAutoSignBroadCast() {
    val filter = IntentFilter()
    filter.addAction(AUTO_SIGN_RECEIVER)
    this.registerReceiver(AutoSignReceiver(), filter)
}
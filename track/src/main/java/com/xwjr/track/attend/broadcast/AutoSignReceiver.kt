package com.xwjr.track.attend.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.xwjr.track.LogUtils
import com.xwjr.track.attend.service.AutoSignService

class AutoSignReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        LogUtils.i("启动自动签到service")
        intent.setClass(context,AutoSignService::class.java)
        context.startService(intent)
    }
}
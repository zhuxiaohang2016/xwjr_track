package com.xwjr.track.attend.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.xwjr.track.LogUtils
import java.util.*

class AutoSignService : Service() {

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    override fun onCreate() {
        super.onCreate()
        LogUtils.i("AutoSignService  onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtils.i("AutoSignService  onStartCommand")

        when (intent?.getBooleanExtra("autoSignStatus", false)) {
            true -> {
                if (timer == null || timerTask == null) {
                    timer = Timer()
                    timerTask = object : TimerTask() {
                        override fun run() {
                            LogUtils.i("调用签到接口")
                        }
                    }
                    timer?.schedule(timerTask, 1000, 10000)
                    LogUtils.i("启动自动签到服务")
                }
            }
            false -> {
                timerTask = null
                timer?.cancel()
                timer = null
                LogUtils.i("取消自动签到服务")
            }
        }
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask = null
        timer?.cancel()
        timer = null
        LogUtils.i("AutoSignService onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder {
        LogUtils.i("AutoSignService onBind")
        throw UnsupportedOperationException("不支持onBind方法")
    }

}
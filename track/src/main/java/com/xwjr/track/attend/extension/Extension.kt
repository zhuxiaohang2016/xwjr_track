package com.xwjr.track.attend.extension

import android.os.Looper
import android.util.Log
import com.xwjr.track.TrackConfig
import com.xwjr.track.attend.util.ToastUtils
import java.io.File
import java.util.*

private const val TAG = "xwjrStaple"
/**
 * 日志打印扩展
 */
fun Any?.logI(content: String) {
    try {
        if (TrackConfig.isDebug()) {
            Log.i(TAG, content)
        }
    } catch (e: Exception) {
        Log.i(TAG, "日志打印错误")
        e.printStackTrace()
    }
}

/**
 * 日志打印扩展
 */
fun Any?.logE(content: String) {
    try {
        if (TrackConfig.isDebug()) {
            Log.e(TAG, content)
        }
    } catch (e: Exception) {
        Log.e(TAG, "日志打印错误")
        e.printStackTrace()
    }
}


fun Any.showToast(content: String?) {
    try {
        if (content != null) {
            ToastUtils.showShortToast(content)
        } else {
            logI("toast内容为空")
        }
    } catch (e: Exception) {
        logE("toast失败")
        e.printStackTrace()
    }
}

fun Any.showToast(resID: Int?) {
    try {
        if (resID != null) {
            ToastUtils.showShortToast(TrackConfig.getContext().getString(resID))
        } else {
            logI("toast资源id为空")
        }
    } catch (e: Exception) {
        logE("toast失败")
        e.printStackTrace()
    }
}

/**
 *延迟处理
 */
fun Any?.laterDeal(time: Long = 3000, deal: (() -> Unit)? = null) {
    try {
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                logI("倒计时结束")
                if (deal != null) {
                    logI("倒计时结束后，开始执行任务")
//                    Looper.getMainLooper()
                    deal()
//                    Looper.loop()
                }
            }
        }
        logI("开始倒计时")
        timer.schedule(task, time)
    } catch (e: Exception) {
        if (deal != null) {
            deal()
        }
        e.printStackTrace()
        logE("倒计时发生异常")
    }
}

/**
 * 创建文件并赋予 777 权限
 */
fun Any?.newFile(filePath: String): File? {
    try {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        try {
            //给各个目录授予权限
            val filePaths = file.path.split("/")
            val sb = StringBuilder()
            for (i in filePaths.indices) {
                sb.append(filePaths[i])
                if (i < filePaths.size - 1) {
                    sb.append("/")
                }
                val cmd = arrayOf("chmod", "777", sb.toString())
                val processBuilder = ProcessBuilder(*cmd)
                try {
                    processBuilder.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                    logE("文件授权chmod异常->filePath:$filePath")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logE("文件授权chmod异常->filePath:$filePath")
        }
        return file
    } catch (e: Exception) {
        e.printStackTrace()
        logE("创建文件异常->filePath:$filePath")
        return null
    }
}

/**
 * 获取文件并赋予 777 权限
 */
fun Any?.getFile(filePath: String): File? {
    try {
        val file = File(filePath)
        try {
            //给各个目录授予权限
            val filePaths = file.path.split("/")
            val sb = StringBuilder()
            for (i in filePaths.indices) {
                sb.append(filePaths[i])
                if (i < filePaths.size - 1) {
                    sb.append("/")
                }
                val cmd = arrayOf("chmod", "777", sb.toString())
                val processBuilder = ProcessBuilder(*cmd)
                try {
                    processBuilder.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                    logE("文件授权chmod异常->filePath:$filePath")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logE("文件授权chmod异常->filePath:$filePath")
        }
        return file
    } catch (e: Exception) {
        e.printStackTrace()
        logE("获取文件异常->filePath:$filePath")
        return null
    }
}

/**
 * 获取文件并赋予 777 权限
 */
fun Any?.chmodPath(path: String) {
    try {
        try {
            //给各个目录授予权限
            val filePaths = path.split("/")
            val sb = StringBuilder()
            for (i in filePaths.indices) {
                sb.append(filePaths[i])
                if (i < filePaths.size - 1) {
                    sb.append("/")
                }
                val cmd = arrayOf("chmod", "777", sb.toString())
                val processBuilder = ProcessBuilder(*cmd)
                try {
                    processBuilder.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                    logE("文件授权chmod异常->filePath:$path")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logE("文件授权chmod异常->path:$path")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        logE("获取文件异常->path:$path")
    }
}

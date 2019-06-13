package com.xwjr.track.attend.extension

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
/**
 * .err()
 */
fun String.err(): String {
    return "#$this"
}

/**
 * 判断字符串不为空
 */
fun String?.isNotNullOrEmpty(): Boolean {
    try {
        if (this == null) {
            return false
        }
        if (this.isEmpty()) {
            return false
        }
        return true
    } catch (e: Exception) {
        logI("发生异常：判断字符串不为空时发生异常")
        e.printStackTrace()
        return false
    }

}

/**
 * 字符串md5加密
 */
fun String.md5(): String {
    try {
        if (!this.isNotNullOrEmpty()) {
            return ""
        }
        val md5 = MessageDigest.getInstance("MD5")
        val bytes = md5!!.digest(this.toByteArray())
        val result = StringBuilder()
        for (b in bytes) {
            var temp = Integer.toHexString(b.toInt() and 0xff)
            if (temp.length == 1) {
                temp = "0$temp"
            }
            result.append(temp)
        }
        logI("md5加密成功：$result")
        return result.toString()
    } catch (e: NoSuchAlgorithmException) {
        logI("发生异常：md5加密")
        e.printStackTrace()
        return ""
    }

}

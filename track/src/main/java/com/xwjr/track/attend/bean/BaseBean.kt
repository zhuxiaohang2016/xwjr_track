package com.xwjr.track.attend.bean

import com.xwjr.track.attend.extension.showToast

open class BaseBean {
    val code: String? = null
    val message: String? = null

    fun checkCode(): Boolean {
        return code == "0000"
    }

    fun checkCodeIfErrorShow(): Boolean {
        return if (code == "0000") {
            true
        } else {
            showToast(message)
            false
        }
    }
}

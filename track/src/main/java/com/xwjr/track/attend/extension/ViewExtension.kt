@file:Suppress("DEPRECATION")

package com.xwjr.track.attend.extension

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.TypedValue
import android.util.TypedValue.applyDimension
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.google.gson.annotations.Until
import com.xwjr.track.R
import java.util.*

//初始化checkBox DrawableLeft图片问题
fun Context.initCheckBoxView(rb: CheckBox) {
    val rbTrue = resources.getDrawable(R.drawable.attend_switch)
    rbTrue.setBounds(
            0,
            0,
            applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64f, resources.displayMetrics).toInt(),
            applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt()
    )
    rb.setCompoundDrawables(rbTrue, null, null, null)
}


//popupWindow弹出签到成功 2s后自动消失
fun Context.showSignSuccess() {
    try {
        val view = View.inflate(this, R.layout.attend_sign_success, null)
        val popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.apply {
            isFocusable = true
            isOutsideTouchable = true
            isTouchable = true
            setBackgroundDrawable(BitmapDrawable())
            showAtLocation(view, Gravity.CENTER, 0, 0)
        }

        var timer: Timer? = Timer()
        var timeTask: TimerTask?
        timeTask = object : TimerTask() {
            override fun run() {
                try {
                    view.post {
                        popupWindow.dismiss()
                    }
                    timer = null
                    timeTask = null
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        timer?.schedule(timeTask, 2000)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

//popupWindow弹出提示信息
fun Context.showTip(description: String? = "", buttonText: String? = null, error: (() -> Unit)? = null, deal: (() -> Unit)? = null) {
    try {
        val view = View.inflate(this, R.layout.attend_common_tip, null)
        val popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )

        val tvDescription = view.findViewById(R.id.tv_description) as TextView
        val tvSure = view.findViewById(R.id.tv_sure) as TextView
        val ivClose = view.findViewById(R.id.iv_close) as ImageView

        tvDescription.text = description
        tvSure.text = buttonText

        ivClose.setOnClickListener {
            if (error != null) error()
            popupWindow.dismiss()
        }

        tvSure.setOnClickListener {
            if (deal!=null) deal()
            popupWindow.dismiss()
        }


        popupWindow.apply {
            isFocusable = true
            isOutsideTouchable = true
            isTouchable = true
            setBackgroundDrawable(BitmapDrawable())
            showAtLocation(view, Gravity.CENTER, 0, 0)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}


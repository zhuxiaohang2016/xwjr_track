@file:Suppress("DEPRECATION")

package com.xwjr.track.attend.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.support.constraint.Group
import android.util.TypedValue
import android.util.TypedValue.applyDimension
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.xwjr.track.R
import com.xwjr.track.attend.bean.AttendRecordListBean
import com.xwjr.track.attend.widget.WheelView
import java.util.*

//初始化checkBox DrawableLeft图片问题
fun TextView.initDrawableLeftView(resId: Int, width: Float, height: Float) {
    val rbTrue = resources.getDrawable(resId)
    rbTrue.setBounds(
            0,
            0,
            applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, resources.displayMetrics).toInt(),
            applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, resources.displayMetrics).toInt()
    )
    this.setCompoundDrawables(rbTrue, null, null, null)
}

//初始化checkBox DrawableLeft图片问题
fun TextView.initDrawableRightView(resId: Int, width: Float, height: Float) {
    val rbTrue = resources.getDrawable(resId)
    rbTrue.setBounds(
            0,
            0,
            applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, resources.displayMetrics).toInt(),
            applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, resources.displayMetrics).toInt()
    )
    this.setCompoundDrawables(null, null, rbTrue, null)
}


//popupWindow弹出签到成功 2s后自动消失
fun Context.showSignSuccess() {
    try {
        val view = View.inflate(this, R.layout.attend_tip_sign_success, null)
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
        val view = View.inflate(this, R.layout.attend_tip_common, null)
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
            if (deal != null) deal()
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


//popupWindow弹出提示时间选择
fun Context.showTimeRangeSelect(timeStart: String = "", timeEnd: String = "", deal: ((hourStart: String, secondStart: String, hourEnd: String, secondEnd: String) -> Unit)? = null) {
    try {
        var hourStart = ""
        var secondStart = ""
        var hourEnd = ""
        var secondEnd = ""

        val hoursString = arrayListOf("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")
        val secondsString = arrayListOf(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
                "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
                "50", "51", "52", "53", "54", "55", "56", "57", "58", "59")
        val view = View.inflate(this, R.layout.attend_tip_time_range_select, null)
        val popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )

        val tvCancel = view.findViewById(R.id.tv_cancel) as TextView
        val tvSure = view.findViewById(R.id.tv_sure) as TextView
        val wvHourStart = view.findViewById(R.id.wv_hour_start) as WheelView
        val wvSecondStart = view.findViewById(R.id.wv_second_start) as WheelView
        val wvHourEnd = view.findViewById(R.id.wv_hour_end) as WheelView
        val wvSecondEnd = view.findViewById(R.id.wv_second_end) as WheelView

        wvHourStart.offset = 1
        wvHourStart.setItems(hoursString)
        wvHourStart.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                hourStart = hoursString[selectedIndex - 1]
            }
        })
        if (timeStart.isNotNullOrEmpty())
            wvHourStart.setSelectItem(timeStart.split(":")[0])
        else
            wvHourStart.setSelectItem("00")

        wvSecondStart.offset = 1
        wvSecondStart.setItems(secondsString)
        wvSecondStart.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                secondStart = secondsString[selectedIndex - 1]
            }
        })

        if (timeStart.isNotNullOrEmpty())
            wvSecondStart.setSelectItem(timeStart.split(":")[1])
        else
            wvSecondStart.setSelectItem("00")


        wvHourEnd.offset = 1
        wvHourEnd.setItems(hoursString)
        wvHourEnd.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                hourEnd = hoursString[selectedIndex - 1]
            }
        })

        if (timeEnd.isNotNullOrEmpty())
            wvHourEnd.setSelectItem(timeEnd.split(":")[0])
        else
            wvHourEnd.setSelectItem("00")

        wvSecondEnd.offset = 1
        wvSecondEnd.setItems(secondsString)
        wvSecondEnd.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                secondEnd = secondsString[selectedIndex - 1]
            }
        })

        if (timeEnd.isNotNullOrEmpty())
            wvSecondEnd.setSelectItem(timeEnd.split(":")[1])
        else
            wvSecondEnd.setSelectItem("00")


        tvCancel.setOnClickListener {
            popupWindow.dismiss()
        }

        tvSure.setOnClickListener {
            if (deal != null) deal(hourStart, secondStart, hourEnd, secondEnd)
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


//popupWindow弹出提示星期选择
fun Context.showWeekSelect(defaultData: String = "", deal: ((dataString: MutableList<String>) -> Unit)? = null) {
    try {
        val view = View.inflate(this, R.layout.attend_tip_week_select, null)
        val popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )

        val tvCancel = view.findViewById(R.id.tv_cancel) as TextView
        val tvSure = view.findViewById(R.id.tv_sure) as TextView
        val cbMonday = view.findViewById(R.id.cb_monday) as CheckBox
        val cbTuesday = view.findViewById(R.id.cb_tuesday) as CheckBox
        val cbWednesday = view.findViewById(R.id.cb_wednesday) as CheckBox
        val cbThursday = view.findViewById(R.id.cb_thursday) as CheckBox
        val cbFriday = view.findViewById(R.id.cb_friday) as CheckBox
        val cbSaturday = view.findViewById(R.id.cb_saturday) as CheckBox
        val cbSunday = view.findViewById(R.id.cb_sunday) as CheckBox

        cbMonday.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)
        cbTuesday.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)
        cbWednesday.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)
        cbThursday.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)
        cbFriday.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)
        cbSaturday.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)
        cbSunday.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)

        defaultData.split(",").forEach {
            when (it) {
                cbMonday.text.toString() -> {
                    cbMonday.isChecked = true
                }
                cbTuesday.text.toString() -> {
                    cbTuesday.isChecked = true
                }
                cbWednesday.text.toString() -> {
                    cbWednesday.isChecked = true
                }
                cbThursday.text.toString() -> {
                    cbThursday.isChecked = true
                }
                cbFriday.text.toString() -> {
                    cbFriday.isChecked = true
                }
                cbSaturday.text.toString() -> {
                    cbSaturday.isChecked = true
                }
                cbSunday.text.toString() -> {
                    cbSunday.isChecked = true
                }
            }
        }

        tvCancel.setOnClickListener {
            popupWindow.dismiss()
        }

        tvSure.setOnClickListener {
            val result = arrayListOf<String>()
            if (cbMonday.isChecked) {
                result.add(cbMonday.text.toString())
            }
            if (cbTuesday.isChecked) {
                result.add(cbTuesday.text.toString())
            }
            if (cbWednesday.isChecked) {
                result.add(cbWednesday.text.toString())
            }
            if (cbThursday.isChecked) {
                result.add(cbThursday.text.toString())
            }
            if (cbFriday.isChecked) {
                result.add(cbFriday.text.toString())
            }
            if (cbSaturday.isChecked) {
                result.add(cbSaturday.text.toString())
            }
            if (cbSunday.isChecked) {
                result.add(cbSunday.text.toString())
            }
            if (deal != null) deal(result)
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


//popupWindow弹出提示时间选择
fun Context.showWheelSelect(dataString: MutableList<String>, deal: ((selectData: String) -> Unit)? = null) {
    try {
        var selectData = ""
        val view = View.inflate(this, R.layout.attend_tip_wheel_select, null)
        val popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )

        val tvCancel = view.findViewById(R.id.tv_cancel) as TextView
        val tvSure = view.findViewById(R.id.tv_sure) as TextView
        val wvWheel = view.findViewById(R.id.wv_wheel) as WheelView

        wvWheel.offset = 1
        wvWheel.setItems(dataString)
        wvWheel.onWheelViewListener = (object : WheelView.OnWheelViewListener() {
            override fun onSelected(selectedIndex: Int, item: String?) {
                selectData = dataString[selectedIndex - 1]
            }
        })
        wvWheel.setSelectItem(selectData)


        tvCancel.setOnClickListener {
            popupWindow.dismiss()
        }

        tvSure.setOnClickListener {
            if (deal != null) deal(selectData)
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


//popupWindow弹出提示信息
@SuppressLint("SetTextI18n")
fun Context.showAbnormalAttendRecord(abnormalAttendRecordListBean: AttendRecordListBean? = null) {
    try {
        val view = View.inflate(this, R.layout.attend_tip_attend_record, null)
        val popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )

        val tvSure = view.findViewById(R.id.tv_sure) as TextView
        val ivClose = view.findViewById(R.id.iv_close) as ImageView
        val llContent = view.findViewById(R.id.ll_content) as LinearLayout

        val lateData = abnormalAttendRecordListBean!!.data!!.records!!.filter {
            it.checkinResult == "late"
        }
        if (lateData.isNotEmpty()) {
            val lateTitle = View.inflate(this, R.layout.attend_tip_attend_record_title, null)
            val tvStatus = lateTitle.findViewById(R.id.tv_status) as TextView
            val tvTimes = lateTitle.findViewById(R.id.tv_times) as TextView
            tvStatus.initDrawableLeftView(R.mipmap.attend_dot_blue, 6f, 6f)
            tvStatus.text = "迟到"
            tvTimes.text = "共" + lateData.size + "次"
            llContent.addView(lateTitle)
            lateData.forEach {
                val lateContent = View.inflate(this, R.layout.attend_tip_attend_record_content, null)
                val tvSignTimeDetail = lateContent.findViewById(R.id.tv_sign_time_detail) as TextView
                val groupLocation = lateContent.findViewById(R.id.group_location) as Group
                tvSignTimeDetail.text = it.checkinTime + "    " + it.checkinTime?.substring(0, 10)?.getWeekData()
                groupLocation.visibility = View.GONE
                llContent.addView(lateContent)
            }
        }

        val earlyData = abnormalAttendRecordListBean.data!!.records!!.filter {
            it.checkinResult == "leaveEarly"
        }
        if (earlyData.isNotEmpty()) {
            val earlyTitle = View.inflate(this, R.layout.attend_tip_attend_record_title, null)
            val tvStatus = earlyTitle.findViewById(R.id.tv_status) as TextView
            val tvTimes = earlyTitle.findViewById(R.id.tv_times) as TextView
            tvStatus.initDrawableLeftView(R.mipmap.attend_dot_red, 6f, 6f)
            tvStatus.text = "早退"
            tvTimes.text = "共" + earlyData.size + "次"
            llContent.addView(earlyTitle)
            earlyData.forEach {
                val earlyContent = View.inflate(this, R.layout.attend_tip_attend_record_content, null)
                val tvSignTimeDetail = earlyContent.findViewById(R.id.tv_sign_time_detail) as TextView
                val groupLocation = earlyContent.findViewById(R.id.group_location) as Group
                tvSignTimeDetail.text = it.checkinTime + "    " + it.checkinTime?.substring(0, 10)?.getWeekData()
                groupLocation.visibility = View.GONE
                llContent.addView(earlyContent)
            }
        }


        val forgetData = abnormalAttendRecordListBean.data!!.records!!.filter {
            it.checkinResult == "uncheckin"
        }
        if (forgetData.isNotEmpty()) {
            val forgetTitle = View.inflate(this, R.layout.attend_tip_attend_record_title, null)
            val tvStatus = forgetTitle.findViewById(R.id.tv_status) as TextView
            val tvTimes = forgetTitle.findViewById(R.id.tv_times) as TextView
            tvStatus.initDrawableLeftView(R.mipmap.attend_dot_orange, 6f, 6f)
            tvStatus.text = "未打卡"
            tvTimes.text = "共" + forgetData.size + "次"
            llContent.addView(forgetTitle)
            forgetData.forEach {
                val forgetContent = View.inflate(this, R.layout.attend_tip_attend_record_content, null)
                val tvSignTimeDetail = forgetContent.findViewById(R.id.tv_sign_time_detail) as TextView
                val groupLocation = forgetContent.findViewById(R.id.group_location) as Group
                tvSignTimeDetail.text = it.checkinTime + "    " + it.checkinTime?.substring(0, 10)?.getWeekData()
                groupLocation.visibility = View.GONE
                llContent.addView(forgetContent)
            }
        }


        val outsideData = abnormalAttendRecordListBean.data!!.records!!.filter {
            it.checkinOutside == "outside"
        }
        if (outsideData.isNotEmpty()) {
            val outsideTitle = View.inflate(this, R.layout.attend_tip_attend_record_title, null)
            val tvStatus = outsideTitle.findViewById(R.id.tv_status) as TextView
            val tvTimes = outsideTitle.findViewById(R.id.tv_times) as TextView
            tvStatus.initDrawableLeftView(R.mipmap.attend_dot_blue_soft, 6f, 6f)
            tvStatus.text = "外勤"
            tvTimes.text = "共" + outsideData.size + "次"
            llContent.addView(outsideTitle)
            outsideData.forEach {
                val outsideContent = View.inflate(this, R.layout.attend_tip_attend_record_content, null)
                val tvSignTimeDetail = outsideContent.findViewById(R.id.tv_sign_time_detail) as TextView
                val groupLocation = outsideContent.findViewById(R.id.group_location) as Group
                val tvLocationDes = outsideContent.findViewById(R.id.tv_location_des) as TextView
                tvSignTimeDetail.text = it.checkinTime + "    " + it.checkinTime?.substring(0, 10)?.getWeekData()
                groupLocation.visibility = View.VISIBLE
                tvLocationDes.text = it.locationDetail
                llContent.addView(outsideContent)
            }
        }


        ivClose.setOnClickListener {
            popupWindow.dismiss()
        }

        tvSure.setOnClickListener {
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


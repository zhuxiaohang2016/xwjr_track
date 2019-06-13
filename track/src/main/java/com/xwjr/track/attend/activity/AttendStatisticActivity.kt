package com.xwjr.track.attend.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xwjr.track.R
import com.xwjr.track.attend.extension.initDrawableRightView
import kotlinx.android.synthetic.main.activity_attend_statistic.*
import kotlinx.android.synthetic.main.attend_title.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 考勤统计页面
 */
class AttendStatisticActivity : AppCompatActivity() {

    private var year = 2019
    private var month = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend_statistic)

        init()
        setListener()
        defaultData()
    }

    private fun init() {
        tv_title.text = "考勤统计"
        tv_right.visibility = View.GONE
        getTimeData()
        updateYearMonthView()
        tv_filter.initDrawableRightView(R.mipmap.attend_icon_direction_down, 10f, 6f)
        queryData()
    }

    private fun queryData() {
        
    }

    private fun setListener() {
        iv_back.setOnClickListener { finish() }

        iv_next_month.setOnClickListener {
            if (month == 12) {
                month = 1
                year++
            } else {
                month++
            }
            updateYearMonthView()
        }
        iv_last_month.setOnClickListener {
            if (month == 1) {
                month = 12
                year--
            } else {
                month--
            }
            updateYearMonthView()
        }
    }

    private fun defaultData() {
        updateRecycleView()
    }

    /**
     * 获取时:分
     */
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun getTimeData(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM")
        val date = simpleDateFormat.format(Date())
        year = date.split("-")[0].toInt()
        month = date.split("-")[1].toInt()
        return date
    }

    @SuppressLint("SetTextI18n")
    private fun updateYearMonthView() {
        tv_year_and_month.text = "${year}年${month}月"
    }

    private fun updateRecycleView() {
    }
}

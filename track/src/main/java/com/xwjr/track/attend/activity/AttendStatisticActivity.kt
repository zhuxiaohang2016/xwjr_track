package com.xwjr.track.attend.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xwjr.track.R
import com.xwjr.track.attend.extension.initDrawableRightView
import kotlinx.android.synthetic.main.activity_attend_statistic.*
import kotlinx.android.synthetic.main.attend_title.*

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

        tv_filter.initDrawableRightView(R.mipmap.attend_icon_direction_down, 10f, 6f)
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

    @SuppressLint("SetTextI18n")
    private fun updateYearMonthView() {
        tv_year_and_month.text = "${year}年${month}月"
    }

    private fun defaultData() {
        updateRecycleView()
    }

    private fun updateRecycleView() {
    }
}

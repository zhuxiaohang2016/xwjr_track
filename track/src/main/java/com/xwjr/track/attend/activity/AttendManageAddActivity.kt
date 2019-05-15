package com.xwjr.track.attend.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xwjr.track.R
import com.xwjr.track.attend.extension.*
import kotlinx.android.synthetic.main.activity_attend_manage_add.*
import kotlinx.android.synthetic.main.attend_title.*

class AttendManageAddActivity : AppCompatActivity() {

    private var attendTimes = TWICE //考勤打卡次数

    companion object {
        const val TWICE = 2
        const val FOUR_TIMES = 4
        const val ATTEND_MAP = 1024
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend_manage_add)

        init()
        setListener()
        defaultData()
    }

    private fun init() {
        tv_title.text = "新建考勤"
        tv_right.visibility = View.GONE

        tv_sign_time_afternoon_on.initDrawableRightView(R.mipmap.attend_icon_direction_down_gray, 12f, 7f)
        tv_sign_time_afternoon_off.initDrawableRightView(R.mipmap.attend_icon_direction_down_gray, 12f, 7f)
        tv_sign_time_morning_on.initDrawableRightView(R.mipmap.attend_icon_direction_down_gray, 12f, 7f)
        tv_sign_time_morning_off.initDrawableRightView(R.mipmap.attend_icon_direction_down_gray, 12f, 7f)
        tv_sign_time_week.initDrawableRightView(R.mipmap.attend_icon_direction_right_gray, 7f, 12f)
        tv_attend_worker.initDrawableRightView(R.mipmap.attend_icon_direction_right_gray, 7f, 12f)
        tv_attend_location_add.initDrawableLeftView(R.mipmap.attend_icon_add, 13f, 13f)
        tv_attend_location_des.initDrawableLeftView(R.mipmap.attend_icon_location_gray2, 10f, 13f)
        tv_attend_offset.initDrawableRightView(R.mipmap.attend_icon_direction_right_gray, 7f, 12f)
        cb_attend_auto_sign.initDrawableLeftView(R.drawable.attend_switch, 64f, 24f)
        tv_attend_view_permission.initDrawableRightView(R.mipmap.attend_icon_direction_right_gray, 7f, 12f)

    }

    /**
     * 考勤次数对应view显示
     */
    private fun updateAttendTimesView() {
        when (attendTimes) {
            TWICE -> {
                cl_sign_time_afternoon.visibility = View.GONE
                tv_sign_time_morning_des.text = "上班时间"
            }
            FOUR_TIMES -> {
                cl_sign_time_afternoon.visibility = View.VISIBLE
                tv_sign_time_morning_des.text = "上午上班时间"
            }
        }
    }

    @SuppressLint("SetTextI18n")

    private fun setListener() {
        iv_back.setOnClickListener { finish() }

        cl_sign_time_morning.setOnClickListener {
            showTimeRangeSelect { hourStart, secondStart, hourEnd, secondEnd ->
                tv_sign_time_morning_on.text = "$hourStart:$secondStart"
                tv_sign_time_morning_off.text = "$hourEnd:$secondEnd"
            }
        }
        cl_sign_time_afternoon.setOnClickListener {
            showTimeRangeSelect { hourStart, secondStart, hourEnd, secondEnd ->
                tv_sign_time_afternoon_on.text = "$hourStart:$secondStart"
                tv_sign_time_afternoon_off.text = "$hourEnd:$secondEnd"
            }
        }

        cb_switch_attend_times.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                attendTimes = FOUR_TIMES
                cb_switch_attend_times.text = "切换到一天两次打卡"
            } else {
                attendTimes = TWICE
                cb_switch_attend_times.text = "切换到一天四次打卡"
            }
            updateAttendTimesView()
        }

        tv_sign_time_week.setOnClickListener {
            showWeekSelect {
                var value = ""
                it.forEachIndexed { index, s ->
                    if (index == 0) {
                        value = s
                    } else {
                        value += ",$s"
                    }
                }
                tv_sign_time_week.text = value
            }
        }

        tv_attend_offset.setOnClickListener {
            showWheelSelect(arrayListOf("100米", "200米", "300米")) { selectData ->
                tv_attend_offset.text = selectData
            }
        }

        tv_attend_location_add.setOnClickListener {
            startActivityForResult(Intent(this@AttendManageAddActivity,AttendMapActivity::class.java),ATTEND_MAP)
        }
    }

    private fun defaultData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){

        }
    }
}

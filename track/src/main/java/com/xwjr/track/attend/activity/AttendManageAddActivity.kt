package com.xwjr.track.attend.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.xwjr.track.R
import com.xwjr.track.attend.extension.initDrawableLeftView
import com.xwjr.track.attend.extension.initDrawableRightView
import kotlinx.android.synthetic.main.activity_attend_manage_add.*
import kotlinx.android.synthetic.main.attend_title.*

class AttendManageAddActivity : AppCompatActivity() {

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

    private fun setListener() {
        iv_back.setOnClickListener { finish() }

    }

    private fun defaultData() {
    }
}

package com.xwjr.track.attend.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.xwjr.track.R
import com.xwjr.track.TrackConfig
import com.xwjr.track.attend.bean.AttendManageListBean
import com.xwjr.track.attend.bean.BaseBean
import com.xwjr.track.attend.extension.*
import com.xwjr.track.attend.net.AttendUrlConfig
import com.xwjr.track.attend.net.TrackHttpContract
import com.xwjr.track.attend.net.TrackHttpPresenter
import kotlinx.android.synthetic.main.activity_attend_manage_add.*
import kotlinx.android.synthetic.main.attend_title.*

/**
 * 考勤管理新增页面
 */
class AttendManageAddActivity : AttendBaseActivity(), TrackHttpContract {

    private var attendTimes = TWICE //考勤打卡次数
    private var attendManageDetail: AttendManageListBean.DataBean? = null
    private var operateType = ""
    private var trackHttpPresenter: TrackHttpPresenter? = null

    companion object {
        const val TWICE = 2
        const val FOUR_TIMES = 4
        const val ATTEND_MAP = 1024
        const val ATTEND_STAFF = 1025
        const val ATTEND_STAFF_PERMISSION = 1026
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend_manage_add)

        init()
        setListener()
        defaultData()
    }

    private fun init() {
        tv_right.visibility = View.GONE
        trackHttpPresenter = TrackHttpPresenter(this, this)
        operateType = intent.getStringExtra("operateType")
        when (operateType) {
            "EDIT" -> {
                tv_title.text = "编辑考勤"
                attendManageDetail = Gson().fromJson(intent.getStringExtra("attendManageDetail"), AttendManageListBean.DataBean::class.java)
                updateView()
            }
            "ADD" -> {
                tv_title.text = "新建考勤"
                attendManageDetail = AttendManageListBean.DataBean()
                attendManageDetail?.ruleType = "0"
                attendManageDetail?.distance = "100"
            }
        }


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

    @SuppressLint("SetTextI18n")
    private fun setListener() {
        iv_back.setOnClickListener { finish() }

        cl_sign_time_morning.setOnClickListener {
            showTimeRangeSelect(tv_sign_time_morning_on.text.toString(),
                    tv_sign_time_morning_off.text.toString())
            { hourStart, secondStart, hourEnd, secondEnd ->
                tv_sign_time_morning_on.text = "$hourStart:$secondStart"
                tv_sign_time_morning_off.text = "$hourEnd:$secondEnd"
            }
        }
        cl_sign_time_afternoon.setOnClickListener {
            showTimeRangeSelect(tv_sign_time_afternoon_on.text.toString(),
                    tv_sign_time_afternoon_off.text.toString())
            { hourStart, secondStart, hourEnd, secondEnd ->
                tv_sign_time_afternoon_on.text = "$hourStart:$secondStart"
                tv_sign_time_afternoon_off.text = "$hourEnd:$secondEnd"
            }
        }

        cb_switch_attend_times.setOnCheckedChangeListener { _, isChecked ->
            attendTimes = if (isChecked) {
                FOUR_TIMES
            } else {
                TWICE
            }
            updateAttendTimesView()
        }

        tv_sign_time_week.setOnClickListener {
            showWeekSelect(tv_sign_time_week.text.toString()) {
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

        tv_attend_view_permission.setOnClickListener {
            val intent = intent.setClass(this@AttendManageAddActivity, StaffSearchActivity::class.java)
            intent.putExtra("selectedStaff", attendManageDetail?.queryUserId)
            startActivityForResult(intent, ATTEND_STAFF_PERMISSION)
        }

        tv_attend_worker.setOnClickListener {
            val intent = intent.setClass(this@AttendManageAddActivity, StaffSearchActivity::class.java)
            intent.putExtra("selectedStaff", attendManageDetail?.checkinUserId)
            startActivityForResult(intent, ATTEND_STAFF)
        }

        tv_attend_location_add.setOnClickListener {
            val intent = intent.setClass(this@AttendManageAddActivity, AttendMapActivity::class.java)
            intent.putExtra("latitude", TrackConfig.getLatitude())
            intent.putExtra("longitude", TrackConfig.getLongitude())
            intent.putExtra("circleRadius", tv_attend_offset.text.substring(0, tv_attend_offset.text.length - 1))
            startActivityForResult(intent, ATTEND_MAP)
        }
        tv_attend_location_des.setOnClickListener {
            val intent = intent.setClass(this@AttendManageAddActivity, AttendMapActivity::class.java)
            intent.putExtra("latitude", attendManageDetail?.latitude.toString())
            intent.putExtra("longitude", attendManageDetail?.longitude.toString())
            intent.putExtra("circleRadius", tv_attend_offset.text.substring(0, tv_attend_offset.text.length - 1))
            startActivityForResult(intent, ATTEND_MAP)
        }
        tv_add.setOnClickListener {
            if (checkData()) {
                when (operateType) {
                    "ADD" -> {
                        trackHttpPresenter?.addAttendManage(
                                intent.getStringExtra("token"),
                                if (attendTimes == TWICE) "0" else "1",
                                tv_sign_time_week.text.toString(),
                                attendManageDetail?.checkinUserId.toString(),
                                attendManageDetail?.checkinUserName.toString(),
                                tv_attend_offset.text.substring(0, tv_attend_offset.text.length - 1),
                                getSecond(tv_sign_time_morning_on.text.toString()),
                                getSecond(tv_sign_time_morning_off.text.toString()),
                                if (attendTimes == FOUR_TIMES) getSecond(tv_sign_time_afternoon_on.text.toString()) else "",
                                if (attendTimes == FOUR_TIMES) getSecond(tv_sign_time_afternoon_off.text.toString()) else "",
                                attendManageDetail?.longitude.toString(),
                                attendManageDetail?.latitude.toString(),
                                attendManageDetail?.location.toString(),
                                attendManageDetail?.queryUserId,
                                attendManageDetail?.queryUserName
                        )
                    }
                    "EDIT" -> {
                        trackHttpPresenter?.updateAttendManage(
                                intent.getStringExtra("token"),
                                if (attendTimes == TWICE) "0" else "1",
                                tv_sign_time_week.text.toString(),
                                attendManageDetail?.checkinUserId.toString(),
                                attendManageDetail?.checkinUserName.toString(),
                                tv_attend_offset.text.substring(0, tv_attend_offset.text.length - 1),
                                getSecond(tv_sign_time_morning_on.text.toString()),
                                getSecond(tv_sign_time_morning_off.text.toString()),
                                if (attendTimes == FOUR_TIMES) getSecond(tv_sign_time_afternoon_on.text.toString()) else "",
                                if (attendTimes == FOUR_TIMES) getSecond(tv_sign_time_afternoon_off.text.toString()) else "",
                                attendManageDetail?.longitude.toString(),
                                attendManageDetail?.latitude.toString(),
                                attendManageDetail?.location.toString(),
                                attendManageDetail?.queryUserId,
                                attendManageDetail?.queryUserName
                        )
                    }
                }
            }
        }
    }

    private fun defaultData() {
    }


    /**
     * 编辑情况更新view显示
     */
    @SuppressLint("SetTextI18n")
    private fun updateView() {
        if (attendManageDetail?.ruleType == "0") {
            attendTimes = TWICE
            cb_switch_attend_times.isChecked = false
            if (attendManageDetail?.onWork.isNotNullOrEmpty())
                tv_sign_time_morning_on.text = secondToTime(attendManageDetail?.onWork!!.toInt())
            if (attendManageDetail?.offWork.isNotNullOrEmpty())
                tv_sign_time_morning_off.text = secondToTime(attendManageDetail?.offWork!!.toInt())
        } else if (attendManageDetail?.ruleType == "1") {
            attendTimes = FOUR_TIMES
            cb_switch_attend_times.isChecked = true
            if (attendManageDetail?.onWork.isNotNullOrEmpty())
                tv_sign_time_morning_on.text = secondToTime(attendManageDetail?.onWork!!.toInt())
            if (attendManageDetail?.offWork.isNotNullOrEmpty())
                tv_sign_time_morning_off.text = secondToTime(attendManageDetail?.offWork!!.toInt())
            if (attendManageDetail?.onWorkTwo.isNotNullOrEmpty())
                tv_sign_time_afternoon_on.text = secondToTime(attendManageDetail?.onWorkTwo!!.toInt())
            if (attendManageDetail?.offWorkTwo.isNotNullOrEmpty())
                tv_sign_time_afternoon_off.text = secondToTime(attendManageDetail?.offWorkTwo!!.toInt())
        } else {
            showToast("未获取到打卡类型")
        }
        tv_sign_time_week.text = attendManageDetail?.workDays
        updateAttendStaffView()
        updateLocationView()
        tv_attend_offset.text = attendManageDetail?.distance + "米"
        updateQueryPermissionView()
        updateAttendTimesView()
    }

    private fun updateQueryPermissionView() {
        tv_attend_view_permission.text = attendManageDetail?.queryUserName
    }

    private fun updateAttendStaffView() {
        tv_attend_worker.text = attendManageDetail?.checkinUserName
    }

    private fun updateLocationView() {
        tv_attend_location_add.visibility = View.GONE
        tv_attend_location_des.visibility = View.VISIBLE
        tv_attend_location_des.text = attendManageDetail?.location
    }

    /**
     * 考勤次数对应view显示
     */
    private fun updateAttendTimesView() {
        when (attendTimes) {
            TWICE -> {
                cl_sign_time_afternoon.visibility = View.GONE
                tv_sign_time_morning_des.text = "上班时间"
                cb_switch_attend_times.text = "切换到一天四次打卡"
            }
            FOUR_TIMES -> {
                cl_sign_time_afternoon.visibility = View.VISIBLE
                tv_sign_time_morning_des.text = "上午上班时间"
                cb_switch_attend_times.text = "切换到一天两次打卡"
            }
        }
    }

    private fun checkData(): Boolean {
        when (attendTimes) {
            TWICE -> {
                if (tv_sign_time_morning_on.text.isNullOrEmpty() || tv_sign_time_morning_off.text.isNullOrEmpty()) {
                    showToast("请选择上班时间")
                    return false
                }
            }
            FOUR_TIMES -> {
                if (tv_sign_time_morning_on.text.isNullOrEmpty() || tv_sign_time_morning_off.text.isNullOrEmpty()) {
                    showToast("请选择上午上班时间")
                    return false
                }
                if (tv_sign_time_afternoon_on.text.isNullOrEmpty() || tv_sign_time_afternoon_off.text.isNullOrEmpty()) {
                    showToast("请选择下午上班时间")
                    return false
                }
            }
        }
        if (tv_sign_time_week.text.isNullOrEmpty()) {
            showToast("请选择上班日期")
            return false
        }
        if (tv_attend_worker.text.isNullOrEmpty()) {
            showToast("请选择考勤人员")
            return false
        }
        if (tv_attend_location_des.text.isNullOrEmpty()) {
            showToast("请选择考勤地点")
            return false
        }
        return true
    }

    private fun getSecond(time: String): String {
        var second = 0
        time.split(":").forEachIndexed { index, s ->
            if (index == 0) {
                second += s.toInt() * 3600
            } else if (index == 1) {
                second += s.toInt() * 60
            }
        }
        return second.toString()
    }


    override fun statusBack(i: String, data: Any) {
        try {
            when (i) {
                AttendUrlConfig.updateAttendManage, AttendUrlConfig.addAttendManage -> {
                    logI("更新考勤计划数据，开始解析")
                    data as String
                    val baseBean = (Gson().fromJson(data, BaseBean::class.java))
                    if (baseBean != null && baseBean.checkCodeIfErrorShow()) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ATTEND_MAP -> {
                    attendManageDetail?.longitude = data?.getStringExtra("longitude")
                    attendManageDetail?.latitude = data?.getStringExtra("latitude")
                    attendManageDetail?.location = data?.getStringExtra("location")
                    updateLocationView()
                }
                ATTEND_STAFF -> {
                    attendManageDetail?.checkinUserId = data?.getStringExtra("selectedStaff")
                    attendManageDetail?.checkinUserName = data?.getStringExtra("selectedStaffName")
                    updateAttendStaffView()
                }
                ATTEND_STAFF_PERMISSION -> {
                    attendManageDetail?.queryUserId = data?.getStringExtra("selectedStaff")
                    attendManageDetail?.queryUserName = data?.getStringExtra("selectedStaffName")
                    updateQueryPermissionView()
                }
            }
        }
    }
}

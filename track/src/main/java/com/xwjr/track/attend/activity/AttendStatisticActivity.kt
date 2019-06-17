package com.xwjr.track.attend.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.xwjr.track.R
import com.xwjr.track.attend.adapter.AttendManageListAdapter
import com.xwjr.track.attend.adapter.AttendStatisticListAdapter
import com.xwjr.track.attend.adapter.AttendStatisticListener
import com.xwjr.track.attend.bean.AttendRecordListBean
import com.xwjr.track.attend.bean.AttendStatisticListBean
import com.xwjr.track.attend.extension.initDrawableRightView
import com.xwjr.track.attend.extension.logI
import com.xwjr.track.attend.extension.showAbnormalAttendRecord
import com.xwjr.track.attend.net.AttendUrlConfig
import com.xwjr.track.attend.net.TrackHttpContract
import com.xwjr.track.attend.net.TrackHttpPresenter
import kotlinx.android.synthetic.main.activity_attend_statistic.*
import kotlinx.android.synthetic.main.attend_title.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 考勤统计页面
 */
class AttendStatisticActivity : AttendBaseActivity(), TrackHttpContract {

    private var year = 0
    private var month = 0
    private var attendStatisticListBean: AttendStatisticListBean? = null
    private var trackHttpPresenter: TrackHttpPresenter? = null

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
        trackHttpPresenter = TrackHttpPresenter(this, this)
        getTimeData()
        updateYearMonthView()
        tv_filter.initDrawableRightView(R.mipmap.attend_icon_direction_down, 10f, 6f)
        queryData()
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
    }

    private fun queryData() {
        trackHttpPresenter?.attendStatistic(intent.getStringExtra("token"), "$year-${formatDate(month)}")
    }


    private fun queryAbnormalAttendRecord(userId: String, time: String) {
        trackHttpPresenter?.abnormalAttendRecord(intent.getStringExtra("token"), userId, time)
    }

    private fun formatDate(data: Int?): String {
        if (data in 1..9) {
            return "0$data"
        }
        return data.toString()
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
        tv_year_and_month.text = "${year}年${formatDate(month)}月"
        queryData()
    }

    private fun updateRecycleView() {
        rv_attend_record_list.layoutManager = LinearLayoutManager(this)
        rv_attend_record_list.adapter = AttendStatisticListAdapter(this, attendStatisticListBean!!.data!!.count!!).apply {
            this.attendStatisticListener = object : AttendStatisticListener {
                override fun click(position: Int) {
                    queryAbnormalAttendRecord(attendStatisticListBean!!.data!!.count!![position].userId.toString(), "$year-${formatDate(month)}")
                }
            }
        }
    }

    override fun statusBack(i: String, data: Any) {
        try {
            when (i) {

                AttendUrlConfig.attendStatistic -> {
                    logI("获取考勤统计成功，开始解析")
                    data as String
                    attendStatisticListBean = (Gson().fromJson(data, AttendStatisticListBean::class.java))
                    if (attendStatisticListBean != null && attendStatisticListBean!!.checkCodeIfErrorShow() && attendStatisticListBean!!.data != null && attendStatisticListBean!!.data!!.count != null) {
                        updateRecycleView()
                    }
                }
                AttendUrlConfig.abnormalAttendRecord -> {
                    logI("获取考勤记录成功，开始解析")
                    data as String
                    val abnormalAttendRecordListBean = (Gson().fromJson(data, AttendRecordListBean::class.java))
                    if (abnormalAttendRecordListBean != null && abnormalAttendRecordListBean!!.checkCodeIfErrorShow() && abnormalAttendRecordListBean.data != null && abnormalAttendRecordListBean.data?.records != null && abnormalAttendRecordListBean.data?.records!!.size > 0) {
                        //展示view
                        showAbnormalAttendRecord(abnormalAttendRecordListBean)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

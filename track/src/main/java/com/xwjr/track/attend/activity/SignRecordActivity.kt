package com.xwjr.track.attend.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.xwjr.track.LogUtils
import com.xwjr.track.R
import com.xwjr.track.attend.adapter.SignRecordListAdapter
import com.xwjr.track.attend.bean.AttendRecordListBean
import com.xwjr.track.attend.bean.SignListBean
import com.xwjr.track.attend.extension.convertNull
import com.xwjr.track.attend.extension.logI
import com.xwjr.track.attend.extension.showAbnormalAttendRecord
import com.xwjr.track.attend.net.AttendUrlConfig
import com.xwjr.track.attend.net.TrackHttpContract
import com.xwjr.track.attend.net.TrackHttpPresenter
import kotlinx.android.synthetic.main.activity_sign_record.*
import kotlinx.android.synthetic.main.attend_title.*
import java.util.HashMap

/**
 * 考勤记录页面
 */
class SignRecordActivity : AttendBaseActivity(), TrackHttpContract {

    private val signList: MutableList<SignListBean> = arrayListOf()
    private var trackHttpPresenter: TrackHttpPresenter? = null
    private var attendRecordListBean: AttendRecordListBean? = null
    private var abnormalAttendRecordListBean: AttendRecordListBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_record)
        init()
        setListener()
        defaultData()
    }


    private fun init() {
        tv_title.text = "考勤记录"
        tv_right.visibility = View.GONE
        trackHttpPresenter = TrackHttpPresenter(this, this)
    }


    /**
     * 考勤记录
     */
    private fun queryAttendRecord(time: String) {
        trackHttpPresenter?.attendRecord(intent.getStringExtra("token"), intent.getStringExtra("loginName"), time)
    }

    private fun queryAbnormalAttendRecord(time: String) {
        trackHttpPresenter?.abnormalAttendRecord(intent.getStringExtra("token"), intent.getStringExtra("loginName"), time)
    }

    @SuppressLint("SetTextI18n")
    private fun setListener() {
        iv_back.setOnClickListener { finish() }

        iv_last_month.setOnClickListener {
            calendarView.scrollToPre()
        }
        iv_next_month.setOnClickListener {
            calendarView.scrollToNext()
        }

        calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener {
            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                tv_year_and_month.text = "${calendar?.year}年${formatDate(calendar?.month)}月"
                tv_date.text = "${calendar?.year}-${formatDate(calendar?.month)}-${formatDate(calendar?.day)}"
                LogUtils.i("isClick $isClick  选择了${calendar?.year}  ${formatDate(calendar?.month)} ${formatDate(calendar?.day)}")
                queryAttendRecord(tv_date.text.toString())
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {
            }
        })

        calendarView.setOnMonthChangeListener { year, month ->
            tv_year_and_month.text = "${year}年${formatDate(month)}月"
        }

        iv_attend_record.setOnClickListener {
            queryAbnormalAttendRecord(tv_year_and_month.text.substring(0, 4) + "-" + tv_year_and_month.text.substring(5, 7))
        }
    }


    private fun defaultData() {
        //日历默认选择当天
        calendarView.scrollToCalendar(calendarView.curYear, calendarView.curMonth, calendarView.curDay)
//        addTag()
    }

    private fun formatDate(data: Int?): String {
        if (data in 1..9) {
            return "0$data"
        }
        return data.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun updateStatisticView() {
        tv_late.text = "${attendRecordListBean!!.data!!.count!!.lateCount}"
        tv_early.text = "${attendRecordListBean!!.data!!.count!!.leaveEarlyCount}"
        tv_forget.text = "${attendRecordListBean!!.data!!.count!!.uncheckin}"
        tv_check_out.text = "${attendRecordListBean!!.data!!.count!!.checkinOutside}"
    }

    private fun updateRecycleView() {
        signList.clear()
        attendRecordListBean!!.data!!.records!!.forEach {
            val signStatus = if (it.checkinOutside == "inside") it.checkinResultDisplay.convertNull() else "外勤"
            signList.add(SignListBean(it.checkinResult.convertNull(), it.locationDetail.toString(), it.checkinTime.toString().substring(11, 16), signStatus))
        }
        rv_sign_record_list.layoutManager = LinearLayoutManager(this)
        rv_sign_record_list.adapter = SignRecordListAdapter(this, signList)
    }

    private fun addTag() {
        val year = calendarView.curYear
        val month = calendarView.curMonth

        val map = HashMap<String, Calendar>()
        map[getSchemeCalendar(year, month, 1, "OK").toString()] = getSchemeCalendar(year, month, 1, "OK")
        map[getSchemeCalendar(year, month, 2, "OK").toString()] = getSchemeCalendar(year, month, 2, "OK")
        map[getSchemeCalendar(year, month, 3, "OK").toString()] = getSchemeCalendar(year, month, 3, "OK")
        map[getSchemeCalendar(year, month, 4, "OK").toString()] = getSchemeCalendar(year, month, 4, "OK")
        map[getSchemeCalendar(year, month, 5, "OK").toString()] = getSchemeCalendar(year, month, 5, "OK")
        map[getSchemeCalendar(year, month, 6, "OK").toString()] = getSchemeCalendar(year, month, 6, "OK")
        map[getSchemeCalendar(year, month, 7, "OK").toString()] = getSchemeCalendar(year, month, 7, "OK")
        map[getSchemeCalendar(year, month, 8, "忘").toString()] = getSchemeCalendar(year, month, 8, "忘")
        map[getSchemeCalendar(year, month, 9, "OK").toString()] = getSchemeCalendar(year, month, 9, "OK")
        map[getSchemeCalendar(year, month, 10, "OK").toString()] = getSchemeCalendar(year, month, 10, "OK")
        map[getSchemeCalendar(year, month, 11, "OK").toString()] = getSchemeCalendar(year, month, 11, "OK")
        map[getSchemeCalendar(year, month, 12, "OK").toString()] = getSchemeCalendar(year, month, 12, "OK")
        map[getSchemeCalendar(year, month, 13, "OK").toString()] = getSchemeCalendar(year, month, 13, "OK")
        map[getSchemeCalendar(year, month, 14, "OK").toString()] = getSchemeCalendar(year, month, 14, "OK")
        map[getSchemeCalendar(year, month, 15, "OK").toString()] = getSchemeCalendar(year, month, 15, "OK")
        map[getSchemeCalendar(year, month, 16, "退").toString()] = getSchemeCalendar(year, month, 16, "退")
        map[getSchemeCalendar(year, month, 18, "OK").toString()] = getSchemeCalendar(year, month, 18, "OK")
        map[getSchemeCalendar(year, month, 19, "OK").toString()] = getSchemeCalendar(year, month, 19, "OK")
        map[getSchemeCalendar(year, month, 20, "OK").toString()] = getSchemeCalendar(year, month, 20, "OK")
        map[getSchemeCalendar(year, month, 21, "OK").toString()] = getSchemeCalendar(year, month, 21, "OK")
        map[getSchemeCalendar(year, month, 22, "OK").toString()] = getSchemeCalendar(year, month, 22, "OK")
        map[getSchemeCalendar(year, month, 23, "OK").toString()] = getSchemeCalendar(year, month, 23, "OK")
        map[getSchemeCalendar(year, month, 24, "OK").toString()] = getSchemeCalendar(year, month, 24, "OK")
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        calendarView.setSchemeDate(map)
    }

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, text: String): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.scheme = text
        return calendar
    }

    override fun statusBack(i: String, data: Any) {
        try {
            when (i) {

                AttendUrlConfig.attendRecord -> {
                    logI("获取考勤记录成功，开始解析")
                    data as String
                    attendRecordListBean = (Gson().fromJson(data, AttendRecordListBean::class.java))
                    if (attendRecordListBean != null && attendRecordListBean!!.checkCodeIfErrorShow() && attendRecordListBean!!.data != null && attendRecordListBean!!.data?.records != null && attendRecordListBean!!.data?.count != null) {
                        updateStatisticView()
                        updateRecycleView()
                    }
                }
                AttendUrlConfig.abnormalAttendRecord -> {
                    logI("获取考勤记录成功，开始解析")
                    data as String
                    abnormalAttendRecordListBean = (Gson().fromJson(data, AttendRecordListBean::class.java))
                    if (abnormalAttendRecordListBean != null && abnormalAttendRecordListBean!!.checkCodeIfErrorShow() && abnormalAttendRecordListBean!!.data != null && abnormalAttendRecordListBean!!.data?.records != null && abnormalAttendRecordListBean!!.data?.records!!.size > 0) {
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

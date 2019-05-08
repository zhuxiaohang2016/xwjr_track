package com.xwjr.track.attend.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.xwjr.track.LogUtils
import com.xwjr.track.R
import com.xwjr.track.attend.adapter.SignListAdapter
import com.xwjr.track.attend.bean.SignListBean
import kotlinx.android.synthetic.main.activity_attend_record.*
import kotlinx.android.synthetic.main.attend_title.*
import java.util.HashMap

/**
 * 考勤记录页面
 */
class SignRecordActivity : AppCompatActivity() {

    private val signList: MutableList<SignListBean> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend_record)
        init()
        setListener()
        defaultData()
    }


    private fun init() {
        tv_title.text = "考勤记录"
        tv_right.visibility = View.GONE

        signList.add(SignListBean("上午签到", "卑微竟然看我大家", "08:00", true))
        signList.add(SignListBean("上午签退", "是的法定防守打法", "12:00", false))
        signList.add(SignListBean("下午签到", "ad发送到发送到发送到发送到发多发的发送到发顺丰拉开", "13:46", false))
        signList.add(SignListBean("下午签退", "卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家", "18:05", true))
        signList.add(SignListBean("下午签退", "卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家", "18:05", true))
        signList.add(SignListBean("下午签退", "卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家", "18:05", true))
        signList.add(SignListBean("下午签退", "卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家", "18:05", true))
        signList.add(SignListBean("下午签退", "卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家", "18:05", true))
        signList.add(SignListBean("未签到", "卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家卑微竟然看我大家", "18:05", true))
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
                tv_year_and_month.text = "${calendar?.year}年${calendar?.month}月"
                LogUtils.i("isClick $isClick  选择了${calendar?.year}  ${calendar?.month} ${calendar?.day}")
            }

            override fun onCalendarOutOfRange(calendar: Calendar?) {
            }
        })

        calendarView.setOnMonthChangeListener { year, month ->
            tv_year_and_month.text = "${year}年${month}月"
        }
    }

    private fun defaultData() {
        //日历默认选择当天
        calendarView.scrollToCalendar(calendarView.curYear, calendarView.curMonth, calendarView.curDay)
        //
        addTag()
        updateRecycleView()
    }

    private fun updateRecycleView() {
        rv_sign_record_list.layoutManager = LinearLayoutManager(this)
        rv_sign_record_list.adapter = SignListAdapter(this, signList)
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

}

package com.xwjr.track.attend.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xwjr.track.LogUtils
import com.xwjr.track.R
import com.xwjr.track.attend.adapter.AttendManageListAdapter
import com.xwjr.track.attend.bean.AttendManageListBean
import com.xwjr.track.attend.extension.showTip
import kotlinx.android.synthetic.main.activity_attend_manage.*
import kotlinx.android.synthetic.main.attend_title.*

/**
 * 考勤管理页面
 */
class AttendManageActivity : AppCompatActivity() {

    private val attendManageList: MutableList<AttendManageListBean> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend_manage)
        init()
        setListener()
        defaultData()
    }

    private fun init() {
        tv_title.text = "考勤管理"
        tv_right.visibility = View.GONE

        attendManageList.add(AttendManageListBean("8:00", "", "18:00", "", "24234fssadffjlljkadsljkfalfjalksf"))
        attendManageList.add(AttendManageListBean("8:00", "34:34", "18:00", "", "爱的发圣诞节啦收到了发牢骚的减肥垃圾的法拉水电费骄傲了的减肥的爱的发圣诞节啦收到了发牢骚的减肥垃圾的法拉水电费骄傲了的减肥的爱的发圣诞节啦收到了发牢骚的减肥垃圾的法拉水电费骄傲了的减肥的爱的发圣诞节啦收到了发牢骚的减肥垃圾的法拉水电费骄傲了的减肥的"))
        attendManageList.add(AttendManageListBean("8:00", "16:09", "18:00", "87:09", "打算发打发斯蒂芬"))
        attendManageList.add(AttendManageListBean("8:00", "04:82", "18:00", "33:98", "阿斯顿发卡机劳动法拉克的看法拉开大口径拉了卡决胜巅峰"))
        attendManageList.add(AttendManageListBean("8:00", "", "18:00", "23:00", "阿道夫逻辑吖大立科技发酵"))
    }

    private fun setListener() {
        iv_back.setOnClickListener { finish() }
    }

    private fun defaultData() {
        updateRecycleView()
    }

    private fun updateRecycleView() {
        rv_attend_manage_list.layoutManager = LinearLayoutManager(this)
        rv_attend_manage_list.adapter = AttendManageListAdapter(this, attendManageList).apply {
            this.manageListener = object : AttendManageListAdapter.ManageListener {
                override fun add() {
                    attendManageList.add(AttendManageListBean("8:00", "34:34", "18:00", "", "爱的发圣诞节啦收到了发牢骚的减肥垃圾的法拉水电费骄傲了的减肥的爱的发圣诞节啦收到了发牢骚的减肥垃圾的法拉水电费骄傲了的减肥的爱的发圣诞节啦收到了发牢骚的减肥垃圾的法拉水电费骄傲了的减肥的爱的发圣诞节啦收到了发牢骚的减肥垃圾的法拉水电费骄傲了的减肥的"))
                    rv_attend_manage_list.adapter.notifyDataSetChanged()
                    LogUtils.i("新增考勤")
                }

                override fun delete(position: Int) {
                    showTip("请确认是否删除", "确定") {
                        attendManageList.removeAt(position)
                        rv_attend_manage_list.adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}

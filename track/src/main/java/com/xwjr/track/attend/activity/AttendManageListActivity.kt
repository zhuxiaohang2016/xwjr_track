package com.xwjr.track.attend.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.xwjr.track.LogUtils
import com.xwjr.track.R
import com.xwjr.track.attend.adapter.AttendManageListAdapter
import com.xwjr.track.attend.bean.AttendManageListBean
import com.xwjr.track.attend.extension.err
import com.xwjr.track.attend.extension.logI
import com.xwjr.track.attend.extension.showTip
import com.xwjr.track.attend.net.AttendUrlConfig
import com.xwjr.track.attend.net.TrackHttpContract
import com.xwjr.track.attend.net.TrackHttpPresenter
import kotlinx.android.synthetic.main.activity_attend_manage_list.*
import kotlinx.android.synthetic.main.attend_title.*

/**
 * 考勤管理列表页面
 */
class AttendManageListActivity : AppCompatActivity(), TrackHttpContract {


    private var attendManageData: AttendManageListBean? = null
    private val attendManageDataList: MutableList<AttendManageListBean.DataBean> = arrayListOf()
    private var trackHttpPresenter: TrackHttpPresenter? = null

    companion object {
        const val ADD_ATTEND = 1024
        const val EDIT_ATTEND = 1025
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend_manage_list)
        init()
        setListener()
        defaultData()
    }

    private fun init() {
        tv_title.text = "考勤管理"
        tv_right.visibility = View.GONE
        trackHttpPresenter = TrackHttpPresenter(this, this)
        queryData()
    }

    private fun queryData() {
        trackHttpPresenter?.queryAttendManageList(intent.getStringExtra("token"))
    }

    private fun setListener() {
        iv_back.setOnClickListener { finish() }
    }

    private fun defaultData() {
    }

    private fun updateRecycleView() {
        rv_attend_manage_list.layoutManager = LinearLayoutManager(this)
        rv_attend_manage_list.adapter = AttendManageListAdapter(this, attendManageDataList).apply {
            this.manageListener = object : AttendManageListAdapter.ManageListener {
                override fun itemClick(position: Int) {
                    val intent = intent.setClass(this@AttendManageListActivity, AttendManageAddActivity::class.java)
                    intent.putExtra("operateType", "EDIT")
                    intent.putExtra("attendManageDetail", Gson().toJson(attendManageDataList[position]))
                    startActivityForResult(intent, EDIT_ATTEND)
                }

                override fun add() {
                    val intent = intent.setClass(this@AttendManageListActivity, AttendManageAddActivity::class.java)
                    intent.putExtra("operateType", "ADD")
                    startActivityForResult(intent, ADD_ATTEND)
                }

                override fun delete(position: Int) {
//                    showTip("请确认是否删除", "确定") {
//                        attendManageDataList.removeAt(position)
//                        rv_attend_manage_list.adapter.notifyDataSetChanged()
//                    }
                }
            }
        }
    }

    override fun statusBack(i: String, data: Any) {
        try {
            when (i) {
                AttendUrlConfig.queryAttendManageList -> {
                    logI("获取考勤计划数据成功，开始解析")
                    data as String
                    attendManageData = (Gson().fromJson(data, AttendManageListBean::class.java))
                    if (attendManageData != null && attendManageData!!.checkCodeIfErrorShow() && attendManageData!!.data != null) {
                        attendManageDataList.clear()
                        attendManageDataList.add(attendManageData!!.data!!)
                        updateRecycleView()
                    } else {
                        attendManageDataList.clear()
                        updateRecycleView()
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
                ADD_ATTEND, EDIT_ATTEND -> {
                    logI("新增/编辑考勤返回")
                    queryData()
                }
            }
        }
    }
}

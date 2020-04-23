package com.xwjr.track.attend.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.gson.Gson
import com.xwjr.track.R
import com.xwjr.track.attend.adapter.StaffSearchAdapter
import com.xwjr.track.attend.adapter.StaffSearchListener
import com.xwjr.track.attend.bean.StaffListBean
import com.xwjr.track.attend.extension.initDrawableRightView
import com.xwjr.track.attend.extension.logI
import com.xwjr.track.attend.net.AttendUrlConfig
import com.xwjr.track.attend.net.TrackHttpContract
import com.xwjr.track.attend.net.TrackHttpPresenter
import kotlinx.android.synthetic.main.activity_staff_search.*
import kotlinx.android.synthetic.main.attend_title.*

/**
 * 员工搜索列表
 */
class StaffSearchActivity : AttendBaseActivity(), TrackHttpContract {

    private var trackHttpPresenter: TrackHttpPresenter? = null
    private var staffList: MutableList<StaffListBean.DataBean> = arrayListOf()
    private var searchList: MutableList<StaffListBean.DataBean> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_search)

        init()
        setListener()
        defaultData()
    }

    private fun init() {
        tv_right.visibility = View.GONE
        tv_title.text = "选择人员"
        et_search.initDrawableRightView(R.mipmap.attend_icon_search_gray, 16f, 16f)
        cb_all.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)
        trackHttpPresenter = TrackHttpPresenter(this, this)
        queryData()
    }

    private fun setListener() {
        iv_back.setOnClickListener { finish() }

        tv_sure.setOnClickListener {
            val filterStaffList = staffList.filter {
                it.isSelect == true
            }
            var selectedStaff = ""
            var selectedStaffName = ""
            filterStaffList.forEachIndexed { index, item ->
                if (index == 0) {
                    selectedStaff = item.userName.toString()
                    selectedStaffName = item.realName.toString()
                } else {
                    selectedStaff += "," + item.userName.toString()
                    selectedStaffName += "," + item.realName.toString()
                }
            }
            val intent = Intent()
            intent.putExtra("selectedStaff", selectedStaff)
            intent.putExtra("selectedStaffName", selectedStaffName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        cb_all.setOnClickListener {
            if (!cb_all.isChecked) {
                searchList.forEach {
                    it.isSelect = false
                }
            } else {
                searchList.forEach {
                    it.isSelect = true
                }
            }
            updateRecycleView()
        }

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchList.clear()
                if (s.isNullOrEmpty()) {
                    searchList.addAll(staffList)
                } else {
                    staffList.forEach {
                        if (it.realName.toString().contains(s.toString())) {
                            searchList.add(it)
                        }
                    }
                }
                updateSelectAllView()
                updateRecycleView()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun updateSelectAllView() {
        cb_all.isChecked = true
        searchList.forEach {
            if (it.isSelect == null || it.isSelect == false) {
                cb_all.isChecked = false
            }
        }
        if (searchList.size == 0) {
            cb_all.visibility = View.GONE
        } else {
            cb_all.visibility = View.VISIBLE
        }
    }

    private fun defaultData() {

    }

    private fun queryData() {
        trackHttpPresenter?.queryStaffList(intent.getStringExtra("token"), intent.getStringExtra("bankId"))
    }


    private fun updateRecycleView() {
        rv_staff_list.layoutManager = LinearLayoutManager(this)
        rv_staff_list.adapter = StaffSearchAdapter(this, searchList).apply {
            this.staffSearchListener = object : StaffSearchListener {
                override fun selectStatus(selectAll: Boolean) {
                    cb_all.isChecked = selectAll
                }
            }
        }
    }


    override fun statusBack(i: String, data: Any) {
        try {
            when (i) {
                AttendUrlConfig.queryStaffList -> {
                    logI("获取员工列表，开始解析")
                    data as String
                    val staffData = (Gson().fromJson(data, StaffListBean::class.java))
                    if (staffData != null && staffData.checkCodeIfErrorShow() && staffData.data != null) {
                        staffList.clear()
                        staffList.addAll(staffData.data!!)
                        val selectedStaff = intent.getStringExtra("selectedStaff")
                        selectedStaff?.split(",")?.forEach {
                            staffList.forEach { item ->
                                if (it == item.userName) {
                                    item.isSelect = true
                                }
                            }
                        }
                        searchList.clear()
                        searchList.addAll(staffList)
                        updateSelectAllView()
                        updateRecycleView()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

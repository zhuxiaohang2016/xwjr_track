package com.xwjr.track.attend.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.xwjr.track.LogUtils

import com.xwjr.track.R
import com.xwjr.track.attend.adapter.SignListAdapter
import com.xwjr.track.attend.bean.SignListBean
import com.xwjr.track.attend.extension.initCheckBoxView
import com.xwjr.track.attend.extension.showSignSuccess
import kotlinx.android.synthetic.main.activity_attend.*
import kotlinx.android.synthetic.main.attend_title.*

/**
 * 考勤签到页面
 */
class SignActivity : AppCompatActivity() {

    private val signList: MutableList<SignListBean> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend)

        init()
        setListener()
        defaultData()
    }

    private fun init() {
        tv_title.text = "考勤"
        tv_right.text = "考勤记录"

        initCheckBoxView(cb_auto_sign)


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

    private fun setListener() {
        iv_back.setOnClickListener { finish() }
        tv_right.setOnClickListener {
            startActivity(Intent(this@SignActivity, SignRecordActivity::class.java))
        }
        iv_sign.setOnClickListener {
            showSignSuccess()
        }
        iv_manage.setOnClickListener {
            startActivity(Intent(this@SignActivity, AttendManageActivity::class.java))
        }
        iv_statistic.setOnClickListener {
            LogUtils.i("统计")
        }
        cb_auto_sign.setOnCheckedChangeListener { _, isChecked ->
            val intent = Intent("attend.broadcast.AutoSignReceiver")
            intent.putExtra("autoSignStatus", isChecked)
            sendBroadcast(intent)
        }
    }

    private fun defaultData() {
        updateRecycleView()
    }

    private fun updateRecycleView() {
        rv_sign_list.layoutManager = LinearLayoutManager(this)
        rv_sign_list.adapter = SignListAdapter(this, signList)
    }


}

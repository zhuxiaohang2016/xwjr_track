package com.xwjr.track.attend.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson

import com.xwjr.track.R
import com.xwjr.track.TrackConfig
import com.xwjr.track.attend.adapter.SignListAdapter
import com.xwjr.track.attend.adapter.SignListener
import com.xwjr.track.attend.bean.AttendManageListBean
import com.xwjr.track.attend.bean.AttendRecordListBean
import com.xwjr.track.attend.bean.BaseBean
import com.xwjr.track.attend.bean.SignListBean
import com.xwjr.track.attend.extension.*
import com.xwjr.track.attend.net.AttendUrlConfig
import com.xwjr.track.attend.net.TrackHttpContract
import com.xwjr.track.attend.net.TrackHttpPresenter
import kotlinx.android.synthetic.main.activity_sign.*
import kotlinx.android.synthetic.main.attend_title.*
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.content.ContextCompat
import com.xwjr.track.TrackLocationData
import com.xwjr.track.attend.util.PermissionPageUtils


/**
 * 考勤签到页面
 */
@SuppressLint("HandlerLeak")
class SignActivity : AttendBaseActivity(), TrackHttpContract {

    private val signList: MutableList<SignListBean> = arrayListOf()
    private var userRole = ""//用户角色
    private var trackHttpPresenter: TrackHttpPresenter? = null
    private var attendManageDetail: AttendManageListBean.DataBean? = null
    private var isInAttendRange = false
    private var timer: Timer? = Timer()
    var myHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> {
                    logI("更新定位地址")
                    updateLocationView()
                    updateAttendRange()
                }
            }
            super.handleMessage(msg)
        }
    }

    companion object {
        const val LOCATION_INTERVAL = 10000L
        const val ZJL = "ZGSZJL"
        const val MANAGE_ATTEND = 1024
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_sign)

            init()
            setListener()
            defaultData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun init() {
        tv_title.text = "考勤"
        tv_right.text = "考勤记录"
        userRole = intent.getStringExtra("userRole")
        trackHttpPresenter = TrackHttpPresenter(this, this)


        if (ZJL in userRole.split(",")) {
            iv_manage.visibility = View.VISIBLE
        } else {
            iv_manage.visibility = View.GONE
        }
        getDateData()
        updateLocationView()
        cb_auto_sign.initDrawableLeftView(R.drawable.attend_switch, 64f, 24f)
        queryData()
    }

    private fun setListener() {
        iv_back.setOnClickListener { finish() }
        tv_right.setOnClickListener {
            intent.setClass(this@SignActivity, SignRecordActivity::class.java)
            startActivity(intent)
        }
        iv_sign.setOnClickListener {
            showSignSuccess()
        }
        iv_manage.setOnClickListener {
            intent.setClass(this@SignActivity, AttendManageListActivity::class.java)
            startActivityForResult(intent, MANAGE_ATTEND)
        }
        iv_statistic.setOnClickListener {
            intent.setClass(this@SignActivity, AttendStatisticActivity::class.java)
            startActivity(intent)
        }
        cb_auto_sign.setOnCheckedChangeListener { _, isChecked ->
            val intent = Intent("attend.broadcast.AutoSignReceiver")
            intent.putExtra("autoSignStatus", isChecked)
            sendBroadcast(intent)
        }
        cl_location.setOnClickListener {
            if (!isLocServiceEnable(this@SignActivity) || !checkLocationPermission()) {
                return@setOnClickListener
            }
            TrackLocationData.refreshLocation(this)
            laterDeal(500) {
                myHandler.sendEmptyMessage(0)
            }
        }
        tv_check_out.setOnClickListener {
            if (!isLocServiceEnable(this@SignActivity) || !checkLocationPermission()) {
                return@setOnClickListener
            }
            showTip("请确认是否外勤签到？", "确定") {
                attendSign("outside", "")
            }
        }
    }

    private fun defaultData() {
        intervalDeal()
    }

    /**
     * 更新定位之地
     */
    private fun updateLocationView() {
        tv_location.text = TrackConfig.getAddress()
    }

    /**
     * 更新定位描述
     */
    private fun updateLocationDesView() {
        when (isInAttendRange) {
            true -> {
                tv_location_des.text = "已进入考勤范围"
                tv_location_des.setTextColor(Color.parseColor("#ffc793"))
                iv_location_yellow.setImageResource(R.mipmap.attend_icon_location_yellow)
            }
            false -> {
                tv_location_des.text = "未进入考勤范围(点击刷新)"
                tv_location_des.setTextColor(Color.parseColor("#ff5050"))
                iv_location_yellow.setImageResource(R.mipmap.attend_icon_refresh)
            }
        }
    }

    /**
     * 查询考勤计划
     */
    private fun queryData() {
        trackHttpPresenter?.queryAttendManageList(intent.getStringExtra("token"))
    }

    /**
     * 考勤
     */
    private fun attendSign(type: String, checkInType: String) {
        if (isLocServiceEnable(this) && checkLocationPermission()) {
            if (TrackConfig.getLongitude().isNullOrEmpty() || TrackConfig.getLatitude().isNullOrEmpty()) {
                showToast("暂未获取到位置信息，请重试")
                return
            }
            trackHttpPresenter?.attendSign(intent.getStringExtra("token"), type, getDeviceId(this), TrackConfig.getLongitude(), TrackConfig.getLatitude(), TrackConfig.getAddress(), checkInType)
        }
    }

    /**
     * 考勤记录
     */
    private fun queryAttendRecord() {
        trackHttpPresenter?.attendRecord(intent.getStringExtra("token"), intent.getStringExtra("loginName"), getDateData())
    }


    /**
     * 初始化日期布局
     */
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun getDateData(): String {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val str1 = arrayOf("", "日", "一", "二", "三", "四", "五", "六")
        tv_week.text = "星期" + str1[calendar.get(Calendar.DAY_OF_WEEK)]
        tv_date.text = simpleDateFormat.format(Date())
        return tv_date.text.toString()
    }

    /**
     * 获取时:分
     */
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun getTimeData(): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        return simpleDateFormat.format(Date())
    }

    /**
     * 更新考勤范围
     */
    private fun updateAttendRange() {
        try {
            val distance = getDistance(TrackConfig.getLatitude(), TrackConfig.getLongitude(), attendManageDetail?.latitude.toString(), attendManageDetail?.longitude.toString())
            isInAttendRange = distance.toDouble() <= attendManageDetail?.distance.toString().toDouble()
            updateLocationDesView()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 更新考勤列表
     */
    @SuppressLint("SetTextI18n")
    private fun updateView() {
        signList.clear()
        if (attendManageDetail?.ruleType == "0") {
            tv_sign_time.text = secondToTime(attendManageDetail?.onWork!!.toInt()) + "-" + secondToTime(attendManageDetail?.offWork!!.toInt())
            signList.add(SignListBean("上午签到", "", secondToTime(attendManageDetail?.onWork!!.toInt()), ""))
            signList.add(SignListBean("下午签退", "", secondToTime(attendManageDetail?.offWork!!.toInt()), ""))
        } else if (attendManageDetail?.ruleType == "1") {
            tv_sign_time.text = secondToTime(attendManageDetail?.onWork!!.toInt()) +
                    "-" +
                    secondToTime(attendManageDetail?.offWork!!.toInt()) +
                    "  " +
                    secondToTime(attendManageDetail?.onWorkTwo!!.toInt()) +
                    "-" +
                    secondToTime(attendManageDetail?.offWorkTwo!!.toInt())
            signList.add(SignListBean("上午签到", "", secondToTime(attendManageDetail?.onWork!!.toInt()), ""))
            signList.add(SignListBean("上午签退", "", secondToTime(attendManageDetail?.offWork!!.toInt()), ""))
            signList.add(SignListBean("下午签到", "", secondToTime(attendManageDetail?.onWorkTwo!!.toInt()), ""))
            signList.add(SignListBean("下午签退", "", secondToTime(attendManageDetail?.offWorkTwo!!.toInt()), ""))
        }
        updateRecycleView()
        updateAttendRange()
    }

    /**
     * 更新考勤列表
     */
    private fun updateRecycleView() {
        rv_sign_list.layoutManager = LinearLayoutManager(this)
        rv_sign_list.adapter = SignListAdapter(this, signList).apply {
            this.signListener = object : SignListener {
                override fun sign(time: String) {
                    if (!isLocServiceEnable(this@SignActivity) || !checkLocationPermission()) {
                        return
                    }
                    if (!isInAttendRange) {
                        showTip("您未进入考勤范围，如需签到，请用外勤签到", "我知道了")
                        return
                    }
                    when (time) {
                        "上午签到" -> {
                            if (getSecond(getTimeData()).toInt() > attendManageDetail?.onWork!!.toInt()) {
                                showTip("您已经迟到，是否继续打卡？", "继续") {
                                    attendSign("inside", "onWork")
                                }
                            } else {
                                attendSign("inside", "onWork")
                            }
                        }
                        "上午签退" -> {
                            if (getSecond(getTimeData()).toInt() < attendManageDetail?.offWork!!.toInt()) {
                                showTip("还未到下班时间，是否继续打卡？", "继续") {
                                    attendSign("inside", "offWork")
                                }
                            } else {
                                attendSign("inside", "offWork")
                            }
                        }
                        "下午签到" -> {
                            if (getSecond(getTimeData()).toInt() > attendManageDetail?.onWorkTwo!!.toInt()) {
                                showTip("您已经迟到，是否继续打卡？", "继续") {
                                    attendSign("inside", "onWorkTwo")
                                }
                            } else {
                                attendSign("inside", "onWorkTwo")
                            }
                        }
                        "下午签退" -> {
                            if (attendManageDetail?.ruleType == "0") {
                                if (getSecond(getTimeData()).toInt() < attendManageDetail?.offWork!!.toInt()) {
                                    showTip("还未到下班时间，是否继续打卡？", "继续") {
                                        attendSign("inside", "offWork")
                                    }
                                } else {
                                    attendSign("inside", "offWork")
                                }
                            } else if (attendManageDetail?.ruleType == "1") {
                                if (getSecond(getTimeData()).toInt() < attendManageDetail?.offWorkTwo!!.toInt()) {
                                    showTip("还未到下班时间，是否继续打卡？", "继续") {
                                        attendSign("inside", "offWorkTwo")
                                    }
                                } else {
                                    attendSign("inside", "offWorkTwo")
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 获取考勤地点 与 考勤计划点 距离
     */
    private fun getDistance(lat1Str: String, lng1Str: String, lat2Str: String, lng2Str: String): String {
        try {
            if (lat1Str.isNotNullOrEmpty() && lng1Str.isNotNullOrEmpty() && lat2Str.isNotNullOrEmpty() && lng2Str.isNotNullOrEmpty()) {
                val lat1 = java.lang.Double.parseDouble(lat1Str)
                val lng1 = java.lang.Double.parseDouble(lng1Str)
                val lat2 = java.lang.Double.parseDouble(lat2Str)
                val lng2 = java.lang.Double.parseDouble(lng2Str)

                val radLat1 = rad(lat1)
                val radLat2 = rad(lat2)
                val difference = radLat1 - radLat2
                val mDifference = rad(lng1) - rad(lng2)
                var distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2.0) + (Math.cos(radLat1) * Math.cos(radLat2)
                        * Math.pow(Math.sin(mDifference / 2), 2.0))))
                distance *= 6371.392
                distance = (Math.round(distance * 10000000) / 10000).toDouble()
                var distanceStr = distance.toString() + ""
                distanceStr = distanceStr.substring(0, distanceStr.indexOf("."))

                return distanceStr
            } else {
                return "10000000"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "10000000"
        }

    }

    private fun rad(d: Double): Double {
        return d * Math.PI / 180.0
    }

    /**
     * 根据时间获取秒
     */
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

    /**
     *设备id
     */
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        var id = ""
        try {
            id = android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)

//            val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                id = manager.deviceId
//                return id
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return id
    }

    /**
     * 定位服务是否可用
     */
    private fun isLocServiceEnable(context: Context): Boolean {
        return try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (gps || network) {
                true
            } else {
                showToast("请先开启定位服务")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    /**
     * 校验权限
     */
    private fun checkLocationPermission(): Boolean {
        return try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                showTip("您尚未开启定位权限，无法获取您的位置", "去开启") {
                    PermissionPageUtils(this).jumpPermissionPage()
                }
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }

    }

    private fun intervalDeal() {
        timer?.schedule(object : TimerTask() {
            override fun run() {
                myHandler.sendEmptyMessage(0)
            }
        }, 0, LOCATION_INTERVAL)
    }

    /**
     * 更新打卡数据
     */
    private fun updateSignListData() {

    }


    @SuppressLint("DefaultLocale")
    override fun statusBack(i: String, data: Any) {
        try {
            when (i) {
                AttendUrlConfig.queryAttendManageList -> {
                    logI("获取考勤计划数据成功，开始解析")
                    data as String
                    val attendManageData = (Gson().fromJson(data, AttendManageListBean::class.java))
                    if (attendManageData != null && attendManageData.checkCodeIfErrorShow() && attendManageData.data != null) {
                        attendManageDetail = attendManageData.data
                        if (intent.getStringExtra("loginName").toUpperCase() in attendManageDetail!!.checkinUserId.toString().toUpperCase().split(",")) {
                            //如果考勤计划人员里有当前登录人，则显示考勤列表，否则不处理
                            updateView()
                            queryAttendRecord()
                            rv_sign_list.visibility = View.VISIBLE
                            tv_check_out.visibility = View.VISIBLE
                            cl_attend_null.visibility = View.GONE
                        } else {
                            rv_sign_list.visibility = View.GONE
                            tv_check_out.visibility = View.GONE
                            cl_attend_null.visibility = View.VISIBLE
                        }
                        if (intent.getStringExtra("loginName").toUpperCase() in attendManageDetail!!.queryUserId.toString().toUpperCase().split(",")) {
                            //如果有查看统计人员里有当前登录人，则显示统计按钮，否则不显示
                            iv_statistic.visibility = View.VISIBLE
                        } else {
                            iv_statistic.visibility = View.GONE
                        }
                    } else {
                        cl_attend_null.visibility = View.VISIBLE
                    }
                }
                AttendUrlConfig.attendSign -> {
                    logI("考勤成功，开始解析")
                    data as String
                    val baseBean = (Gson().fromJson(data, BaseBean::class.java))
                    if (baseBean != null && baseBean.checkCodeIfErrorShow()) {
                        //获取最新的考勤记录
                        showSignSuccess()
                        queryAttendRecord()
                    }
                }
                AttendUrlConfig.attendRecord -> {
                    logI("获取考勤记录成功，开始解析")
                    data as String
                    val attendRecordListBean = (Gson().fromJson(data, AttendRecordListBean::class.java))
                    if (attendRecordListBean != null && attendRecordListBean.checkCodeIfErrorShow() && attendRecordListBean.data != null && attendRecordListBean.data?.records != null && attendRecordListBean.data?.count != null) {
                        val anotherData: MutableList<SignListBean> = arrayListOf()
                        anotherData.addAll(signList)
                        signList.clear()
                        if (attendManageDetail?.ruleType == "0") {
                            signList.addAll(anotherData.subList(0, 2))
                        } else if (attendManageDetail?.ruleType == "1") {
                            signList.addAll(anotherData.subList(0, 4))
                        }
                        attendRecordListBean.data?.records?.forEach {
                            if (attendManageDetail?.ruleType == "0") {
                                //考勤规则 2次打卡
                                if (it.checkinType.isNotNullOrEmpty()) {
                                    when (it.checkinType) {
                                        "onWork" -> {
                                            signList[0].signStatus = it.checkinResultDisplay.convertNull()
                                            signList[0].location = it.locationDetail.convertNull()
                                        }
                                        "offWork" -> {
                                            signList[1].signStatus = it.checkinResultDisplay.convertNull()
                                            signList[1].location = it.locationDetail.convertNull()
                                        }
                                    }
                                }
                            } else if (attendManageDetail?.ruleType == "1") {
                                //考勤规则 4次打卡
                                if (it.checkinType.isNotNullOrEmpty()) {
                                    when (it.checkinType) {
                                        "onWork" -> {
                                            signList[0].signStatus = it.checkinResultDisplay.convertNull()
                                            signList[0].location = it.locationDetail.convertNull()
                                        }
                                        "offWork" -> {
                                            signList[1].signStatus = it.checkinResultDisplay.convertNull()
                                            signList[1].location = it.locationDetail.convertNull()
                                        }
                                        "onWorkTwo" -> {
                                            signList[2].signStatus = it.checkinResultDisplay.convertNull()
                                            signList[2].location = it.locationDetail.convertNull()
                                        }
                                        "offWorkTwo" -> {
                                            signList[3].signStatus = it.checkinResultDisplay.convertNull()
                                            signList[3].location = it.locationDetail.convertNull()
                                        }
                                    }
                                }
                            }
                            if (it.checkinOutside == "outside") {
                                //外勤
                                signList.add(SignListBean("外勤签到", it.locationDetail.convertNull(), it.checkinTime.convertNull().substring(10, 16), ""))
                            }
                        }
                        updateSignListData()
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
        when (requestCode) {
            MANAGE_ATTEND -> {
                queryData()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        TrackLocationData.setLocationInterval(LOCATION_INTERVAL.toInt())
    }

    override fun onPause() {
        super.onPause()
        TrackLocationData.recoverLocationInterval()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }
}

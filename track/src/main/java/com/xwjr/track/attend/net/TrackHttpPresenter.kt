package com.xwjr.track.attend.net

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.xwjr.track.attend.extension.err
import com.xwjr.track.attend.extension.logI
import com.xwjr.track.attend.extension.showToast
import okhttp3.*
import java.io.IOException

class TrackHttpPresenter(private val context: Context, private val contract: TrackHttpContract) {

    /**
     * 统一的handler数据发送
     */
    private fun sendData(url: String, data: String) {
        Handler(Looper.getMainLooper()).post {
            contract.statusBack(url, data)
        }
    }

    /**
     * 统一的handler数据发送
     */
    private fun sendBundle(data: Bundle) {
        Handler(Looper.getMainLooper()).post {
            contract.statusBack(data.getString("url")!!, data)
        }
    }

    /**
     * 查询考勤计划数据
     */
    fun queryAttendManageList(token: String) {
        logI("开始获取考勤计划数据...")
        val url = AttendUrlConfig.queryAttendManageList
        logI("请求URL:$url")
        OkHttpClient().newCall(Request.Builder()
                .addHeader("Cookie", "ccat=$token")
                .url(url)
                .get()
                .build()
        ).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                logI("发生异常：查询考勤计划数据失败 $url")
                showToast("发生异常：查询考勤计划数据失败")
                e.printStackTrace()
                sendData(url.err(), "")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body()?.string().toString()
                logI("查询考勤计划返回数据 $url ：\n$data")
                sendData(url, data)
            }
        })
    }

    /**
     * 新增考勤计划
     */
    fun addAttendManage(token: String,
                        ruleType: String,
                        workDays: String,
                        checkinUserId: String,
                        checkinUserName: String,
                        distance: String,
                        onWork: String,
                        offWork: String,
                        onWorkTwo: String? = null,
                        offWorkTwo: String? = null,
                        longitude: String,
                        latitude: String,
                        location: String,
                        queryUserId: String? = null,
                        queryUserName: String? = null) {
        logI("开始新增考勤计划...")
        val url = AttendUrlConfig.addAttendManage
        logI("请求URL:$url")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestBody.addFormDataPart("ruleType",
                ruleType)
        requestBody.addFormDataPart("workDays", workDays)
        requestBody.addFormDataPart("checkinUserId", checkinUserId)
        requestBody.addFormDataPart("checkinUserName", checkinUserName)
        requestBody.addFormDataPart("distance", distance)
        requestBody.addFormDataPart("onWork", onWork)
        requestBody.addFormDataPart("offWork", offWork)
        if (onWorkTwo != null)
            requestBody.addFormDataPart("onWorkTwo", onWorkTwo)
        if (offWorkTwo != null)
            requestBody.addFormDataPart("offWorkTwo", offWorkTwo)
        requestBody.addFormDataPart("longitude", longitude)
        requestBody.addFormDataPart("latitude", latitude)
        requestBody.addFormDataPart("location", location)
        if (queryUserId != null)
            requestBody.addFormDataPart("queryUserId", queryUserId)
        if (queryUserName != null)
            requestBody.addFormDataPart("queryUserName", queryUserName)
        logI("请求参数：  ruleType:" + ruleType +
                "  workDays:" + workDays +
                "  checkinUserId:" + checkinUserId +
                "  distance:" + distance +
                "  onWork:" + onWork +
                "  offWork:" + offWork +
                "  onWorkTwo:" + onWorkTwo +
                "  offWorkTwo:" + offWorkTwo +
                "  longitude:" + longitude +
                "  latitude:" + latitude +
                "  location:" + location +
                "  queryUserId:" + queryUserId)
        OkHttpClient().newCall(Request.Builder()
                .addHeader("Cookie", "ccat=$token")
                .url(url)
                .post(requestBody.build())
                .build()
        ).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                logI("发生异常：新增考勤计划数据失败 $url")
                showToast("发生异常：新增考勤计划数据失败")
                e.printStackTrace()
                sendData(url.err(), "")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body()?.string().toString()
                logI("新增考勤计划返回数据 $url ：\n$data")
                sendData(url, data)
            }
        })
    }

    /**
     * 更新考勤计划
     */
    fun updateAttendManage(token: String,
                           ruleType: String,
                           workDays: String,
                           checkinUserId: String,
                           checkinUserName: String,
                           distance: String,
                           onWork: String,
                           offWork: String,
                           onWorkTwo: String? = null,
                           offWorkTwo: String? = null,
                           longitude: String,
                           latitude: String,
                           location: String,
                           queryUserId: String? = null,
                           queryUserName: String? = null) {
        logI("开始更新考勤计划...")
        val url = AttendUrlConfig.updateAttendManage
        logI("请求URL:$url")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestBody.addFormDataPart("ruleType",
                ruleType)
        requestBody.addFormDataPart("workDays", workDays)
        requestBody.addFormDataPart("checkinUserId", checkinUserId)
        requestBody.addFormDataPart("checkinUserName", checkinUserName)
        requestBody.addFormDataPart("distance", distance)
        requestBody.addFormDataPart("onWork", onWork)
        requestBody.addFormDataPart("offWork", offWork)
        if (onWorkTwo != null)
            requestBody.addFormDataPart("onWorkTwo", onWorkTwo)
        if (offWorkTwo != null)
            requestBody.addFormDataPart("offWorkTwo", offWorkTwo)
        requestBody.addFormDataPart("longitude", longitude)
        requestBody.addFormDataPart("latitude", latitude)
        requestBody.addFormDataPart("location", location)
        if (queryUserId != null)
            requestBody.addFormDataPart("queryUserId", queryUserId)
        if (queryUserName != null)
            requestBody.addFormDataPart("queryUserName", queryUserName)
        logI("请求参数：  ruleType:" + ruleType +
                "  workDays:" + workDays +
                "  checkinUserId:" + checkinUserId +
                "  distance:" + distance +
                "  onWork:" + onWork +
                "  offWork:" + offWork +
                "  onWorkTwo:" + onWorkTwo +
                "  offWorkTwo:" + offWorkTwo +
                "  longitude:" + longitude +
                "  latitude:" + latitude +
                "  location:" + location +
                "  queryUserId:" + queryUserId)
        OkHttpClient().newCall(Request.Builder()
                .addHeader("Cookie", "ccat=$token")
                .url(url)
                .post(requestBody.build())
                .build()
        ).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                logI("发生异常：更新考勤计划数据失败 $url")
                showToast("发生异常：更新考勤计划数据失败")
                e.printStackTrace()
                sendData(url.err(), "")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body()?.string().toString()
                logI("更新考勤计划返回数据 $url ：\n$data")
                sendData(url, data)
            }
        })
    }


    /**
     * 查询员工列表
     */
    fun queryStaffList(token: String,
                       bankId: String) {
        logI("开始查询员工列表...")
        val url = AttendUrlConfig.queryStaffList + "?bankId=$bankId&userState=0"
        logI("请求URL:$url")
        OkHttpClient().newCall(Request.Builder()
                .addHeader("Cookie", "ccat=$token")
                .url(url)
                .get()
                .build()
        ).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                logI("发生异常：查询员工列表数据失败 $url")
                showToast("发生异常：查询员工列表数据失败")
                e.printStackTrace()
                sendData(url.err(), "")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body()?.string().toString()
                logI("查询员工列表返回数据 $url ：\n$data")
                sendData(AttendUrlConfig.queryStaffList, data)
            }
        })
    }


    /**
     * 考勤
     */
    fun attendSign(token: String,
                   checkinOutside: String,
                   equipmentId: String,
                   longitude: String,
                   latitude: String,
                   locationDetail: String,
                   checkInType: String) {
        logI("开始考勤...")
        val url = AttendUrlConfig.attendSign
        logI("请求URL:$url")
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        requestBody.addFormDataPart("checkinOutside", checkinOutside)
        requestBody.addFormDataPart("equipmentId", equipmentId)
        requestBody.addFormDataPart("longitude", longitude)
        requestBody.addFormDataPart("latitude", latitude)
        requestBody.addFormDataPart("locationDetail", locationDetail)
        requestBody.addFormDataPart("checkinType", checkInType)
        logI("请求参数：  checkinOutside:" + checkinOutside +
                "  equipmentId:" + equipmentId +
                "  longitude:" + longitude +
                "  latitude:" + latitude +
                "  checkinType:" + checkInType +
                "  locationDetail:" + locationDetail)
        OkHttpClient().newCall(Request.Builder()
                .addHeader("Cookie", "ccat=$token")
                .url(url)
                .post(requestBody.build())
                .build()
        ).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                logI("发生异常：考勤失败 $url")
                showToast("发生异常：考勤失败")
                e.printStackTrace()
                sendData(url.err(), "")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body()?.string().toString()
                logI("考勤返回数据 $url ：\n$data")
                sendData(url, data)
            }
        })
    }


    /**
     * 查询考勤记录
     */
    fun attendRecord(token: String,
                     userId: String,
                     date: String) {
        logI("开始查询考勤记录...")
        val url = AttendUrlConfig.attendRecord + "?userId=$userId&date=$date"
        logI("请求URL:$url")
        OkHttpClient().newCall(Request.Builder()
                .addHeader("Cookie", "ccat=$token")
                .url(url)
                .get()
                .build()
        ).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                logI("发生异常：查询考勤记录失败 $url")
                showToast("发生异常：查询考勤记录失败")
                e.printStackTrace()
                sendData(url.err(), "")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body()?.string().toString()
                logI("查询考勤记录返回数据 $url ：\n$data")
                sendData(AttendUrlConfig.attendRecord, data)
            }
        })
    }


    /**
     * 查询考勤记录
     */
    fun abnormalAttendRecord(token: String,
                             userId: String,
                             month: String) {
        logI("开始查询考勤记录...")
        val url = AttendUrlConfig.abnormalAttendRecord + "?userId=$userId&month=$month"
        logI("请求URL:$url")
        OkHttpClient().newCall(Request.Builder()
                .addHeader("Cookie", "ccat=$token")
                .url(url)
                .get()
                .build()
        ).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                logI("发生异常：查询考勤记录失败 $url")
                showToast("发生异常：查询考勤记录失败")
                e.printStackTrace()
                sendData(url.err(), "")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body()?.string().toString()
                logI("查询考勤记录返回数据 $url ：\n$data")
                sendData(AttendUrlConfig.abnormalAttendRecord, data)
            }
        })
    }


    /**
     * 查询考勤统计
     */
    fun attendStatistic(token: String,
                        month: String) {
        logI("开始查询考勤统计...")
        val url = AttendUrlConfig.attendStatistic + "?month=$month"
        logI("请求URL:$url")
        OkHttpClient().newCall(Request.Builder()
                .addHeader("Cookie", "ccat=$token")
                .url(url)
                .get()
                .build()
        ).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                logI("发生异常：查询考勤统计失败 $url")
                showToast("发生异常：查询考勤统计失败")
                e.printStackTrace()
                sendData(url.err(), "")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body()?.string().toString()
                logI("查询考勤统计返回数据 $url ：\n$data")
                sendData(AttendUrlConfig.attendStatistic, data)
            }
        })
    }


}

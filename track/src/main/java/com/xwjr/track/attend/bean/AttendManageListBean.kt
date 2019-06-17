package com.xwjr.track.attend.bean

class AttendManageListBean : BaseBean() {
    var data: DataBean? = null

    class DataBean {
        var id: Int = 0
        var bankId: String? = null
        var ruleType: String? = null
        var checkinUserId: String? = null
        var checkinUserName: String? = null
        var distance: String? = null
        var workDays: String? = null
        var onWork: String? = null
        var offWork: String? = null
        var onWorkTwo: String? = null
        var offWorkTwo: String? = null
        var longitude: String? = null
        var latitude: String? = null
        var location: String? = null
        var autoCheckin: String? = null
        var queryUserId: String? = null
        var queryUserName: String? = null
        var createTime: String? = null
        var updateTime: String? = null
        var userId: String? = null
        var checkinUserIdList: MutableList<String>? = null
        var queryUserIdList: MutableList<String>? = null
    }
}

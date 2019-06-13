package com.xwjr.track.attend.net

object AttendUrlConfig {
    var domain = "http://p2psp.kfxfd.cn:9080"
    var queryAttendManageList = "$domain/bigbang/internal/checkin/rule/info/MYSELF"
    var updateAttendManage = "$domain/bigbang/internal/checkin/rule/update/MYSELF"
    var addAttendManage = "$domain/bigbang/internal/checkin/rule/save/MYSELF"
    var queryStaffList = "$domain/mloanApi/internal/user/userList/MYSELF"
    var attendSign = "$domain/bigbang/internal/checkin/save/MYSELF"
    var attendRecord = "$domain/bigbang/internal/checkin/info/MYSELF"
}

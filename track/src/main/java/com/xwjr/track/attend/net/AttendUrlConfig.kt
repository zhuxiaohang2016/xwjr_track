package com.xwjr.track.attend.net

import com.xwjr.track.TrackConfig

object AttendUrlConfig {
    var queryAttendManageList = "${TrackConfig.getAttendUrl()}bigbang/internal/checkin/rule/info/MYSELF"
    var updateAttendManage = "${TrackConfig.getAttendUrl()}bigbang/internal/checkin/rule/update/MYSELF"
    var addAttendManage = "${TrackConfig.getAttendUrl()}bigbang/internal/checkin/rule/save/MYSELF"
    var queryStaffList = "${TrackConfig.getAttendUrl()}mloanApi/internal/user/userList/MYSELF"
    var attendSign = "${TrackConfig.getAttendUrl()}bigbang/internal/checkin/save/MYSELF"
    var attendRecord = "${TrackConfig.getAttendUrl()}bigbang/internal/checkin/info/MYSELF"
    var abnormalAttendRecord = "${TrackConfig.getAttendUrl()}bigbang/internal/checkin/abnormalRecord/info/MYSELF"
    var attendStatistic = "${TrackConfig.getAttendUrl()}bigbang/internal/checkin/count/MYSELF"
}

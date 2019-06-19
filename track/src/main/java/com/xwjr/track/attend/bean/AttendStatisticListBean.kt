package com.xwjr.track.attend.bean

class AttendStatisticListBean : BaseBean() {

    var data: DataBean? = null

    class DataBean {
        var count: MutableList<CountBean>? = null

        class CountBean {

            var userId: String? = null
            var userName: String? = null
            var roleName:String?=null
            var lateCount: Int = 0
            var leaveEarlyCount: Int = 0
            var checkinOutside: Int = 0
            var checkinDays: Int = 0
            var uncheckin: Int = 0
        }
    }
}

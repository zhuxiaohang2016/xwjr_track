package com.xwjr.track.attend.bean

class AttendRecordListBean : BaseBean() {

    var data: DataBean? = null

    class DataBean {
        var count: CountBean? = null
        var records: MutableList<RecordsBean>? = null

        class CountBean {

            var userId: String? = null
            var userName: String? = null
            var lateCount: Int = 0
            var leaveEarlyCount: Int = 0
            var checkinOutside: Int = 0
            var checkinDays: Int = 0
            var uncheckin: Int = 0
        }

        class RecordsBean {

            var id: Int = 0
            var userId: String? = null
            var userName: String? = null
            var bankId: String? = null
            var checkinOutside: String? = null
            var checkinType: String? = null
            var checkinTime: String? = null
            var equipmentId: String? = null
            var longitude: String? = null
            var latitude: String? = null
            var locationDetail: String? = null
            var checkinResult: String? = null
            var checkinResultDisplay: String? = null
            var createTime: String? = null
        }
    }
}

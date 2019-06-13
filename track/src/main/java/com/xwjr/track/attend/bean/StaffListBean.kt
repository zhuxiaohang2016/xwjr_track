package com.xwjr.track.attend.bean

class StaffListBean : BaseBean() {

    var data: MutableList<DataBean>? = null

    class DataBean {
        var username: String? = null
        var realname: String? = null
        var phone: String? = null
        var locked: String? = null
        var empId: String? = null
        var roles: String? = null
        var roleNames: String? = null
        var bankId: String? = null
        var bankName: String? = null
        var bankCountyId: String? = null
        var isSelect: Boolean? = null
    }
}

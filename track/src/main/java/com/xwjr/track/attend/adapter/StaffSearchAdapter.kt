package com.xwjr.track.attend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import com.xwjr.track.R
import com.xwjr.track.attend.bean.StaffListBean
import com.xwjr.track.attend.extension.initDrawableRightView


open class StaffSearchAdapter(private val context: Context, private var dataList: MutableList<StaffListBean.DataBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var staffSearchListener: StaffSearchListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.attend_lv_staff_select, parent, false)
        return StaffSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        try {
            if (holder is StaffSearchViewHolder) {
                holder.cb.text = dataList[position].realName
                holder.cb.initDrawableRightView(R.drawable.attend_checkbox, 17f, 17f)

                holder.cl.setOnClickListener {
                    holder.cb.isChecked = !holder.cb.isChecked
                    dataList[position].isSelect = holder.cb.isChecked
                    staffSearchListener?.selectStatus(true)
                    dataList.forEach {
                        if (it.isSelect == null || it.isSelect == false){
                            staffSearchListener?.selectStatus(false)
                        }
                    }
                }
                holder.cb.isChecked =  dataList[position].isSelect != null &&  dataList[position].isSelect!!

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

interface StaffSearchListener {
    fun selectStatus(selectAll: Boolean)
}

class StaffSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cb = itemView.findViewById(R.id.cb) as CheckBox
    val cl = itemView.findViewById(R.id.cl) as ConstraintLayout
}



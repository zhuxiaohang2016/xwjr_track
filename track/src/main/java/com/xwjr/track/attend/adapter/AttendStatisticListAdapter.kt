package com.xwjr.track.attend.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.xwjr.track.R
import com.xwjr.track.attend.bean.AttendStatisticListBean


class AttendStatisticListAdapter(private val context: Context, private var dataList: MutableList<AttendStatisticListBean.DataBean.CountBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var attendStatisticListener: AttendStatisticListener? = null

    companion object {
        const val LAST_DATA = 1024
        const val NORMAL_DATA = 1025
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.attend_statistic_list, parent, false)
        return AttendStatisticListAdapterListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return NORMAL_DATA
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        try {
            if (holder is AttendStatisticListAdapterListViewHolder) {
                holder.tvName.text = dataList[position].userName
                holder.tvPost.text = dataList[position].roleName
                holder.tvAttendTimesValue.text = dataList[position].checkinDays.toString()
                holder.tvLateValue.text = dataList[position].lateCount.toString()
                holder.tvEarlyValue.text = dataList[position].leaveEarlyCount.toString()
                holder.tvForgetValue.text = dataList[position].uncheckin.toString()
                holder.tvOutValue.text = dataList[position].checkinOutside.toString()
                holder.clContent.setOnClickListener {
                    attendStatisticListener?.click(position)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

interface AttendStatisticListener {
    fun click(position: Int)
}

class AttendStatisticListAdapterListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName = itemView.findViewById(R.id.tv_name) as TextView
    val tvPost = itemView.findViewById(R.id.tv_post) as TextView
    val tvAttendTimesValue = itemView.findViewById(R.id.tv_attend_times_value) as TextView
    val tvLateValue = itemView.findViewById(R.id.tv_late_value) as TextView
    val tvEarlyValue = itemView.findViewById(R.id.tv_early_value) as TextView
    val tvForgetValue = itemView.findViewById(R.id.tv_forget_value) as TextView
    val tvOutValue = itemView.findViewById(R.id.tv_out_value) as TextView
    val clContent = itemView.findViewById(R.id.cl_content) as ConstraintLayout
}



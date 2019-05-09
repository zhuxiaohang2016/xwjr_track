package com.xwjr.track.attend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.xwjr.track.R
import com.xwjr.track.attend.bean.AttendManageListBean


class AttendManageListAdapter(private val context: Context, private var dataList: MutableList<AttendManageListBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var manageListener: ManageListener? = null

    companion object {
        const val ADD_BUTTON = 1024
        const val NORMAL_DATA = 1025
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADD_BUTTON -> {
                val view = LayoutInflater.from(context).inflate(R.layout.attend_add_button, parent, false)
                AttendManageAddButtonHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.attend_manage_list, parent, false)
                AttendManageHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == dataList.size) {
            return ADD_BUTTON
        }
        return NORMAL_DATA
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        try {
            if (holder is AttendManageHolder) {
                if (dataList[position].morningOffTime.isNullOrEmpty() || dataList[position].afternoonOffTime.isNullOrEmpty()) {
                    //2次签到
                    holder.groupSignTimeMorningOff.visibility = View.GONE
                    holder.groupSignTimeAfternoonOff.visibility = View.GONE
                    holder.tvSignTimeMorningOnDes.text = "上班"
                    holder.tvSignTimeAfternoonOnDes.text = "下班"
                } else {
                    //4次签到
                    holder.groupSignTimeMorningOff.visibility = View.VISIBLE
                    holder.groupSignTimeAfternoonOff.visibility = View.VISIBLE
                    holder.tvSignTimeMorningOnDes.text = "上午上班"
                    holder.tvSignTimeAfternoonOnDes.text = "下午上班"
                }

                if (!dataList[position].morningOnTime.isNullOrEmpty()) {
                    holder.tvSignTimeMorningOn.text = dataList[position].morningOnTime
                } else {
                    holder.tvSignTimeMorningOn.text = ""
                }

                if (!dataList[position].morningOffTime.isNullOrEmpty()) {
                    holder.tvSignTimeMorningOff.text = dataList[position].morningOffTime
                } else {
                    holder.tvSignTimeMorningOff.text = ""
                }

                if (!dataList[position].afternoonOnTime.isNullOrEmpty()) {
                    holder.tvSignTimeAfternoonOn.text = dataList[position].afternoonOnTime
                } else {
                    holder.tvSignTimeAfternoonOn.text = ""
                }

                if (!dataList[position].afternoonOffTime.isNullOrEmpty()) {
                    holder.tvSignTimeAfternoonOff.text = dataList[position].afternoonOffTime
                } else {
                    holder.tvSignTimeAfternoonOff.text = ""
                }

                if (!dataList[position].location.isNullOrEmpty()) {
                    holder.tvLocationDes.text = "签到地点:${dataList[position].location}"
                } else {
                    holder.tvLocationDes.text = ""
                }

                holder.ivDelete.setOnClickListener {
                    manageListener?.delete(position)
                }

                holder.clContent.setOnClickListener {
                    manageListener?.itemClick(position)
                }
            }

            if (holder is AttendManageAddButtonHolder) {
                holder.tvAdd.text = "新建考勤"
                holder.clAdd.setOnClickListener {
                    manageListener?.add()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface ManageListener {
        fun delete(position: Int)
        fun add()
        fun itemClick(position: Int)
    }

}

class AttendManageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvSignTimeMorningOn = itemView.findViewById(R.id.tv_sign_time_morning_on) as TextView
    val tvSignTimeMorningOnDes = itemView.findViewById(R.id.tv_sign_time_morning_on_des) as TextView
    //    val groupSignTimeMorningOn = itemView.findViewById(R.id.group_sign_time_morning_on) as Group
    val tvSignTimeMorningOff = itemView.findViewById(R.id.tv_sign_time_morning_off) as TextView
    val groupSignTimeMorningOff = itemView.findViewById(R.id.group_sign_time_morning_off) as Group
    val tvSignTimeAfternoonOn = itemView.findViewById(R.id.tv_sign_time_afternoon_on) as TextView
    val tvSignTimeAfternoonOnDes = itemView.findViewById(R.id.tv_sign_time_afternoon_on_des) as TextView
    //    val groupSignTimeAfternoonOn = itemView.findViewById(R.id.group_sign_time_afternoon_on) as Group
    val tvSignTimeAfternoonOff = itemView.findViewById(R.id.tv_sign_time_afternoon_off) as TextView
    val groupSignTimeAfternoonOff = itemView.findViewById(R.id.group_sign_time_afternoon_off) as Group
    val ivDelete = itemView.findViewById(R.id.iv_delete) as ImageView
    val tvLocationDes = itemView.findViewById(R.id.tv_location_des) as TextView
    val clContent = itemView.findViewById(R.id.cl_content) as ConstraintLayout
}

class AttendManageAddButtonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val clAdd = itemView.findViewById(R.id.cl_add) as ConstraintLayout
    val tvAdd = itemView.findViewById(R.id.tv_add) as TextView
}



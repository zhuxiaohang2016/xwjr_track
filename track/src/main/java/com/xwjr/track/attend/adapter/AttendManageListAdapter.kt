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
import com.xwjr.track.attend.extension.secondToTime
import com.xwjr.track.attend.extension.showToast


class AttendManageListAdapter(private val context: Context, private var dataList: MutableList<AttendManageListBean.DataBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        if (dataList.size == 0) {
            return 1
        }
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList.size == 0) {
            return ADD_BUTTON
        }
        return NORMAL_DATA
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        try {
            if (holder is AttendManageHolder) {
                when {
                    dataList[position].ruleType == "0" -> {
                        //2次签到
                        holder.groupSignTimeMorningOff.visibility = View.GONE
                        holder.groupSignTimeAfternoonOff.visibility = View.GONE
                        holder.tvSignTimeMorningOnDes.text = "上班"
                        holder.tvSignTimeAfternoonOnDes.text = "下班"
                    }
                    dataList[position].ruleType == "1" -> {
                        //4次签到
                        holder.groupSignTimeMorningOff.visibility = View.VISIBLE
                        holder.groupSignTimeAfternoonOff.visibility = View.VISIBLE
                        holder.tvSignTimeMorningOnDes.text = "上午上班"
                        holder.tvSignTimeAfternoonOnDes.text = "下午上班"
                    }
                    else -> {
                        showToast("未获取到打卡类型")
                        return
                    }
                }

                if (!dataList[position].onWork.isNullOrEmpty()) {
                    holder.tvSignTimeMorningOn.text = secondToTime(dataList[position].onWork!!.toInt())
                } else {
                    holder.tvSignTimeMorningOn.text = ""
                }

                if (!dataList[position].onWorkTwo.isNullOrEmpty()) {
                    holder.tvSignTimeMorningOff.text = secondToTime(dataList[position].onWorkTwo!!.toInt())
                } else {
                    holder.tvSignTimeMorningOff.text = ""
                }

                if (!dataList[position].offWork.isNullOrEmpty()) {
                    holder.tvSignTimeAfternoonOn.text = secondToTime(dataList[position].offWork!!.toInt())
                } else {
                    holder.tvSignTimeAfternoonOn.text = ""
                }

                if (!dataList[position].offWorkTwo.isNullOrEmpty()) {
                    holder.tvSignTimeAfternoonOff.text = secondToTime(dataList[position].offWorkTwo!!.toInt())
                } else {
                    holder.tvSignTimeAfternoonOff.text = ""
                }

                if (!dataList[position].location.isNullOrEmpty()) {
                    holder.tvLocationDes.text = "签到地点:${dataList[position].location}"
                } else {
                    holder.tvLocationDes.text = ""
                }

                holder.ivDelete.visibility = View.GONE
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



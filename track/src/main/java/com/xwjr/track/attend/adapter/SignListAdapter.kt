package com.xwjr.track.attend.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.xwjr.track.attend.bean.SignListBean
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.xwjr.track.R
import com.xwjr.track.attend.extension.isNotNullOrEmpty


class SignListAdapter(private val context: Context, private var dataList: MutableList<SignListBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var signListener: SignListener? = null

    companion object {
        const val LAST_DATA = 1024
        const val NORMAL_DATA = 1025
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.attend_sign_list, parent, false)
        return SignListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == dataList.size - 1) {
            return LAST_DATA
        }
        return NORMAL_DATA
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        try {
            if (holder is SignListViewHolder) {
                when (dataList[position].timeDes) {
                    "上午签到", "上午签退" -> {
                        holder.ivSignTime.setImageResource(R.mipmap.attend_icon_sign_time_day)
                    }
                    "下午签到", "下午签退" -> {
                        holder.ivSignTime.setImageResource(R.mipmap.attend_icon_sign_time_night)
                    }
                    else -> {
                        holder.ivSignTime.setImageResource(R.mipmap.attend_icon_sign_time_error)
                    }
                }
                holder.tvSignTimeDes.text = dataList[position].timeDes
                holder.tvSignType.text = dataList[position].signStatus
                holder.tvSignTimeDetail.text = dataList[position].timeDetail
                holder.tvLocationDes.text = dataList[position].location
                if (position == dataList.size - 1) {
                    holder.viewLineVertical.visibility = View.GONE
                }
                if (dataList[position].signStatus.isNotNullOrEmpty()) {
                    holder.tvSign.setBackgroundResource(R.drawable.attend_shape_gray_solid)
                    holder.tvSign.text = "考勤结束"
                    holder.tvSign.setTextColor(Color.parseColor("#b4b4b5"))
                } else {
                    holder.tvSign.setBackgroundResource(R.drawable.attend_shape_button_solid)
                    holder.tvSign.text = "考勤签到"
                    holder.tvSign.setTextColor(Color.parseColor("#ffffff"))
                    holder.tvSign.setOnClickListener {
                        signListener?.sign(dataList[position].timeDes)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

interface SignListener {
    fun sign(time: String)
}

class SignListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivSignTime = itemView.findViewById(R.id.iv_sign_time) as ImageView
    val tvSignTimeDes = itemView.findViewById(R.id.tv_sign_time_des) as TextView
    val tvSignType = itemView.findViewById(R.id.tv_sign_type) as TextView
    val tvSignTimeDetail = itemView.findViewById(R.id.tv_sign_time_detail) as TextView
    val tvLocationDes = itemView.findViewById(R.id.tv_location_des) as TextView
    val viewLineVertical = itemView.findViewById(R.id.view_line_vertical) as View
    val tvSign = itemView.findViewById(R.id.tv_sign) as TextView
}



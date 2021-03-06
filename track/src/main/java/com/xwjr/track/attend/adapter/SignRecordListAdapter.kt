package com.xwjr.track.attend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.xwjr.track.attend.bean.SignListBean
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.xwjr.track.R
import com.xwjr.track.attend.extension.convertNull


class SignRecordListAdapter(private val context: Context, private var dataList: MutableList<SignListBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val LAST_DATA = 1024
        const val NORMAL_DATA = 1025
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.attend_sign_record_list, parent, false)
        return SignRecordListViewHolder(view)
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        try {
            if (holder is SignRecordListViewHolder) {
                when(dataList[position].timeDes){
                    "normal"->{
                        holder.tvSignTimeDetail.setTextColor(Color.parseColor("#666666"))
                    }
                    else->{
                        holder.tvSignTimeDetail.setTextColor(Color.parseColor("#ff5050"))
                    }
                }
                holder.tvSignTimeDetail.text = dataList[position].timeDetail.convertNull() + "    " + dataList[position].signStatus.convertNull()
                holder.tvLocationDes.text = dataList[position].location
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class SignRecordListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvSignTimeDetail = itemView.findViewById(R.id.tv_sign_time_detail) as TextView
    val tvLocationDes = itemView.findViewById(R.id.tv_location_des) as TextView
}



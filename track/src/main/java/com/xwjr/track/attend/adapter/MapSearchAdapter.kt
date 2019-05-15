package com.xwjr.track.attend.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.amap.api.services.core.PoiItem
import com.xwjr.track.R


open class MapSearchAdapter(private val context: Context, private var dataList: MutableList<PoiItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mapSearchListener: MapSearchListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.attend_map_search_list, parent, false)
        return MapSearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        try {
            if (holder is MapSearchViewHolder) {
                holder.tvSearchContent.text = dataList[position].title + "(" + dataList[position].snippet + ")"
                holder.tvSearchContent.setOnClickListener {
                    mapSearchListener?.itemClick(position)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

interface MapSearchListener {
    fun itemClick(position: Int)
}

class MapSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvSearchContent = itemView.findViewById(R.id.tv_search_content) as TextView
}



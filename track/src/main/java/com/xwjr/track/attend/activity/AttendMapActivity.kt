package com.xwjr.track.attend.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.amap.api.maps.AMap
import com.xwjr.track.R
import com.xwjr.track.attend.extension.initDrawableRightView
import kotlinx.android.synthetic.main.activity_attend_map.*
import kotlinx.android.synthetic.main.attend_title.*
import com.xwjr.track.TrackConfig
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.xwjr.track.TrackLocationData
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.xwjr.track.LogUtils
import com.xwjr.track.attend.adapter.MapSearchAdapter
import android.view.inputmethod.InputMethodManager
import com.xwjr.track.attend.adapter.MapSearchListener
import com.xwjr.track.attend.extension.showTip


/**
 * 设置考勤地点
 */
class AttendMapActivity : AppCompatActivity() {
    private var aMap: AMap? = null
    private var pointX: Double = 0.0
    private var pointY: Double = 0.0
    private var pointAddress: String = ""
    private var pointCity: String = ""
    private var mapSearchList: MutableList<PoiItem> = arrayListOf()
    private var marker: Marker? = null
    private var circle: Circle? = null
    private var circleRadius = 0.toDouble()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend_map)
        mv.onCreate(savedInstanceState)
        init()
        setListener()
        defaultData()
    }

    private fun init() {
        tv_title.text = "选择考勤地址"
        tv_right.visibility = View.GONE
        et_search.initDrawableRightView(R.mipmap.attend_icon_search_gray, 16f, 16f)
        circleRadius = intent.getStringExtra("circleRadius").toDouble()
        rv_map_search.layoutManager = LinearLayoutManager(this)
        rv_map_search.adapter = MapSearchAdapter(this@AttendMapActivity, mapSearchList).apply {
            this.mapSearchListener = object : MapSearchListener {
                override fun itemClick(position: Int) {
                    hideSoftKeyboard()
                    pointX = mapSearchList[position].latLonPoint.latitude
                    pointY = mapSearchList[position].latLonPoint.longitude
                    val latLng = LatLng(pointX, pointY)
                    circle?.center = latLng
                    TrackLocationData.getAMapAddress(this@AttendMapActivity, pointX, pointY) { address, city ->
                        pointAddress = address
                        pointCity = city
                        marker?.position = latLng
                        marker?.snippet = pointAddress
                        marker?.showInfoWindow()
                    }
                    moveCamera(latLng)
                    rv_map_search.visibility = View.GONE
                }
            }
        }
        initMapLocation()
    }

    private fun setListener() {
        iv_back.setOnClickListener {
            hideSoftKeyboard()
            finish()
        }


//        var inputQuery: InputtipsQuery
//        var inputTips: Inputtips

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s.toString().isNotEmpty()) {
                    rv_map_search.visibility = View.VISIBLE
                } else {
                    rv_map_search.visibility = View.GONE
                }
                searchPoint(s.toString())
//                inputQuery = InputtipsQuery(s.toString(), pointCity)
//                inputQuery.cityLimit = false//限制在当前城市
//                inputTips = Inputtips(this@AttendMapActivity, inputQuery)
//                inputTips.setInputtipsListener { p0, _ -> LogUtils.i("检索返回数据：$p0") }
//                inputTips.requestInputtipsAsyn()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        tv_sure.setOnClickListener {
            showTip("确定使用此位置？", "确定") {
                val intent = Intent()
                intent.putExtra("latitude", pointX.toString())
                intent.putExtra("longitude", pointY.toString())
                intent.putExtra("location", pointAddress)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun defaultData() {

    }

    private fun initMapLocation() {
        if (aMap == null) {
            aMap = mv.map
        }
//        //绘制定位蓝点
//        val myLocationStyle = MyLocationStyle()//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
//        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        aMap?.myLocationStyle = myLocationStyle//设置定位蓝点的Style
//        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
//        aMap?.isMyLocationEnabled = true


        //绘制定位锚点 和面
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")
        pointX = if (latitude.isNullOrEmpty()){
            TrackConfig.getLatitude().toDouble()
        }else{
            latitude.toDouble()
        }

        pointY = if (longitude.isNullOrEmpty()){
            TrackConfig.getLongitude().toDouble()
        }else{
            longitude.toDouble()
        }

        var latLng = LatLng(pointX, pointY)

        TrackLocationData.getAMapAddress(this@AttendMapActivity, pointX, pointY) { address, city ->
            pointAddress = address
            pointCity = city
            marker?.position = latLng
            marker?.snippet = pointAddress
            marker?.showInfoWindow()
        }

        val markerOptions = MarkerOptions().position(latLng).title("当前位置").snippet(pointAddress).draggable(true).visible(true)
        marker = aMap?.addMarker(markerOptions)
        marker?.showInfoWindow()
        //绘制范围
        val circleOptions = CircleOptions().center(latLng).radius(circleRadius).fillColor(Color.argb(80, 0, 132, 255)).strokeColor(Color.argb(80, 0, 132, 255)).strokeWidth(15f)
        circle = aMap?.addCircle(circleOptions)


        // 定义 Marker拖拽的监听
        val markerDragListener = object : AMap.OnMarkerDragListener {

            // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            override fun onMarkerDragStart(arg0: Marker) {
            }

            // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            override fun onMarkerDragEnd(arg0: Marker) {
                pointX = arg0.position.latitude
                pointY = arg0.position.longitude
                latLng = LatLng(pointX, pointY)
                circle?.center = latLng
                TrackLocationData.getAMapAddress(this@AttendMapActivity, pointX, pointY) { address, city ->
                    pointAddress = address
                    pointCity = city
                    marker?.snippet = pointAddress
                    marker?.showInfoWindow()
                }
            }

            // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            override fun onMarkerDrag(arg0: Marker) {
            }
        }
        aMap?.setOnMarkerDragListener(markerDragListener)
        moveCamera(latLng)
    }

    private fun moveCamera(latLng: LatLng) {
        //移动绘制点到地图中心 调整缩放级别
        val mCameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 17f, 0f, 0f))
        aMap?.animateCamera(mCameraUpdate)
    }

    //搜索地点
    private fun searchPoint(key: String) {
        val query = PoiSearch.Query(key, "", "")
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.pageSize = 100// 设置每页最多返回多少条poiitem
        query.pageNum = 1 //设置查询页码

        val poiSearch = PoiSearch(this, query)
        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
            }

            override fun onPoiSearched(p0: PoiResult?, p1: Int) {
                mapSearchList.clear()

                p0?.pois?.forEach {
                    mapSearchList.add(it)
                }

                LogUtils.i("搜索的数据：$mapSearchList")

                rv_map_search.adapter.notifyDataSetChanged()

            }

        })
        poiSearch.searchPOIAsyn()
    }


    override fun onResume() {
        super.onResume()
        mv.onResume()
    }

    override fun onPause() {
        super.onPause()
        mv.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mv.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mv.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        hideSoftKeyboard()
        super.onBackPressed()
    }

    fun hideSoftKeyboard() {
        val view = currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}

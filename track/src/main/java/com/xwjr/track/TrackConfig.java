package com.xwjr.track;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;


public class TrackConfig {

    public static Boolean isDebug = true;
    static Context context;
    static String logTag = "track";
    static String trackUrl = "http://p2psp.kfxfd.cn:9080/apphub/tracking/";
    static String trackApphubkey = "49dd08f0-24e6-11e7-b026-6b0b8b32be51";
    static String latitude = "";
    static String longitude = "";
    static String address = "";
    static String city="";
    static int singleDataLimit = 20;
    static long locationInterval = 60000;
    static String battery = "-1";
    static boolean localDataAutoUpload = true;
    static boolean debug = false;
    static String xwjrUserId = "";
    static String empId = "";

    public static void init(Context context, String trackUrl, String trackApphubkey, String amapKey) {
        try {
            TrackConfig.trackUrl = trackUrl;
            TrackConfig.trackApphubkey = trackApphubkey;
            TrackConfig.context = context;
            AMapLocationClient.setApiKey(amapKey);
            new TrackLocationData().initAMap();
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            context.registerReceiver(new BatteryReceiver(), filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init(Context context, String trackUrl, String trackApphubkey, int singleDataLimit, String amapKey) {
        try {
            TrackConfig.trackUrl = trackUrl;
            TrackConfig.trackApphubkey = trackApphubkey;
            TrackConfig.context = context;
            TrackConfig.singleDataLimit = singleDataLimit;
            AMapLocationClient.setApiKey(amapKey);
            new TrackLocationData().initAMap();
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            context.registerReceiver(new BatteryReceiver(), filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //设置定位间隔时间
    public static void setLocationInterval(long time) {
        locationInterval = time;
    }

    //设置本地数据自动上传功能
    public static void setLocalDataAutoUpload(boolean data) {
        localDataAutoUpload = data;
    }

    //设置debug模式
    public static void setDebug(boolean data) {
        debug = data;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        TrackConfig.context = context;
    }

    public static String getLogTag() {
        return logTag;
    }

    public static void setLogTag(String logTag) {
        TrackConfig.logTag = logTag;
    }

    public static String getTrackUrl() {
        return trackUrl;
    }

    public static void setTrackUrl(String trackUrl) {
        TrackConfig.trackUrl = trackUrl;
    }

    public static String getTrackApphubkey() {
        return trackApphubkey;
    }

    public static void setTrackApphubkey(String trackApphubkey) {
        TrackConfig.trackApphubkey = trackApphubkey;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String latitude) {
        TrackConfig.latitude = latitude;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String longitude) {
        TrackConfig.longitude = longitude;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        TrackConfig.address = address;
    }

    public static int getSingleDataLimit() {
        return singleDataLimit;
    }

    public static void setSingleDataLimit(int singleDataLimit) {
        TrackConfig.singleDataLimit = singleDataLimit;
    }

    public static long getLocationInterval() {
        return locationInterval;
    }

    public static String getBattery() {
        return battery;
    }

    public static void setBattery(String battery) {
        TrackConfig.battery = battery;
    }

    public static boolean isLocalDataAutoUpload() {
        return localDataAutoUpload;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static String getXwjrUserId() {
        return xwjrUserId;
    }

    public static void setXwjrUserId(String xwjrUserId) {
        TrackConfig.xwjrUserId = xwjrUserId;
    }

    public static String getEmpId() {
        return empId;
    }

    public static void setEmpId(String empId) {
        TrackConfig.empId = empId;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        TrackConfig.city = city;
    }
}

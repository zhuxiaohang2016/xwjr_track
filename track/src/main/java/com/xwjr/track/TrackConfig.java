package com.xwjr.track;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.amap.api.location.AMapLocationClient;


public class TrackConfig {

    static Context context;
    static String logTag = "track";
    static String trackUrl = "http://p2psp.kfxfd.cn:9080/apphub/tracking/";
    static String trackApphubkey = "49dd08f0-24e6-11e7-b026-6b0b8b32be51";
    static String latitude = "";
    static String longitude = "";
    static String address = "";
    static String city = "";
    static int singleDataLimit = 20;
    static int singleFKDataLimit = 50;
    static long locationInterval = 60000;
    static String battery = "-1";
    static boolean localDataAutoUpload = true;
    static boolean debug = false;
    static String attendUrl = "";

    static String uploadUrl = "";//上传通讯录等地址
    static String xwjrToken = "";//用户token


    public static void init(Context context, String trackUrl, String trackApphubkey, String amapKey) {
        try {
            TrackConfig.trackUrl = trackUrl;
            TrackConfig.trackApphubkey = trackApphubkey;
            TrackConfig.context = context;
            AMapLocationClient.setApiKey(amapKey);
            TrackLocationData.initAMap(context);
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
            TrackLocationData.initAMap(context);
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

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        TrackConfig.city = city;
    }

    public static String getAttendUrl() {
        return attendUrl;
    }

    public static void setAttendUrl(String attendUrl) {
        TrackConfig.attendUrl = attendUrl;
    }

    public static String getUploadUrl() {
        return uploadUrl;
    }

    public static void setUploadUrl(String uploadUrl) {
        TrackConfig.uploadUrl = uploadUrl;
    }

    public static String getXwjrToken() {
        return xwjrToken;
    }

    public static void setXwjrToken(String xwjrToken) {
        TrackConfig.xwjrToken = xwjrToken;
    }

    public static int getSingleFKDataLimit() {
        return singleFKDataLimit;
    }

    public static void setSingleFKDataLimit(int singleFKDataLimit) {
        TrackConfig.singleFKDataLimit = singleFKDataLimit;
    }
}

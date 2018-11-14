package com.xwjr.track;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import xwjr.amap.api.location.AMapLocationClient;

public class TrackConfig {

    static Context context;
    static String logTag = "track";
    static String trackUrl = "http://p2psp.kfxfd.cn:9080/apphub/tracking/";
    static String trackApphubkey = "49dd08f0-24e6-11e7-b026-6b0b8b32be51";
    static String latitude = "";
    static String longitude = "";
    static String address = "";
    static int singleDataLimit = 20;
    static long locationInterval = 60000;
    static String battery = "-1";
    static boolean localDataAutoUpload = true;
    static boolean debug = false;

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
}

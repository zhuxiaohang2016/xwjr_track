package com.xwjr.track;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;

public class TrackConfig {

    public static Context context;
    public static String logTag = "track";
    public static String trackUrl = "http://p2psp.kfxfd.cn:9080/apphub/tracking/";
    public static String trackApphubkey = "49dd08f0-24e6-11e7-b026-6b0b8b32be51";
    public static String latitude = "";
    public static String longitude = "";
    public static String address = "";
    public static int singleDataLimit = 20;
    public static long locationInterval = 60000;
    public static boolean localDataAutoUpload = true;

    public static void init(Context context, String trackUrl, String trackApphubkey, String amapKey) {
        try {
            TrackConfig.trackUrl = trackUrl;
            TrackConfig.trackApphubkey = trackApphubkey;
            TrackConfig.context = context;
            AMapLocationClient.setApiKey(amapKey);
            new TrackLocationData().initAMap();
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
}

package com.xwjr.track;

import android.content.Context;

public class TrackConfig {

    public static Context context;
    public static String logTag = "track";
    public static String trackUrl = "http://p2psp.kfxfd.cn:9080/apphub/tracking/";
    public static String trackApphubkey = "49dd08f0-24e6-11e7-b026-6b0b8b32be51";
    public static String latitude = "";
    public static String longitude = "";

    public static void init(Context context, String trackUrl, String trackApphubkey) {
        try {
            TrackConfig.trackUrl = trackUrl;
            TrackConfig.trackApphubkey = trackApphubkey;
            TrackConfig.context = context;

            new LocationData().getLngAndLat();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

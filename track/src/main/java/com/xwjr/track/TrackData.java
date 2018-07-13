package com.xwjr.track;


import android.os.Build;

import java.util.HashMap;
import java.util.Map;

public class TrackData {

    public static Map<String, String> getCommonMap() {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("latitude", TrackConfig.latitude);
            map.put("longitude", TrackConfig.longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("appKey", TrackConfig.trackApphubkey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("currentVersionName", AndroidUtil.getVersionName(TrackConfig.context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            map.put("systemInfo_ID",Build.ID);
            map.put("systemInfo_BRAND",Build.BRAND);
            map.put("systemInfo_MODEL",Build.MODEL);
            map.put("systemInfo_RELEASE",Build.VERSION.RELEASE);
            map.put("systemInfo_SDK",Build.VERSION.SDK);
            map.put("systemInfo_BOARD",Build.BOARD);
            map.put("systemInfo_PRODUCT",Build.PRODUCT);
            map.put("systemInfo_DEVICE",Build.DEVICE);
            map.put("systemInfo_FINGERPRINT",Build.FINGERPRINT);
            map.put("systemInfo_HOST",Build.HOST);
            map.put("systemInfo_TAGS",Build.TAGS);
            map.put("systemInfo_TYPE",Build.TYPE);
            map.put("systemInfo_TIME",String.valueOf(Build.TIME));
            map.put("systemInfo_INCREMENTAL",Build.VERSION.INCREMENTAL);
            map.put("systemInfo_DISPLAY",Build.DISPLAY);
            map.put("systemInfo_SDK_INT",String.valueOf(Build.VERSION.SDK_INT));
            map.put("systemInfo_MANUFACTURER",Build.MANUFACTURER);
            map.put("systemInfo_BOOTLOADER",Build.BOOTLOADER);
            map.put("systemInfo_CPU_ABI",Build.CPU_ABI);
            map.put("systemInfo_CPU_ABI2",Build.CPU_ABI2);
            map.put("systemInfo_HARDWARE",Build.HARDWARE);
            map.put("systemInfo_UNKNOWN",Build.UNKNOWN);
            map.put("systemInfo_CODENAME",Build.VERSION.CODENAME);
            map.put("systemInfo_SERIAL",Build.SERIAL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

}

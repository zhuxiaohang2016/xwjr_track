package com.xwjr.track;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TrackLocalData {

    private static String XWJRTrackTable = "XWJRTrackTable";
    private static String XWJRTrackData = "XWJRTrackData";
    private static String USER_ID = "USER_ID";
    private static String CONTACT_SIZE = "CONTACT_SIZE";
    private static String READ_CONTACT_STATUS = "READ_CONTACT_STATUS";
    private static TimerTask timerTask;
    private static Timer timer;


    /**
     * 储存用户ID数据
     */
    public static void saveUserId(String data) {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_ID, data);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户ID数据
     */
    public static String getUserId() {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            return sharedPreferences.getString(USER_ID, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 储存用户联系人数量与读取状态
     */
    public static void saveContactStatus(String data) {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (data.equals("START")){
                editor.putString(READ_CONTACT_STATUS, data);
                editor.putString(CONTACT_SIZE, "0");
            }else if (data.equals("END")){
                editor.putString(READ_CONTACT_STATUS, data);
            }else {
                editor.putString(READ_CONTACT_STATUS, "READING");
                editor.putString(CONTACT_SIZE, data);
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户联系人读取状态
     */
    public static String getContactStatus() {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            return sharedPreferences.getString(READ_CONTACT_STATUS, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取通讯录总数
     */
    public static String getContactSize() {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            return sharedPreferences.getString(CONTACT_SIZE, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 储存单次数据
     */
    public static void saveTrackData(Map<String, String> data) {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            List<Map<String, String>> d = getTrackData();
            d.add(data);
            editor.putString(XWJRTrackData, new Gson().toJson(d));
            editor.commit();
            checkLength(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 储存单次数据
     */
    public static void saveTrackData(List<Map<String, String>> data) {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            List<Map<String, String>> d = getTrackData();
            d.addAll(data);
            editor.putString(XWJRTrackData, new Gson().toJson(d));
            editor.commit();
            //如果超过20条，则上传
            checkLength(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkLength(List<Map<String, String>> d) {
        if (TrackConfig.debug) {
            LogUtils.i("当前本地数据：" + d.size() + "条");
        }
        if (TrackConfig.localDataAutoUpload) {
            //如果超过20条，则上传
            if (d.size() >= TrackConfig.singleDataLimit) {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        TrackOperate.upLoadLocalData();
                    }
                };
                if (timer != null) {
                    timer.cancel();
                }
                timer = new Timer();
                timer.schedule(timerTask, 3000);
            }
        }
    }

    /**
     * 获取数据
     */
    public static List<Map<String, String>> getTrackData() {
        List<Map<String, String>> maps = new ArrayList<>();
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            Type type = new TypeToken<List<Map<String, String>>>() {
            }.getType();
            String d = sharedPreferences.getString(XWJRTrackData, "");
            if (!TextUtils.isEmpty(d)) {
                return new Gson().fromJson(d, type);
            }
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            return maps;
        }
    }

    /**
     * 获取数据
     */
    public static void clearTrackData() {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(XWJRTrackData, "");
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

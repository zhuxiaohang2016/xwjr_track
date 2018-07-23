package com.xwjr.track;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrackLocalData {

    private static String XWJRTrackTable = "XWJRTrackTable";
    private static String XWJRTrackData = "XWJRTrackData";

    /**
     * 储存单次数据
     */
    public static void saveTrackData(Map<String, String> data) {
        try {
            SharedPreferences sharedPreferences = TrackConfig.context.getSharedPreferences(XWJRTrackTable, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            List<Map<String,String>> d = getTrackData();
            d.add(data);
            editor.putString(XWJRTrackData, new Gson().toJson(d));
            editor.commit();
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
            List<Map<String,String>> d = getTrackData();
            d.addAll(data);
            editor.putString(XWJRTrackData, new Gson().toJson(d));
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
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

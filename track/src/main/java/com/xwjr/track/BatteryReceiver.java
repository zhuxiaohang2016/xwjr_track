package com.xwjr.track;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReceiver extends BroadcastReceiver {

    public BatteryReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            int current = intent.getExtras().getInt("level");// 获得当前电量
            int total = intent.getExtras().getInt("scale");// 获得总电量
            int percent = current * 100 / total;
            TrackConfig.battery = String.valueOf(percent);
        } catch (Exception e) {
            TrackConfig.battery = "-1";
            e.printStackTrace();
        }


    }
}
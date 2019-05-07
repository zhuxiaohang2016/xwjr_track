package com.xwjr.xwjrtrack;

import android.app.Application;

import com.xwjr.track.attend.extension.BroadcastExtensionKt;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BroadcastExtensionKt.registerAutoSignBroadCast(this);
    }
}

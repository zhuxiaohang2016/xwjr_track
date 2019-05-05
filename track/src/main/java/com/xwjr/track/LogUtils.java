package com.xwjr.track;

import android.util.Log;

public class LogUtils {
    public static void i(String content) {
        if (TrackConfig.isDebug()) {
            Log.i("track", content);
        }
    }

    public static void e(String content) {
        if (TrackConfig.isDebug()) {
            Log.e("track", content);
        }
    }
}

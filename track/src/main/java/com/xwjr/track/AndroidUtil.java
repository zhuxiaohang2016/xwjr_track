package com.xwjr.track;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 手机信息 & MAC地址 & 开机时间等
 */
public class AndroidUtil {


    /**
     * 获取 MAC 地址
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        return mac;
    }

    /**
     * 获取本地Ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            int ipAddress = info.getIpAddress();
            String Ipv4Address = InetAddress
                    .getByName(
                            String.format("%d.%d.%d.%d", (ipAddress & 0xff),
                                    (ipAddress >> 8 & 0xff),
                                    (ipAddress >> 16 & 0xff),
                                    (ipAddress >> 24 & 0xff))).getHostAddress()
                    .toString();
            return Ipv4Address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 开机时间
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);
        return h + ":" + m;
    }

    public static String getSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("ID:").append(Build.ID);
        sb.append(",BRAND:").append(Build.BRAND);
        sb.append(",MODEL:").append(Build.MODEL);
        sb.append(",RELEASE:").append(Build.VERSION.RELEASE);
        sb.append(",SDK:").append(Build.VERSION.SDK);
        sb.append(",BOARD:").append(Build.BOARD);
        sb.append(",PRODUCT:").append(Build.PRODUCT);
        sb.append(",DEVICE:").append(Build.DEVICE);
        sb.append(",FINGERPRINT:").append(Build.FINGERPRINT);
        sb.append(",HOST:").append(Build.HOST);
        sb.append(",TAGS:").append(Build.TAGS);
        sb.append(",TYPE:").append(Build.TYPE);
        sb.append(",TIME:").append(Build.TIME);
        sb.append(",INCREMENTAL:").append(Build.VERSION.INCREMENTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append(",DISPLAY:").append(Build.DISPLAY);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append(",SDK_INT:").append(Build.VERSION.SDK_INT);
            sb.append(",MANUFACTURER:").append(Build.MANUFACTURER);
            sb.append(",BOOTLOADER:").append(Build.BOOTLOADER);
            sb.append(",CPU_ABI:").append(Build.CPU_ABI);
            sb.append(",CPU_ABI2:").append(Build.CPU_ABI2);
            sb.append(",HARDWARE:").append(Build.HARDWARE);
            sb.append(",UNKNOWN:").append(Build.UNKNOWN);
            sb.append(",CODENAME:").append(Build.VERSION.CODENAME);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sb.append(",SERIAL:").append(Build.SERIAL);
        }
        return sb.toString();
    }

    public static String getVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}

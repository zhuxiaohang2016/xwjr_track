package com.xwjr.track;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.xwjr.track.TrackConfig.context;

/**
 * 手机信息 & MAC地址 & 开机时间等
 */
public class AndroidUtil {

    public static String getDeviceId() {
        try {
            final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(TrackConfig.context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            if (manager.getDeviceId() == null || manager.getDeviceId().equals("")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    return manager.getDeviceId(0);
                }
            } else {
                return manager.getDeviceId();
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public static String getAndroidID() {
        try {
            return Settings.Secure.getString(TrackConfig.context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取 MAC 地址
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        //wifi mac地址
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String mac = info.getMacAddress();
            return mac;
        } catch (Exception e) {
            return "";
        }

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

    /**
     * 获取当前网络连接的类型
     *
     * @param context context
     * @return int
     */
    public static String getNetworkState(Context context) {
        try {

            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
            if (null == connManager) { // 为空则认为无网络
                return "NO";
            }
            // 获取网络类型，如果为空，返回无网络
            NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
            if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
                return "NO";
            }
            // 判断是否为WIFI
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != wifiInfo) {
                NetworkInfo.State state = wifiInfo.getState();
                if (null != state) {
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        return "WIFI";
                    }
                }
            }
            // 若不是WIFI，则去判断是2G、3G、4G网
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = telephonyManager.getNetworkType();
            switch (networkType) {
            /*
             GPRS : 2G(2.5) General Packet Radia Service 114kbps
             EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
             UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
             CDMA : 2G 电信 Code Division Multiple Access 码分多址
             EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
             EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
             1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
             HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
             HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
             HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
             IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
             EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
             LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
             EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
             HSPAP : 3G HSPAP 比 HSDPA 快些
             */
                // 2G网络
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                // 3G网络
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                // 4G网络
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:
                    return "MOBILE";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

}

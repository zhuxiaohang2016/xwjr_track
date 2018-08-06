package com.xwjr.track;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class TrackLocationData {
    /**
     * 获取经纬度
     */
    public String getLngAndLat() {
        try {
            double latitude = 0.0;
            double longitude = 0.0;
            LocationManager locationManager = (LocationManager) TrackConfig.context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
                if (ActivityCompat.checkSelfPermission(TrackConfig.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(TrackConfig.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TrackConfig.logTag, "无获取定位信息权限");
                    return "";
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } else {//当GPS信号弱没获取到位置的时候又从网络获取
                    return getLngAndLatWithNetwork();
                }
            } else {    //从网络获取经纬度
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            TrackConfig.latitude = String.valueOf(latitude);
            TrackConfig.longitude = String.valueOf(longitude);
            TrackConfig.address = getAddress(latitude, longitude);
            return longitude + "," + latitude;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



    //从网络获取经纬度
    public String getLngAndLatWithNetwork() {
        try {
            double latitude = 0.0;
            double longitude = 0.0;
            LocationManager locationManager = (LocationManager) TrackConfig.context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(TrackConfig.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(TrackConfig.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TrackConfig.logTag, "无获取定位信息权限");
                return "";
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            TrackConfig.latitude = String.valueOf(latitude);
            TrackConfig.longitude = String.valueOf(longitude);
            TrackConfig.address = getAddress(latitude, longitude);
            return longitude + "," + latitude;
        } catch (Exception e) {
            return "";
        }
    }


    LocationListener locationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            try {
                TrackConfig.latitude = String.valueOf(location.getLatitude());
                TrackConfig.longitude = String.valueOf(location.getLongitude());
                TrackConfig.address = getAddress(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    //解析地址
    private String getAddress(double latitude, double longitude) {
        Address address = LocationUtils.getAddress(latitude, longitude);
        if (address != null) {
            return address.getLocality() + address.getSubLocality() + address.getFeatureName();
        } else {
            return "";
        }
    }

}

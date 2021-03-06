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
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import javax.security.auth.callback.Callback;


public class TrackLocationData {

    //声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public static AMapLocationListener mLocationListener = null;

    public static AMapLocationClientOption mLocationOption = null;


    public static void initAMap(final Context context) {

        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        TrackConfig.latitude = String.valueOf(amapLocation.getLatitude());//获取纬度
                        TrackConfig.longitude = String.valueOf(amapLocation.getLongitude());//获取经度
                        TrackConfig.address = amapLocation.getAddress();
                        TrackConfig.city = amapLocation.getCity();
                        if (TextUtils.isEmpty(TrackConfig.address)) {
                            getAMapAddress(context, amapLocation.getLatitude(), amapLocation.getLongitude(), null);
                        }
                        LogUtils.i("高德地图定位地址：" + TrackConfig.latitude + "  " + TrackConfig.longitude + "  " + TrackConfig.address);
                    } else {
                        TrackConfig.latitude = "";
                        TrackConfig.longitude = "";
                        TrackConfig.address = "";
                        TrackConfig.city = "";
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtils.e("location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                } else {
                    TrackConfig.latitude = "";
                    TrackConfig.longitude = "";
                    TrackConfig.address = "";
                    TrackConfig.city = "";
                }
            }
        };

        //初始化定位
        mLocationClient = new AMapLocationClient(TrackConfig.context);
        mLocationListener = mAMapLocationListener;
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(TrackConfig.locationInterval);
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //启动定位
        mLocationClient.startLocation();
    }

    public static void setLocationInterval(int time) {
        try {
            mLocationClient.stopLocation();
            mLocationOption.setInterval(time);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recoverLocationInterval() {
        try {
            mLocationClient.stopLocation();
            mLocationOption.setInterval(TrackConfig.locationInterval);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshLocation(final Context context) {
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        TrackConfig.latitude = String.valueOf(amapLocation.getLatitude());//获取纬度
                        TrackConfig.longitude = String.valueOf(amapLocation.getLongitude());//获取经度
                        TrackConfig.address = amapLocation.getAddress();
                        TrackConfig.city = amapLocation.getCity();
                        if (TextUtils.isEmpty(TrackConfig.address)) {
                            getAMapAddress(context, amapLocation.getLatitude(), amapLocation.getLongitude(), null);
                        }
                        LogUtils.i("高德地图定位地址：" + TrackConfig.latitude + "  " + TrackConfig.longitude + "  " + TrackConfig.address);
                    } else {
                        TrackConfig.latitude = "";
                        TrackConfig.longitude = "";
                        TrackConfig.address = "";
                        TrackConfig.city = "";
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        LogUtils.e("location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                } else {
                    TrackConfig.latitude = "";
                    TrackConfig.longitude = "";
                    TrackConfig.address = "";
                    TrackConfig.city = "";
                }
            }
        };

        //初始化定位
        AMapLocationClient mLocationClient = new AMapLocationClient(TrackConfig.context);
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();
    }


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
                    LogUtils.i("无获取定位信息权限");
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
                LogUtils.i("无获取定位信息权限");
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
            String data = "";
            if (address.getLocality() != null && !address.getLocality().equals("null"))
                data += address.getLocality();
            if (address.getSubLocality() != null && !address.getSubLocality().equals("null"))
                data += address.getSubLocality();
            if (address.getFeatureName() != null && !address.getFeatureName().equals("null"))
                data += address.getFeatureName();
            return data;
        } else {
            return "";
        }
    }

    public static void getAMapAddress(Context context, double latitude, double longitude, final LocationAddressCallBack callback) {
        final LatLonPoint latLng = new LatLonPoint(latitude, longitude);
        final GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        final int[] range = {1000};
        final RegeocodeQuery[] query = {new RegeocodeQuery(latLng, range[0], GeocodeSearch.AMAP)};
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == 1000 && !TextUtils.isEmpty(regeocodeResult.getRegeocodeAddress().getFormatAddress())) {
                    if (callback == null) {
                        TrackConfig.address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                        TrackConfig.city = regeocodeResult.getRegeocodeAddress().getCity();
                    } else {
                        callback.getAddress(regeocodeResult.getRegeocodeAddress().getFormatAddress(), regeocodeResult.getRegeocodeAddress().getCity());
                    }
                    LogUtils.i("高德地图解析地址：" + regeocodeResult.getRegeocodeAddress().getFormatAddress());
                } else {
                    if (range[0] > 10000) {
                        return;
                    }
                    range[0] += 1000;
//                    LogUtils.i("高德地图解析地址扩大范围：" + range[0]);
                    query[0] = new RegeocodeQuery(latLng, range[0], GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query[0]);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            }
        });
        geocoderSearch.getFromLocationAsyn(query[0]);
    }

    public interface LocationAddressCallBack {
        void getAddress(String address, String city);
    }


}

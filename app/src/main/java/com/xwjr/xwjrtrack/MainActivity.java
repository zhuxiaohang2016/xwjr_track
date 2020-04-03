package com.xwjr.xwjrtrack;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.MapView;
import com.xwjr.track.LogUtils;
import com.xwjr.track.TrackConfig;
import com.xwjr.track.TrackLocalData;
import com.xwjr.track.TrackLocationData;
import com.xwjr.track.TrackOperate;
import com.xwjr.track.attend.activity.SignActivity;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TrackConfig.setDebug(true);
        TrackConfig.init(this, "http://p2psp.kfxfd.cn:9080/apphub/tracking/", "49dd08f0-24e6-11e7-b026-6b0b8b32be51", "15e703beb1cc85b69ccba4f2ebb21a37");
        MapView mMapView = null;
        TrackConfig.setAttendUrl("http://p2psp.kfxfd.cn:9080/");
        TrackConfig.setUploadUrl("http://p2p.slowlytime.com:9081/rsapi/tracking/mobilePhone/wwxjk/MYSELF");
        TrackConfig.setXwjrToken("ae3a511c11f5d766156f9dda9e458bc7db1195e767d4f82d93fa95fc6f1d4032");
        TrackConfig.setSingleFKDataLimit(2);
//        TrackConfig.setAttendUrl("http://p2psp.kfxfd.cn:9080/");
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 99);


        findViewById(R.id.tv_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Map<String, String> data = TrackData.getCommonMap();
//                data.put("adad", "dafadf");
//                data.put("dafafdafaf", String.valueOf(System.currentTimeMillis()));
//                TrackLocalData.saveTrackData(data);
//                TrackLocalData.saveTrackData(data);
//                TrackLocalData.saveTrackData(data);
//                TrackOperate.upLoadContract("18810409404");
            }
        });

        findViewById(R.id.tv_amap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AMapActivity.class));
            }
        });
        findViewById(R.id.tv_amap_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackLocationData.setLocationInterval(1000);
            }
        });

        findViewById(R.id.tv_attend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignActivity.class);
                intent.putExtra("userRole", "ZGSZJL");
                intent.putExtra("loginName", "32");
                intent.putExtra("token", "ae3a511c11f5d766156f9dda9e458bc7db1195e767d4f82d93fa95fc6f1d4032");
                intent.putExtra("bankId", "91000");
                startActivity(intent);
            }
        });

        findViewById(R.id.tv_contacts).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},10);
                        TrackOperate.upLoadFKContract("18810409404");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!TrackLocalData.getContactStatus().equals("END")){
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                LogUtils.i("通讯录读取完毕");
                            }
                        }).start();
                    }
                }
        );

        findViewById(R.id.tv_call).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},10);
                        TrackOperate.upLoadFKCall("18810409404");
                    }
                }
        );


        findViewById(R.id.tv_sms).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermissions(new String[]{Manifest.permission.READ_SMS},10);
                        TrackOperate.upLoadFKSMS("18810409404");
                    }
                }
        );


    }

}

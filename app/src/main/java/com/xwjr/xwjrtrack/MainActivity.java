package com.xwjr.xwjrtrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xwjr.track.TrackConfig;
import com.xwjr.track.TrackData;
import com.xwjr.track.TrackOperate;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TrackConfig.init(this, "http://p2psp.kfxfd.cn:9080/apphub/tracking/", "49dd08f0-24e6-11e7-b026-6b0b8b32be51");
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        List<Map<String, String>> uData = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            Map<String, String> data = TrackData.getCommonMap();
//            data.put("love", "false" + i);
//            uData.add(data);
//        }
//
//        TrackOperate.upload(TrackData.mapList2String(uData));


//        TrackData.getContactData("dfadf");
        TrackOperate.upLoadContract("1403204");
        TrackOperate.upLoadCall("1403204");
        TrackOperate.upLoadSMS("1403204");
    }

}

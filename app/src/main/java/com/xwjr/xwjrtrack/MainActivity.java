package com.xwjr.xwjrtrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xwjr.track.TrackConfig;
import com.xwjr.track.TrackData;
import com.xwjr.track.TrackLocalData;
import com.xwjr.track.TrackOperate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


        TrackLocalData.saveTrackData(TrackData.getCommonMap());

        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> data1 = TrackData.getCommonMap();
        data1.put("asd", "dafa");
        mapList.add(data1);

        Map<String, String> data2 = TrackData.getCommonMap();
        data2.put("adfawerw", "dqerqreqafa");
        mapList.add(data2);
        TrackLocalData.saveTrackData(mapList);

        TrackOperate.upLoadLocalData();

    }

}

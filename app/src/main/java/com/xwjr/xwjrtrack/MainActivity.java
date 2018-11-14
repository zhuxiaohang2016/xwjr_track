package com.xwjr.xwjrtrack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xwjr.track.TrackConfig;
import com.xwjr.track.TrackData;
import com.xwjr.track.TrackLocalData;
import com.xwjr.track.TrackLocationData;
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
        TrackConfig.setDebug(true);
        TrackConfig.init(this, "http://p2psp.kfxfd.cn:9080/apphub/tracking/", "49dd08f0-24e6-11e7-b026-6b0b8b32be51", "15e703beb1cc85b69ccba4f2ebb21a37");


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
    }

}

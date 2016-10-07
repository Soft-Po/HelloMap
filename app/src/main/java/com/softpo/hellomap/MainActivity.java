package com.softpo.hellomap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void btnOnclick(View view) {

        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.btnBaseMap:
                intent.setClass(this,BaseMapActivity.class);
                break;
            case R.id.btnLocation:
                intent.setClass(this,LocationActivity.class);
                break;
            case R.id.btnPoi:
                intent.setClass(this,PoiActivity.class);
                break;
            case R.id.btnRoutePlan://路线检索
                intent.setClass(this,RoutePlanActivity.class);

                break;
            case R.id.btnBusLine:
                intent.setClass(this,BusLineActivity.class);
                break;
        }

        startActivity(intent);
    }
}

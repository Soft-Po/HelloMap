package com.softpo.hellomap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class LocationActivity extends AppCompatActivity implements BDLocationListener {

    private MapView mapView;
    private BaiduMap baiduMap;
    //LocationClient类必须在主线程中声明。需要Context类型的参数。 Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
    private LocationClient locaClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initView();

        initLocationClient();
    }

    private void initLocationClient() {

        //设置定位功能可用
        baiduMap.setMyLocationEnabled(true);

//        1、设置定位参数
        LocationClientOption option = new LocationClientOption();

        //3、option一系列设置

        //3.1设置坐标系
        option.setCoorType("bd09ll");
//        3.2设置定位模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        3.3设置开启Gps
        option.setOpenGps(true);
//        3.4设置扫描频率
        option.setScanSpan(3 * 1000);
//        3.5设置是否接受地址信息
        option.setIsNeedAddress(true);
//        3.6设置是否需要设备方向
        option.setNeedDeviceDirect(true);//返回定位结果包含手机机头方向
        //2、使用定位参数
        locaClient.setLocOption(option);

//        4、设置定位的监听
        locaClient.registerLocationListener(this);

//        5、启动定位
        locaClient.start();
        Log.d("flag","--------------->LocationClient.start()");

    }

    private void initView() {
        mapView = ((MapView) findViewById(R.id.mapView));
        baiduMap = mapView.getMap();

        locaClient = new LocationClient(getApplicationContext());
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

//    定位监听的回掉
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

        Log.d("flag", "收到定位数据");

//        1、构造定位数据
        MyLocationData data = new MyLocationData.Builder()
                .direction(bdLocation.getDirection())//获取方向
                .latitude(bdLocation.getLatitude())//纬度
                .longitude(bdLocation.getLongitude())//经度
                .accuracy(bdLocation.getRadius())//定位精度
                .build();
//        2、设置定位数据
        baiduMap.setMyLocationData(data);

//        3、更新地图中心点
//        定位的坐标点
        LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
//        地图状态更新类
        MapStatusUpdate mapSatusUpadate = MapStatusUpdateFactory.newLatLng(ll);
//          更新地图状态
        baiduMap.animateMapStatus(mapSatusUpadate);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locaClient.unRegisterLocationListener(this);
        mapView.onDestroy();
    }
}

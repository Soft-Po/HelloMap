package com.softpo.hellomap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

public class PoiActivity extends AppCompatActivity {
    
    private MapView mMapView;

    private BaiduMap mBaiduMap;
    
    private PoiSearch mPoiSearch;
    
    private EditText input_key,input_city;

    private int pageNum  =1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        
        initView();

        initPoiSearch();
    }

    private void initPoiSearch() {
//        1、获取检索的实例
        mPoiSearch = PoiSearch.newInstance();

        //2、设置监听
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                //如果没有返回值或者没有查到数据
                if(poiResult==null||poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND){
                    return;
                }
                //如果查询过程中没有错误，在地图上显示覆盖物
                if(poiResult.error== SearchResult.ERRORNO.NO_ERROR){

                    mBaiduMap.clear();

//                    1、在地图上初始化覆盖物对象
                    PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);

                    mBaiduMap.setOnMarkerClickListener(overlay);

                    overlay.setData(poiResult);
                    //添加覆盖物
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }
            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {

                if (result == null
                        || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(
                        PoiActivity.this,
                            result.getName() + ":"
                                    + result.getAddress(), Toast.LENGTH_LONG).show();

                    if (result.getDetailUrl() != null) {
                        Intent intent = new Intent();
                        intent.setClass(PoiActivity.this,
                                DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("url", result.getDetailUrl());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }

            }
        });

    }
    private void initView() {
        input_city = ((EditText) findViewById(R.id.input_city));

        input_key = ((EditText) findViewById(R.id.input_key));

        mMapView = ((MapView) findViewById(R.id.mapView));

        mBaiduMap = mMapView.getMap();

    }
    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        // 重写该方法的目的是：为了执行poiSearch.searchPoiDetail()方法，该方法是为了获取每个poi的明细。
        // 只有获取到了明细内容，才能执行onGetPoiDetailResult()方法，才能做到页面跳转到明细页面
        @Override
        public boolean onPoiClick(int index) {
            List<PoiInfo> list = getPoiResult().getAllPoi();
            PoiInfo info = list.get(index);
            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
                    .poiUid(info.uid));
            return true;
        }
    }

    //3、发起检索请求
    public void btnPoiSearch(View view) {

        String city = input_city.getText().toString().trim();
        String key = input_key.getText().toString().trim();

        switch (view.getId()){
            case R.id.btnPoiSearch:
                pageNum = 1;
                break;
            case R.id.btnNext:
                pageNum++;
                break;
        }
        if(!TextUtils.isEmpty(city)&&!TextUtils.isEmpty(key)){
            mPoiSearch.searchInCity(new PoiCitySearchOption()
                    .city(city).keyword(key).pageNum(pageNum));
        }else {
            Toast.makeText(this,"请输入内容",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        4、销毁PoiSearch
        mPoiSearch.destroy();
    }
}

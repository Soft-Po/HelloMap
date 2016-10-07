package com.softpo.hellomap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class BusLineActivity extends AppCompatActivity {

    private EditText editText_city;
    private EditText editText_bus;
    private BaiduMap baiduMap = null;
    private PoiSearch poiSearch = null;
    private BusLineSearch busLineSearch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_bus_line);

        initView();
    }

    private void initView() {
        editText_city = (EditText) findViewById(R.id.editText_city);
        editText_bus = (EditText) findViewById(R.id.editText_bus);

        baiduMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_busline)).getBaiduMap();
        poiSearch = PoiSearch.newInstance();
        busLineSearch = BusLineSearch.newInstance();

        poiSearch
                .setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

                    @Override
                    public void onGetPoiResult(PoiResult result) {
                        busLineSearch.searchBusLine(new BusLineSearchOption()
                                .city(editText_city.getText().toString()).uid(
                                        result.getAllPoi().get(0).uid));

                    }

                    @Override
                    public void onGetPoiDetailResult(PoiDetailResult result) {
                        // TODO Auto-generated method stub

                    }
                });

        busLineSearch
                .setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {

                    @Override
                    public void onGetBusLineResult(BusLineResult result) {
                        if (result == null
                                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                            Toast.makeText(BusLineActivity.this, "无结果！", Toast.LENGTH_LONG)
                                    .show();
                        }
                        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                            // 往地图上显示覆盖物
                            baiduMap.clear();
                            BusLineOverlay overlay = new BusLineOverlay(
                                    baiduMap);
                            overlay.setData(result);
                            overlay.addToMap();
                            overlay.zoomToSpan();
                            baiduMap.setOnMarkerClickListener(overlay);
                        }
                    }
                });
    }

    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                poiSearch.searchInCity(new PoiCitySearchOption().city(
                        editText_city.getText().toString()).keyword(
                        editText_bus.getText().toString()));
                break;
            default:
                break;
        }
    }
}

package com.softpo.hellomap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
//import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

public class RoutePlanActivity extends AppCompatActivity {

    private MapView mapView_routeplan;
    private BaiduMap baiduMap = null;;
    private RoutePlanSearch routePlanSearch = null;
    private EditText editText_routeplan_city;
    private EditText editText_routeplan_start;
    private EditText editText_routeplan_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan);

        initView();
        mapView_routeplan = (MapView) findViewById(R.id.mapView_routeplan);
        Log.d("flag", "------------>mapView: " + mapView_routeplan);
        baiduMap = mapView_routeplan.getMap();
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch
                .setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

                    @Override
                    public void onGetWalkingRouteResult(
                            WalkingRouteResult result) {
                        if (result == null
                                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                            Toast.makeText(RoutePlanActivity.this, "查询结果为空！", Toast.LENGTH_LONG)
                                    .show();
                        }
                        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                            baiduMap.clear();
                            WalkingRouteOverlay overlay = new WalkingRouteOverlay(
                                    baiduMap);
                            baiduMap.setOnMarkerClickListener(overlay);
                            overlay.setData(result.getRouteLines().get(0));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }
                    }

                    @Override
                    public void onGetTransitRouteResult(
                            TransitRouteResult result) {
                        if (result == null
                                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                            Toast.makeText(RoutePlanActivity.this, "查询结果为空！", Toast.LENGTH_LONG)
                                    .show();
                        }
                        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                            baiduMap.clear();
                            TransitRouteOverlay overlay = new TransitRouteOverlay(
                                    baiduMap);
                            baiduMap.setOnMarkerClickListener(overlay);
                            overlay.setData(result.getRouteLines().get(0));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }
                    }

                    @Override
                    public void onGetDrivingRouteResult(
                            DrivingRouteResult result) {
                        if (result == null
                                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                            Toast.makeText(RoutePlanActivity.this, "查询结果为空！", Toast.LENGTH_LONG)
                                    .show();
                        }
                        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                            baiduMap.clear();
                            DrivingRouteOverlay overlay = new DrivingRouteOverlay(
                                    baiduMap);
                            baiduMap.setOnMarkerClickListener(overlay);
                            overlay.setData(result.getRouteLines().get(0));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }
                    }

                    @Override
                    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                    }
                });
    }

    private void initView() {
        editText_routeplan_city = (EditText) findViewById(R.id.editText_routeplan_city);
        editText_routeplan_start = (EditText) findViewById(R.id.editText_routeplan_start);
        editText_routeplan_end = (EditText) findViewById(R.id.editText_routeplan_end);
    }

    public void clickButton(View view) {
        PlanNode startNode = PlanNode.withCityNameAndPlaceName(
                editText_routeplan_city.getText().toString(),
                editText_routeplan_start.getText().toString());
        PlanNode endNode = PlanNode.withCityNameAndPlaceName(
                editText_routeplan_city.getText().toString(),
                editText_routeplan_end.getText().toString());
        switch (view.getId()) {
            case R.id.button_route_drive:
                routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(
                        startNode).to(endNode));
                break;
            case R.id.button_route_walk:
                routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(
                        startNode).to(endNode));
                break;
            case R.id.button_route_transit:
                routePlanSearch.transitSearch(new TransitRoutePlanOption()
                        .city(editText_routeplan_city.getText().toString())
                        .from(startNode).to(endNode));
                break;
        }
    }
}

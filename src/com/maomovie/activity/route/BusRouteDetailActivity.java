package com.maomovie.activity.route;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.maomovie.R;
import com.maomovie.adapter.BusSegmentListAdapter;
import com.maomovie.adapter.DriveResultListAdapter;
import com.maomovie.adapter.DriveSegmentListAdapter;
import com.maomovie.util.amap.AMapUtil;
import com.maomovie.util.amap.DriveRouteColorfulOverLay;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by YanP on 2016/8/26.
 * 业务功能：公交\驾车线路详情界面
 */
public class BusRouteDetailActivity extends Activity implements AMap.OnMapLoadedListener,
        AMap.OnMapClickListener, AMap.InfoWindowAdapter, AMap.OnInfoWindowClickListener,
        AMap.OnMarkerClickListener, View.OnClickListener{
    private AMap aMap;
    private MapView mapView;
    private BusPath mBuspath;
    private BusRouteResult mBusRouteResult;
    private DrivePath mDrivePath;
    private DriveRouteResult mDriveRouteResult;
    private TextView mTitle, mTitleBusRoute, mDesBusRoute;
    private TextView tvShowMap;//显示地图的按钮
    private ListView mBusSegmentList;
    private LinearLayout mBuspathview;
    private TextView mBusMap;
    private BusRouteOverlay mBusrouteOverlay;//公交线路沿途描点
    private DriveRouteColorfulOverLay drivingRouteOverlay;//自驾线路沿途描点

    private int routeType = 0;
    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        mapView = (MapView) findViewById(R.id.route_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        getIntentData();
        init();
        //只对api19以上版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        //为状态栏着色
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_moviebtn_normal);
    }

    /**
     * 设置沉侵式状态栏
     *
     * @param on
     */
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mBuspath = intent.getParcelableExtra("bus_path");
            mBusRouteResult = intent.getParcelableExtra("bus_result");
            mDrivePath = intent.getParcelableExtra("drive_path");
            mDriveRouteResult = intent.getParcelableExtra("drive_result");
            routeType = intent.getIntExtra("route_type", 0);
        }
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mTitle = (TextView) findViewById(R.id.tvTitle);
        mTitleBusRoute = (TextView) findViewById(R.id.firstline);
        mDesBusRoute = (TextView) findViewById(R.id.secondline);
        String dur = "";//时长
        String dis = "";//距离
        int taxiCost = 0;//打的费用
        if (routeType == ROUTE_TYPE_BUS) {
            mTitle.setText("公交路线详情");
            dur = AMapUtil.getFriendlyTime((int) mBuspath.getDuration());
            dis = AMapUtil.getFriendlyLength((int) mBuspath.getDistance());
            taxiCost = (int) mBusRouteResult.getTaxiCost();
        } else {
            mTitle.setText("驾驶路线详情");
            dur = AMapUtil.getFriendlyTime((int) mDrivePath.getDuration());
            dis = AMapUtil.getFriendlyLength((int) mDrivePath.getDistance());
            taxiCost = (int) mDriveRouteResult.getTaxiCost();
        }

        mTitleBusRoute.setText(dur + "(" + dis + ")");
        mDesBusRoute.setText("打车约" + taxiCost + "元");
        mDesBusRoute.setVisibility(View.VISIBLE);
        mBusMap = (TextView) findViewById(R.id.tvMap);
        mBusMap.setVisibility(View.VISIBLE);
        mBuspathview = (LinearLayout) findViewById(R.id.bus_path);

        tvShowMap = (TextView) findViewById(R.id.tvMap);
        tvShowMap.setOnClickListener(this);
        configureListView();
    }

    private void registerListener() {
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }

    private void configureListView() {
        mBusSegmentList = (ListView) findViewById(R.id.bus_segment_list);
        if (routeType == ROUTE_TYPE_BUS) {
            BusSegmentListAdapter mBusSegmentListAdapter = new BusSegmentListAdapter(
                    this.getApplicationContext(), mBuspath.getSteps());
            mBusSegmentList.setAdapter(mBusSegmentListAdapter);
        } else if(routeType == ROUTE_TYPE_DRIVE){
            DriveSegmentListAdapter mDriveSegmentListAdapter = new DriveSegmentListAdapter(
                    this.getApplicationContext(),mDrivePath.getSteps());
            mBusSegmentList.setAdapter(mDriveSegmentListAdapter);
        }

    }

    public void onBackClick(View view) {
        this.finish();
    }

    @Override
    public void onMapLoaded() {
        if (routeType == ROUTE_TYPE_BUS && mBusrouteOverlay != null) {
            mBusrouteOverlay.addToMap();
            mBusrouteOverlay.zoomToSpan();
        } else if (routeType == ROUTE_TYPE_DRIVE && drivingRouteOverlay != null){
            drivingRouteOverlay.addToMap();
            drivingRouteOverlay.zoomToSpan();
        }
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v == tvShowMap){
            mBuspathview.setVisibility(View.GONE);
            mBusMap.setVisibility(View.GONE);
            mapView.setVisibility(View.VISIBLE);
            aMap.clear();// 清理地图上的所有覆盖物
            if (routeType == ROUTE_TYPE_BUS) {//显示公交线路地图
                mBusrouteOverlay = new BusRouteOverlay(this, aMap,
                        mBuspath, mBusRouteResult.getStartPos(),
                        mBusRouteResult.getTargetPos());
                mBusrouteOverlay.removeFromMap();
            } else if(routeType == ROUTE_TYPE_DRIVE){//显示自驾线路地图
                drivingRouteOverlay = new DriveRouteColorfulOverLay(
                        aMap, mDrivePath,
                        mDriveRouteResult.getStartPos(),
                        mDriveRouteResult.getTargetPos(), null);
                drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                drivingRouteOverlay.removeFromMap();
            }
        }
    }
}
package com.maomovie.activity.route;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.*;
import com.maomovie.R;
import com.maomovie.context.MaoApplication;
import com.maomovie.entity.CinemaEntity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by YanP on 2016/8/22.
 * 业务功能：显示影院位置和当前位置
 */
public class ShowMapActivity extends Activity implements View.OnClickListener, LocationSource,
        AMapLocationListener, AMap.OnInfoWindowClickListener, AMap.OnMarkerClickListener,
        AMap.InfoWindowAdapter, AMap.OnMapClickListener {
    private MapView mMapView = null;
    private ImageButton btnBack;
    private TextView tvTitle;//标题名字
    private TextView tvNavigation;//导航
    private Context context;
    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明位置变化回调监听器
    private OnLocationChangedListener mListener;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private CinemaEntity cinemaEntity;
    private Marker currentMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmap);
        cinemaEntity = (CinemaEntity) getIntent().getSerializableExtra("CinemaEntity");
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.amapMapView);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        findView();
        initMap();//地图初始化
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

    /**
     * 控件初始化
     */
    private void findView() {
        context = this;
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(cinemaEntity.getNm());

        tvNavigation = (TextView) findViewById(R.id.tvNavigation);
        tvNavigation.setOnClickListener(this);
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        initMapListener();
        setUpMap();
        markerPoint(cinemaEntity.getLat(), cinemaEntity.getLng());
    }

    private void initMapListener() {
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    private void setUpMap() {
        // 自定义系统定位小蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromResource(R.drawable.ic_boy));// 设置小蓝点的图标
//        myLocationStyle.strokeColor(Color.argb(100,129,194,214));// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(50, 129, 194, 214));// 设置圆形的填充颜色
//        myLocationStyle.anchor(100, 100);//设置小蓝点的锚点
//        myLocationStyle.strokeWidth(0.5f);// 设置圆形的边框粗细
//        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(180);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setScaleControlsEnabled(true);// 设置地图默认的比例尺是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    private void markerPoint(double lat, double lng) {
        // 设置当前地图显示为当前位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        markerOptions.title(cinemaEntity.getNm());
        markerOptions.visible(true);
        markerOptions.draggable(true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cinema_location));
        markerOptions.icon(bitmapDescriptor);
        currentMarker = aMap.addMarker(markerOptions);
        currentMarker.setObject(cinemaEntity.getAddr());
        //主动显示信息窗口
        currentMarker.showInfoWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == tvNavigation){
            Intent intent = new Intent(this,TrafficRouteActivity.class);
            intent.putExtra("CinemaEntity",cinemaEntity);
            startActivity(intent);
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = MaoApplication.mLocationClient;
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //初始化定位参数
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(20000);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.i("Tag", "onLocationChanged");
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                Toast.makeText(context, errText, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.infowindow, null);
        TextView tvNm = (TextView) infoWindow.findViewById(R.id.tvCinemaName);
        TextView tvAddr = (TextView) infoWindow.findViewById(R.id.tvCinemaAddr);
        tvNm.setText(cinemaEntity.getNm());
        tvAddr.setText(cinemaEntity.getAddr());
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this,TrafficRouteActivity.class);
        intent.putExtra("CinemaEntity",cinemaEntity);
        startActivity(intent);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker.isInfoWindowShown()) {
            currentMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
        }
//        Toast.makeText(this,"经度：" + latLng.latitude + "\n纬度：" + latLng.longitude,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        return false;
    }
}
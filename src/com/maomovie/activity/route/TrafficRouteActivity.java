package com.maomovie.activity.route;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.*;
import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.*;
import com.maomovie.R;
import com.maomovie.activity.BaseActivity;
import com.maomovie.adapter.BusResultListAdapter;
import com.maomovie.adapter.DriveResultListAdapter;
import com.maomovie.context.MaoApplication;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.service.LoadingDialog;
import com.maomovie.util.ToastUtil;

import java.util.List;

/**
 * Created by YanP on 2016/8/25.
 * 业务功能：导航主页
 */
public class TrafficRouteActivity extends BaseActivity implements View.OnClickListener, RouteSearch.OnRouteSearchListener {
    private Context mContext;
    private Dialog loadingDialog;
    private ImageButton btnBack;//返回按钮
    private ImageButton btnExchange;//交换按钮
    private RadioButton radioButton0;//公交车
    private RadioButton radioButton1;//自驾车
    private TextView tvEndName;//影院名称
    private TextView tvStartName;//当前位置
    private CinemaEntity cinemaEntity;
    private ListView listView;

    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;

    private final int ROUTE_TYPE_CROSSTOWN = 4;
    private LatLonPoint mStartPoint;//起点
    private LatLonPoint mEndPoint;//终点
    private AMapLocation location;//当前位置信息
    private String mCurrentCityName = "成都";

    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private BusRouteResult mBusRouteResult;
    private BusRouteResult showresult;
    private BusPath showpath;
    private WalkRouteResult mWalkRouteResult;

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_trafficroute);
        findView();
        setView();
        initRoute();
    }

    /**
     * 初始化控件
     */
    private void findView() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnExchange = (ImageButton) findViewById(R.id.btnExchange);
        btnExchange.setOnClickListener(this);

        radioButton0 = (RadioButton) findViewById(R.id.radioButton0);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton0.setOnClickListener(this);
        radioButton1.setOnClickListener(this);

        tvStartName = (TextView) findViewById(R.id.tvCurrentAddr);
        tvEndName = (TextView) findViewById(R.id.tvTagAddr);

        listView = (ListView) findViewById(R.id.listView);
    }

    /**
     * 对控件赋值
     */
    private void setView() {
        mContext = this;
        loadingDialog = LoadingDialog.createLoadingDialog(mContext);
        //接收上一界面传递过来的影院信息实体
        cinemaEntity = (CinemaEntity) getIntent().getSerializableExtra("CinemaEntity");
        //获取定位信息
        location = MaoApplication.currentLocation;
        tvEndName.setText(cinemaEntity.getNm());
        //设置起点坐标
        mStartPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
        //设置终点坐标
        mEndPoint = new LatLonPoint(cinemaEntity.getLat(), cinemaEntity.getLng());
    }

    private void initRoute() {
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
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
        } else if (v == radioButton0) {
            radioButton0.setChecked(true);
            searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
        } else if (v == radioButton1) {
            radioButton1.setChecked(true);
            searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        } else if (v == btnExchange) {
            //起点和终点互换
            String startName = tvStartName.getText().toString();
            String endName = tvEndName.getText().toString();
            tvEndName.setText(startName);
            tvStartName.setText(endName);
            if(tvStartName.getText().toString().equals("我的位置")){
                tvStartName.setTextColor(getResources().getColor(R.color.titleblue));
                tvEndName.setTextColor(getResources().getColor(R.color.black_gray));
            } else {
                tvStartName.setTextColor(getResources().getColor(R.color.black_overlay));
                tvEndName.setTextColor(getResources().getColor(R.color.titleblue));
            }
            LatLonPoint backupPotin = new LatLonPoint(mStartPoint.getLatitude(), mStartPoint.getLongitude());
            mStartPoint = mEndPoint;
            mEndPoint = backupPotin;
            if (radioButton0.isChecked())
                searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
            else if(radioButton1.isChecked())
                searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "起点未设置");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }
        loadingDialog.show();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        } else if (routeType == ROUTE_TYPE_CROSSTOWN) {
            RouteSearch.FromAndTo fromAndTo_bus = new RouteSearch.FromAndTo(
                    mStartPoint, mEndPoint);
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo_bus, mode,
                    "呼和浩特市", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            query.setCityd("农安县");
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        loadingDialog.dismiss();
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
                    BusResultListAdapter busAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
                    listView.setAdapter(busAdapter);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        loadingDialog.dismiss();
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    DriveResultListAdapter adapter = new DriveResultListAdapter(mContext,mDriveRouteResult);
                    listView.setAdapter(adapter);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }


    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        loadingDialog.dismiss();
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }
}
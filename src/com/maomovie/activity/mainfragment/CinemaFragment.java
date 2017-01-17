package com.maomovie.activity.mainfragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.maomovie.R;
import com.maomovie.activity.ChoiceCityAcvitity;
import com.maomovie.activity.MoviesOfCinemaActivity;
import com.maomovie.adapter.AreaFillterAdapter;
import com.maomovie.adapter.FragmentCinemaAdapter;
import com.maomovie.adapter.SubwayFillterAdapter;
import com.maomovie.context.MaoApplication;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.entity.CityEntity;
import com.maomovie.entity.SubwayEntity;
import com.maomovie.entity.SupportCityEntity;
import com.maomovie.service.LoadingDialog;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.sqlite.service.CinemaService;
import com.maomovie.util.GsonUtils;
import com.maomovie.util.HttpUtil;
import com.maomovie.util.SortByDistacneUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CinemaFragment extends Fragment implements View.OnClickListener,
        AreaFillterAdapter.AreaFillterCallback ,SubwayFillterAdapter.SubwayFillterCallback{

    private View view;
    private Dialog myDialog = null;
    private PullToRefreshListView pullToRefresh;
    private int totalPage = 0;//数据总页数
    private int pageIndex = 1;//当前加载的页数
    private int pageSize = 50;//每页加载的条数
    private TextView tvCity;//城市名称
    private LinearLayout selectCity;//选择城市的父控件
    private LinearLayout layFilter; //筛选按钮
    private LinearLayout laySearch; //搜索按钮
    private FragmentCinemaAdapter adapter;
    private AreaFillterAdapter areaFillterAdapter;
    private SubwayFillterAdapter subwayFillterAdapter;
    private LinearLayout containerLocation;// 定位地理位置的父容器
    private ImageView imgAddressRefresh;//地点刷新
    private TextView tvCurrentAddr;//当前地点位置
    private List<CinemaEntity> cinemas = new ArrayList<CinemaEntity>();
    private List<CinemaEntity> backupList = new ArrayList<CinemaEntity>();
    private RefreshCityRecevier receiver = new RefreshCityRecevier();
    private CinemaService cinemaService = new CinemaService();
    private ThreadHelper threadHelper = new ThreadHelper(new Handler());
    //声明行政区域数据源
    public static List<CityEntity.AreasBean> areaList = new ArrayList<CityEntity.AreasBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_cinema, null);
        } else if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViewsInLayout();
        }
        initView();//初始化控件
        //注册广播接收器
        getActivity().registerReceiver(receiver, new IntentFilter("REFRESH_CITY"));
        return view;
    }

    /**
     * 业务需求：当Fragment处于可见状态时，加载数据
     * 背景：我们在做应用的过程中，一个应用的界面可能是多个Fragment切换而成的，但是如果在每次应用启动的时候就去加载大量的网络数据
     * (假设你的每个Fragment都需要加载网络数据，你也可以理解为初始化大量资源)肯定是不好的
     * Fragment虽然有onResume和onPause的，但是这两个方法是Activity的方法，调用时机也是与Activity相同，
     * 和ViewPager搭配使用这个方法就很鸡肋了，根本不是你想要的效果，这里采用一种方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//相当于Fragment的onResume
            if (cinemas == null || cinemas.size() == 0) {
                getCinemasCountFromDatabase();//从数据库查询已经保存的影院数量
                if (MaoApplication.currentLocation != null) {
                    if (MaoApplication.currentLocation.getErrorCode() == 0) {
                        String addressStr = MaoApplication.currentLocation.getAddress();
                        addressStr = addressStr == null ? "地址获取失败" : addressStr;
                        tvCurrentAddr.setText(addressStr.replace("中国", "").replace("四川省", ""));
                    } else {
                        tvCurrentAddr.setText("定位失败：" + MaoApplication.currentLocation.getErrorInfo());
                    }
                }
            }
        } else {//相当于Fragment的onPause

        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tvCity = (TextView) view.findViewById(R.id.tvCityName);
        selectCity = (LinearLayout) view.findViewById(R.id.selectCity);
        selectCity.setOnClickListener(this);

        layFilter = (LinearLayout) view.findViewById(R.id.cinemaFilter);
        layFilter.setOnClickListener(this);

        laySearch = (LinearLayout) view.findViewById(R.id.searchMovie);
        laySearch.setOnClickListener(this);

        pullToRefresh = (PullToRefreshListView) view.findViewById(R.id.cinema_pullListView);
        containerLocation = (LinearLayout) view.findViewById(R.id.container_location);
        imgAddressRefresh = (ImageView) view.findViewById(R.id.cinemaImg_locationRefresh);
        imgAddressRefresh.setOnClickListener(this);
        tvCurrentAddr = (TextView) view.findViewById(R.id.tvCurrntAddress);
        setView();
    }

    /**
     * 为控件设置相关属性
     */
    private void setView() {
        adapter = new FragmentCinemaAdapter(getActivity(), cinemas);
        pullToRefresh.setAdapter(adapter);
        /*
         * Mode.BOTH：同时支持上拉下拉 Mode.PULL_FROM_START：只支持下拉Pulling Down
		 * Mode.PULL_FROM_END：只支持上拉Pulling Up
		 */
        /*
         * 如果Mode设置成Mode.BOTH，需要设置刷新Listener为OnRefreshListener2，
		 * 并实现onPullDownToRefresh()、onPullUpToRefresh()两个方法。
		 * 如果Mode设置成Mode.PULL_FROM_START或Mode.PULL_FROM_END，
		 * 需要设置刷新Listener为OnRefreshListener，同时实现onRefresh()方法。
		 * 当然也可以设置为OnRefreshListener2，但是
		 * Mode.PULL_FROM_START的时候只调用onPullDownToRefresh()方法，
		 * Mode.PULL_FROM的时候只调用onPullUpToRefresh()方法.
		 */
        pullToRefresh.setMode(Mode.BOTH);
        pullToRefresh.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getDataFormInternet();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (pageIndex <= totalPage) {
                    new GetDataFromDatabase().execute();
                } else {
                    pullToRefresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullToRefresh.onRefreshComplete();
                        }
                    },1000);
                }
                containerLocation.setVisibility(View.GONE);
            }
        });
        init();
        /*
         * setOnRefreshListener(OnRefreshListener listener):设置刷新监听器；
		 * setOnLastItemVisibleListener(OnLastItemVisibleListener
		 * listener):设置是否到底部监听器； setOnPullEventListener(OnPullEventListener
		 * listener);设置事件监听器； onRefreshComplete()：设置刷新完成
		 */
        /*
		 * pulltorefresh.setOnScrollListener()
		 */
        // SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
        // SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
        // SCROLL_STATE_IDLE(0) 停止滚动
		/*
		 * setOnLastItemVisibleListener 当用户拉到底时调用
		 */
		/*
		 * setOnTouchListener是监控从点下鼠标 （可能拖动鼠标）到放开鼠标（鼠标可以换成手指）的整个过程
		 * ，他的回调函数是onTouchEvent（MotionEvent event）,
		 * 然后通过判断event.getAction()是MotionEvent.
		 * ACTION_UP还是ACTION_DOWN还是ACTION_MOVE分别作不同行为。
		 * setOnClickListener的监控时间只监控到手指ACTION_DOWN时发生的行为
		 */

        /**
         * 滑动事件
         */
        pullToRefresh.setOnScrollListener(new OnScrollListener() {
            // 当listView开始滑动时，隐藏定位按钮
            // 停止滑动时，显示定位按钮
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        containerLocation.setVisibility(View.VISIBLE);
                        break;
                    case SCROLL_STATE_FLING:
                    case SCROLL_STATE_TOUCH_SCROLL:
                        containerLocation.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        //当用户滑动屏幕顶部调用加载更多的方法时，隐藏地点定位的布局
        pullToRefresh.setOnPullEventListener(new OnPullEventListener<ListView>() {

            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction) {
                if (direction == Mode.PULL_FROM_END) {
                    containerLocation.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 设置下拉刷新样式及点击事件
     */
    private void init() {
        ILoadingLayout startLabels = pullToRefresh.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        // // 设置下拉刷新文本
        // pullToRefresh.getLoadingLayoutProxy(false, true)
        // .setPullLabel("上拉刷新...");
        // pullToRefresh.getLoadingLayoutProxy(false, true).setReleaseLabel(
        // "放开刷新...");
        // pullToRefresh.getLoadingLayoutProxy(false, true).setRefreshingLabel(
        // "正在加载...");
        // // 设置上拉刷新文本
        // pullToRefresh.getLoadingLayoutProxy(true, false)
        // .setPullLabel("下拉刷新...");
        // pullToRefresh.getLoadingLayoutProxy(true, false).setReleaseLabel(
        // "放开刷新...");
        // pullToRefresh.getLoadingLayoutProxy(true, false).setRefreshingLabel(
        // "正在加载...");

        pullToRefresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CinemaEntity entity = cinemas.get(position - 1);
                Intent intent = new Intent(getActivity(), MoviesOfCinemaActivity.class);
                intent.putExtra("CinemaEntity", entity);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == selectCity) {
            //跳转到城市列表界面
            Intent intent = new Intent(getActivity(), ChoiceCityAcvitity.class);
            startActivity(intent);
        } else if (v == laySearch) {

        } else if (v == layFilter) {
            showPopupWindow();
        } else if (v == imgAddressRefresh) {
            if (MaoApplication.currentLocation != null) {
                String addressStr = MaoApplication.currentLocation.getAddress();
                tvCurrentAddr.setText(addressStr.replace("中国", "").replace("四川省", ""));
            }
        }
    }

    private void showPopupWindow() {
        //装载R.layout.additem对应的界面布局
        final View root = getActivity().getLayoutInflater().inflate(R.layout.additem, null);
        //创建PopupWindow对象
        final PopupWindow popupWindow = new PopupWindow(root, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        //设置点击屏幕其他区域，PopupWindow消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景；使用该方法点击窗体之外，才可关闭窗体
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        ListView itemListView = (ListView) root.findViewById(R.id.itemListView);
        TextView tv1 = (TextView) root.findViewById(R.id.tvFillter1);
        TextView tv2 = (TextView) root.findViewById(R.id.tvFillter2);
        TextView tv3 = (TextView) root.findViewById(R.id.tvFillter3);
        TextView tv4 = (TextView) root.findViewById(R.id.tvFillter4);
        TextView tv5 = (TextView) root.findViewById(R.id.tvFillter5);
        TextView[] tvList = new TextView[]{tv1, tv2, tv3, tv4, tv5};
        for (int i = 0; i < tvList.length; i++) {
            TextView textView = tvList[i];
            textView.setOnClickListener(new PopupListener(tvList,itemListView));
        }
        //默认出现行政区域的过滤列表
        areaFillterAdapter = new AreaFillterAdapter(areaList,getActivity(),CinemaFragment.this);
        itemListView.setAdapter(areaFillterAdapter);
        //以下拉方式显示
        popupWindow.showAsDropDown(laySearch);
        //PopupWindow显示在指定位置
        popupWindow.showAtLocation(laySearch, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 行政区域listView点击的回调方法
     * @param v
     * @param position
     */
    @Override
    public void subwayFillterCallback(View v, int position) {
        subwayFillterAdapter.notifyDataSetChanged();
        SubwayEntity subwayEntity = subwayFillterAdapter.subwayList.get(position);
        cinemas.clear();
        for(int i : subwayEntity.getPositons()){
            cinemas.add(backupList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void areaFillterCallback(View v, int position) {
        areaFillterAdapter.notifyDataSetChanged();
        cinemas.clear();
        if(position == 0){
            cinemas.addAll(backupList);
        } else {
            //获取用户选择的行政区域id
            int areaId = areaList.get(position).getId();
            //对数据源遍历找出该行政区域的影院
            for(CinemaEntity entity : backupList){
                if(entity.getAreaId() == areaId){
                    cinemas.add(entity);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    class PopupListener implements View.OnClickListener {
        private TextView[] tvList;
        private ListView listView;

        public PopupListener(TextView[] tvList,ListView listView) {
            this.tvList = tvList;
            this.listView = listView;
        }

        @Override
        public void onClick(View v) {
            cinemas.clear();
            cinemas.addAll(backupList);
            for (int i = 0; i < tvList.length; i++) {
                TextView textView = tvList[i];
                if (v == textView) {
                    textView.setTextColor(getResources().getColor(R.color.color_text_selected));
                    textView.setBackgroundColor(getResources().getColor(R.color.white));
                    switch (i){
                        case 0://行政区域
                            areaFillterAdapter = new AreaFillterAdapter(areaList,getActivity(),CinemaFragment.this);
                            listView.setAdapter(areaFillterAdapter);
                            break;
                        case 1://地铁信息
                            subwayFillterAdapter = new SubwayFillterAdapter(cinemas,getActivity(),CinemaFragment.this);
                            listView.setAdapter(subwayFillterAdapter);
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                } else {
                    textView.setTextColor(getResources().getColor(R.color.black_gray));
                    textView.setBackgroundColor(getResources().getColor(R.color.hologray));
                }
            }
        }
    }


    /**
     * 从手机数据库查询已存储的影院条数
     */
    private void getCinemasCountFromDatabase() {
        myDialog = LoadingDialog.createLoadingDialog(getActivity());
        myDialog.show();
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return cinemaService.queryCount(getActivity());
            }

            @Override
            public void result(Object result) {
                //查询数据库条数失败，直接从网络上获取数据
                if (result == null || result instanceof Exception) {
                    getDataFormInternet();
                } else {//如果查询条数成功，从数据库分页取出影院信息
                    int total = (int) result;
                    totalPage = total % pageSize == 0 ? (total / pageSize) : ((total / pageSize) + 1);
                    if (totalPage > 0)
                        new GetDataFromDatabase().execute();
                    else
                        getDataFormInternet();
                }
            }
        });
    }

    /**
     * 从手机本地数据库查询影院信息的异步任务
     */
    class GetDataFromDatabase extends AsyncTask<Void, Void, Object> {

        @Override
        protected Object doInBackground(Void... params) {
            return cinemaService.queryByPage(getActivity(), pageIndex, pageSize);
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result == null || result instanceof Exception) {
                myDialog.dismiss();
                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                pageIndex++;
                List<CinemaEntity> list = (List<CinemaEntity>) result;
                cinemas.addAll(list);
                backupList.addAll(list);
                if (cinemas.size() > 0) {//如果从数据库查询到结果
                    sortData();
                } else {//如果没有从数据库查询到结果
                    getDataFormInternet();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                getDataFormInternet();
            } finally {
                pullToRefresh.onRefreshComplete();
            }
        }

    }


    /**
     * 从网络上获取影院信息
     */
    private void getDataFormInternet() {
        if (myDialog == null) {
            myDialog = LoadingDialog.createLoadingDialog(getActivity());
            myDialog.show();
        }
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return HttpUtil.getCinemasOfCity(tvCity.getText().toString());
            }

            @Override
            public void result(Object result) {
                if (result == null || result instanceof Exception) {
                    myDialog.dismiss();
                    Toast.makeText(getActivity(), "请求异常", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonResult = new JSONObject(result.toString());
                        JSONArray jsonData = jsonResult.optJSONArray("data");
                        List<CinemaEntity> list = (List<CinemaEntity>) GsonUtils.jsonToList(
                                jsonData.toString(), new TypeToken<List<CinemaEntity>>() {
                                }.getType());
                        if (list.size() > 0) {
                            cinemas.clear();
                            backupList.clear();
                            cinemas.addAll(list);
                            backupList.addAll(list);
                            sortData();
                            saveData(list);
                        } else {
                            myDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDialog.dismiss();
                        Toast.makeText(getActivity(), "解析数据异常", Toast.LENGTH_SHORT).show();
                    } finally {
                        pullToRefresh.onRefreshComplete();
                    }
                }
            }
        });
    }

    /**
     * 对影院按距离远近进行排序
     */
    private void sortData() {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return SortByDistacneUtil.sort(cinemas);
            }

            @Override
            public void result(Object result) {
                myDialog.dismiss();
                if ((Boolean) result) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 保存数据到本地
     */
    private void saveData(final List<CinemaEntity> list) {
        threadHelper.dataHander(new Runnable() {
            @Override
            public void run() {
                cinemaService.add(list, getActivity());
            }
        });
    }

    /**
     * 广播接收器：接收选择城市之后发送的广播
     */
    class RefreshCityRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SupportCityEntity cityEntity = (SupportCityEntity) intent.getSerializableExtra("city");
            tvCity.setText(cityEntity.getNm());
            getDataFormInternet();
        }
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            //注销广播接收器
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
}

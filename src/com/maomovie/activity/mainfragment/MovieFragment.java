package com.maomovie.activity.mainfragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.maomovie.R;
import com.maomovie.activity.ChoiceCityAcvitity;
import com.maomovie.activity.MovieTheaterActivity;
import com.maomovie.activity.playvideo.MovieVideoActivity;
import com.maomovie.activity.moviedetial.MovieDetailActivity;
import com.maomovie.adapter.FragmentMovieAdapter;
import com.maomovie.entity.SupportCityEntity;
import com.maomovie.entity.TodayMovieEntity;
import com.maomovie.presenter.movieFragment.MovieFragBizInterface;
import com.maomovie.presenter.movieFragment.MovieFragBizPresenter;
import com.maomovie.service.LoadingDialog;
import com.maomovie.util.GsonUtils;
import com.maomovie.util.ImageDownloader;
import com.maomovie.util.LoadImageUtil;
import com.maomovie.util.OnImageDownload;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment implements View.OnClickListener , OnScrollListener,FragmentMovieAdapter.Callback,MovieFragView{

    private View view;
    private PullToRefreshListView pullToRefresh;
    private TextView tvCity;//城市名称
    private LinearLayout selectCity;//选择城市的父控件
    private FragmentMovieAdapter adapter;
    private List<TodayMovieEntity> todayMovies = new ArrayList<TodayMovieEntity>();
//    private ImageDownloader mDownloader;
    private RefreshCityRecevier refreshCityRecevier = new RefreshCityRecevier();
    private Dialog myDialog;
    private int pageIndex = 1;//查询页下标
    private int totalPage = -1;//数据总页数
    private boolean hasNext = true;//标记是否还有下一页
    private MovieFragBizInterface movieFragBiz;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_movie, null);
        } else if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViewsInLayout();
        }
        if(movieFragBiz == null){
            movieFragBiz = new MovieFragBizPresenter(this);
        }
        initView();//初始化控件
        //注册广播接收器
        getActivity().registerReceiver(refreshCityRecevier, new IntentFilter("REFRESH_CITY"));
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
            if (todayMovies == null || todayMovies.size() == 0) {
                if(movieFragBiz == null){
                    movieFragBiz = new MovieFragBizPresenter(this);
                }
                //从本地数据库获热播电影数量
                movieFragBiz.queryCountFromDatabase(getActivity());
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
        pullToRefresh = (PullToRefreshListView) view.findViewById(R.id.pullToRefresh);
        adapter = new FragmentMovieAdapter(getActivity(),todayMovies,this);
        pullToRefresh.setAdapter(adapter);
        /*
		 * Mode.BOTH：同时支持上拉下拉
		 * Mode.PULL_FROM_START：只支持下拉Pulling Down
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

        setView();//设置控件的相关属性
    }


    /**
     * 设置控件的相关属性
     */
    private void setView() {
        pullToRefresh.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //这里在Adapter里面使用Glide框架加载图片更加优雅流畅，不再使用以下代码

                //因为pullToRefresh有上拉刷新和下拉刷新，所有可见的item会在头部和尾部各多出一个
                /*for (int i = 1; i <= visibleItemCount && todayMovies.size() > 0; i++) {
                    // 获取到item的电影剧照
                    int position = firstVisibleItem == 0 ? (firstVisibleItem+i-1):(firstVisibleItem+i-2);
                    if (position == totalItemCount - 2)
                        return;//如果用户已经滑动到最后一个item，即pullToRefresh的尾部，表示数据已加载完毕
                    String picUrl = todayMovies.get(position).getImg();
                    ImageView iv_show = (ImageView) view.findViewWithTag(picUrl);
                    Log.i("Tag", "iv_show---" + iv_show);
                    if (mDownloader == null) {
                        mDownloader = new ImageDownloader();
                    }
                    String ivTag = null;
                    if (iv_show != null) {
                        ivTag = (String) iv_show.getTag();
                    }
                    if (ivTag == null) {
                        return;
                    }
                    if (mDownloader != null) {
                        // 异步下载图片
                        mDownloader.imageDownload(picUrl, iv_show, "/maomovie/cache/movieImg/", getActivity(),
                                new OnImageDownload() {
                                    @Override
                                    public void onDownloadSucc(Bitmap bitmap, String c_url, ImageView mimageView) {
                                        LoadImageUtil.setImageByImageView(mimageView, bitmap);
                                    }
                                });
                    }
                }*/
            }
        });
    }

    /**
     * 设置下拉刷新样式
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
        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新事件
                pageIndex = 1;
                todayMovies.clear();
                movieFragBiz.getDataFromInternet(pageIndex);//从网络上获取数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多事件：
                if (totalPage >= pageIndex) {//1.如果数据库中还有下一页数据，从数据库中加载；
                    movieFragBiz.loadMoreFromDatabase(getActivity(),pageIndex);
                } else {//2.否则从网络上加载更多数据
                    movieFragBiz.getDataFromInternet(pageIndex);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == selectCity){
            //跳转到城市列表界面
            Intent intent = new Intent(getActivity(), ChoiceCityAcvitity.class);
            startActivity(intent);
        }
    }

    /**
     * 这是FragmentMovieAdapter点击【购票】按钮的回调方法
     * @param v
     * @param position
     */
    @Override
    public void click(View v, int position) {
        TodayMovieEntity movieEntity = todayMovies.get(position);
        Bitmap bitmap = null;
        Intent intent = new Intent();
        intent.putExtra("movieId",movieEntity.getId());
        intent.putExtra("movieName",movieEntity.getNm());
        switch (v.getId()){
            case R.id.btn_BuyTicket://购票
                intent.setClass(getActivity(), MovieTheaterActivity.class);
                startActivity(intent);
                break;
            case R.id.moviePhoto://预告片
                intent.putExtra("sc",movieEntity.getSc());
                intent.putExtra("rt",movieEntity.getRt());
                v.setDrawingCacheEnabled(true);
                bitmap = v.getDrawingCache();
                intent.putExtra("img",bitmap);
                intent.setClass(getActivity(), MovieVideoActivity.class);
                startActivity(intent);
                v.setDrawingCacheEnabled(false);//清空画图缓冲区
                break;
            case R.id.item_movie_lay:
                View imageView = v.findViewById(R.id.moviePhoto);
                imageView.setDrawingCacheEnabled(true);
                bitmap = imageView.getDrawingCache();
                intent.putExtra("img",bitmap);
                intent.setClass(getActivity(),MovieDetailActivity.class);
                startActivity(intent);
                imageView.setDrawingCacheEnabled(false);//清空画图缓冲区
                break;
        }
    }

    /**
     * 设置静读条是否可见
     * @param visibile
     */
    @Override
    public void setProgressBarVisibile(int visibile) {
        if(visibile == View.VISIBLE){
            myDialog = LoadingDialog.createLoadingDialog(getActivity());
            myDialog.show();
        } else {
            myDialog.dismiss();
        }
    }

    /**
     * 通过数据库查询电影信息的结果
     * @param code  200正常，201加载更多正常，400一般失败，401加载更多失败
     * @param result
     */
    @Override
    public void queryMovieDatabaseResult(int code, Object result) {
        switch (code){
            case 200:
                try {
                    List<TodayMovieEntity> list = (List<TodayMovieEntity>) result;
                    todayMovies.addAll(list);
                    if (todayMovies.size() == 0) {//如果从数据库获取到的电影数量为0，那么从服务器获取电影信息
                        movieFragBiz.getDataFromInternet(pageIndex);
                        return;
                    }
                    pageIndex++;
                    setProgressBarVisibile(View.GONE);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    setProgressBarVisibile(View.GONE);
                    Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                }
                break;
            case 201://加载更多电影信息成功
                pageIndex++;
                List<TodayMovieEntity> list = (List<TodayMovieEntity>) result;
                if (list.size() > 0) {//如果加载到更多电影信息
                    todayMovies.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    pullToRefresh.setPullToRefreshOverScrollEnabled(false);
                }
                pullToRefresh.onRefreshComplete();
                break;
            case 400://从数据库查询第一页电影信息失败
            case 401://从数据库加载电影信息失败
                movieFragBiz.getDataFromInternet(pageIndex);
                break;
        }
    }

    /**
     * 通过服务器查询电影信息结果
     * @param code  200正常，400失败
     * @param result
     */
    @Override
    public void queryMovieInternetResult(int code, Object result) {
        if(code == 400){//查询失败
            setProgressBarVisibile(View.GONE);
            pullToRefresh.onRefreshComplete();
            Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_LONG).show();
        } else if (code == 200){//查询成功
            if (!hasNext){//如果没有下一页了
                myDialog.dismiss();
                pullToRefresh.onRefreshComplete();
            }
            try {
//				saveMovieInfoToFile(result.toString(),getActivity().getFilesDir() + "/data","movie-info.txt");
                JSONObject jsonResult = new JSONObject(result.toString());
                int status = jsonResult.optInt("status");
                if (status == 0) {//表示请求成功
                    pageIndex++;
                    //逐层解析json数据
                    JSONObject jsonData = jsonResult.optJSONObject("data");
                    hasNext = jsonData.optBoolean("hasNext");
                    JSONArray jsonMovies = jsonData.optJSONArray("movies");
                    List<TodayMovieEntity> localList = (List<TodayMovieEntity>) GsonUtils.jsonToList(jsonMovies.toString(),
                            new TypeToken<List<TodayMovieEntity>>() {
                            }.getType());
                    todayMovies.addAll(localList);
                    myDialog.dismiss();
                    adapter.notifyDataSetChanged();
                    movieFragBiz.saveToDatabase(getActivity(),localList);
                } else {
                    myDialog.dismiss();
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                myDialog.dismiss();
                Toast.makeText(getActivity(), "解析数据失败", Toast.LENGTH_SHORT).show();
            }finally {
                pullToRefresh.onRefreshComplete();
            }
        }
    }

    /**
     * 通过数据库查询电影数量
     * @param code  200成功，400失败
     * @param total 电影总数量
     */
    @Override
    public void queryMovieCountResult(int code, int total) {
        if(code == 200){
            totalPage = total % 20 == 0 ? (total / 20) : ((total / 20) + 1);
            movieFragBiz.queryDataFromDatabase(getActivity(),pageIndex);
        } else {
            movieFragBiz.getDataFromInternet(pageIndex);
        }
    }
    
    /**
     * 将电影信息保存到文件中
     * @param movieInfo	电影信息
     * @param filePath	文件目录
     * @param fileName	文件名称
     * @throws IOException
     */
    private void saveMovieInfoToFile(String movieInfo,String filePath,String fileName) throws IOException{
    	if(movieInfo == null || filePath == null || fileName == null) return;
    	File path = new File(filePath);
    	if(!path.exists()){//如果目录不存在，创建目录
    		boolean flag = path.mkdirs();
    		Log.i("TAG", "mkdirs " + filePath + flag);
    	}
    	//根据目录和文件名称创建File对象
    	File file = new File(path, fileName);
		PrintWriter writer = new PrintWriter(file,"utf-8");
		char[] buf = movieInfo.toCharArray();
		writer.write(buf );
		writer.close();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
            case SCROLL_STATE_FLING:
                //Log.i("Main","用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                Glide.with(this).pauseRequests();
                //刷新
                break;
            case SCROLL_STATE_IDLE:
                // Log.i("Main", "视图已经停止滑动");
                Glide.with(this).resumeRequests();
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                // Log.i("Main","手指没有离开屏幕，视图正在滑动");
                Glide.with(this).resumeRequests();
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * 广播接收器：接收选择城市之后发送的广播
     */
    class RefreshCityRecevier extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            SupportCityEntity cityEntity = (SupportCityEntity) intent.getSerializableExtra("city");
            tvCity.setText(cityEntity.getName());
        }
    }


    @Override
    public void onDestroy() {
        if(refreshCityRecevier != null){
            //注销广播接收器
            getActivity().unregisterReceiver(refreshCityRecevier);
            refreshCityRecevier = null;
        }
        movieFragBiz.onDestory();
        super.onDestroy();
    }
}

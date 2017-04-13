package com.maomovie.activity.playvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.maomovie.R;
import com.maomovie.activity.GalleryActivity;
import com.maomovie.adapter.CommentAdapter;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.util.*;
import com.maomovie.view.MediaController;
import com.maomovie.view.SuperVideoPlayer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yanpeng on 2016/12/19.
 * 电影短片
 */
public class MovieVideoActivity extends Activity implements View.OnClickListener,AbsListView.OnScrollListener, CommentAdapter.ClickImageCallBack {
    private Context context;
    private ThreadHelper threadHelper = new ThreadHelper(new Handler());
    private TextView tvErrorMsg;
    private String videoUrl;//视频播放地址
    private ImageView ivPhoto;  //电影海报
    private TextView tvNm,tvRt,tvSc;//电影名称、上映时间、分数的文本显示器
    private TextView tvCmsTotal1,tvCmsTotal2;//显示评论总数量(分别对应layCommentTotal，headerView2)
    private LinearLayout  layCommentTotal;  //评论总条数父控件
    private RelativeLayout layProgress;//等待框父控件
    private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;

    private PullToRefreshListView pullToRefresh;
    private CommentAdapter listAdaper;
    private JSONArray commentJsonArray;//观众评论数据；
    private String movieName;   //电影名称
    private int offset = 1,limit=50,totalPage=1;//查询评论的下标,单页条数,总页数
    private int savePlayTime = 0;//用于保存页面跳转时的播放进度

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_video);
        context = this;
        initView();//初始化控件
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //播放器初始化
        mSuperVideoPlayer = (SuperVideoPlayer) findViewById(R.id.video_player_item_1);
        mPlayBtnView = findViewById(R.id.play_btn);
        mPlayBtnView.setOnClickListener(this);
        mSuperVideoPlayer.setVideoPlayCallback(mVideoPlayCallback);

        layProgress = (RelativeLayout) findViewById(R.id.layProgress);
        int movieId = getIntent().getIntExtra("movieId", 0);            //电影id
        tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
        tvCmsTotal1 = (TextView) findViewById(R.id.tvCommentTotal);
        String commentData = getIntent().getStringExtra("commentData");
        try {
            if(commentData != null){
                commentJsonArray = new JSONArray(commentData);
            }else {
                commentJsonArray = new JSONArray();
                getMoreData(movieId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //头部内容1,电影名称及购票
        View headerView1 = View.inflate(context,R.layout.item_video_header1,null);
        //头部内容2，评论条数
        View headerView2 = View.inflate(context,R.layout.item_video_header2,null);
        tvCmsTotal2 = (TextView) headerView2.findViewById(R.id.tvCommentTotal2);
        ivPhoto = (ImageView) headerView1.findViewById(R.id.moviePhoto);
        tvNm = (TextView) headerView1.findViewById(R.id.title);
        tvRt = (TextView) headerView1.findViewById(R.id.tvRt);
        tvSc = (TextView) headerView1.findViewById(R.id.tvSc);
        movieName = getIntent().getStringExtra("movieName");     //电影名称
        tvRt.setText(getIntent().getStringExtra("rt"));
        int score = getIntent().getIntExtra("sc", 0);
        tvSc.setText(score==0 ? "未评" : String.valueOf(score));
        tvNm.setText(movieName);
        Bitmap movieBitmap = getIntent().getParcelableExtra("img");
        ivPhoto.setImageBitmap(movieBitmap);
        pullToRefresh = (PullToRefreshListView) findViewById(R.id.pullToRefresh);
        //添加 第一个头部 （电影名称、上映时间、购票）
        pullToRefresh.getRefreshableView().addHeaderView(headerView1,null,false);
        //添加 第二个头部 （评论条数）
        pullToRefresh.getRefreshableView().addHeaderView(headerView2,null,false);
        listAdaper = new CommentAdapter(context, commentJsonArray,this);
        pullToRefresh.setAdapter(listAdaper);
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
        pullToRefresh.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        initPullToRefresh(movieId);
        layCommentTotal = (LinearLayout) findViewById(R.id.layCommentTotal);
        if(movieId == 0){//如果没有接收到影片id
            tvErrorMsg.setVisibility(View.VISIBLE);
        } else {//网络请求电影的预告片资源
            getHtml(movieId);
        }
    }

    //初始化pullToResfresh样式
    private void initPullToRefresh(final int movieId){
        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false, true);
        Drawable pull_progress = context.getResources().getDrawable(R.drawable.bg_pull_process);
        endLabels.setLoadingDrawable(pull_progress);
        endLabels.setPullLabel("上拉加载更多评论");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        pullToRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (offset > totalPage) {//如果评论已加载到最后一页
                    pullToRefresh.onRefreshComplete();
                    return;
                }
                getMoreData(movieId);
            }
        });
    }

    /**
     * 获取更多评论
     * @param movieId
     */
    private void getMoreData(final int movieId) {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                try {
                    //获取更多评论
                    return HttpUtil.getCommentsJson(movieId, limit, offset++);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void result(Object result) {
                layProgress.setVisibility(View.GONE);
                pullToRefresh.onRefreshComplete();
                if(result == null || result instanceof Exception){
                    tvCmsTotal1.setText("获取更多评论失败");
                    return;
                }
                try {
                    //解析json
                    JSONObject jsonResult = new JSONObject(result.toString());
                    jsonResult = jsonResult.optJSONObject("data").getJSONObject("CommentResponseModel");
                    int total = jsonResult.optInt("total");
                    tvCmsTotal1.setText("评论(" + total + "）");
                    tvCmsTotal2.setText("评论(" + total + "）");
                    totalPage = total%limit == 0 ? total/limit : (total/limit+1);
                    JSONArray jsonArray = jsonResult.optJSONArray("cmts");
                    //将评论逐条添加到数据源
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        commentJsonArray.put(jsonObject);
                    }
                    listAdaper.notifyDataSetChanged();
                    pullToRefresh.setOnScrollListener(MovieVideoActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void playVideo(){
        try {
            mPlayBtnView.setVisibility(View.GONE);
            mSuperVideoPlayer.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setTvMovieName("即将播放：" + movieName);
            mSuperVideoPlayer.setAutoHideController(false);
            Uri uri = Uri.parse(videoUrl);
            mSuperVideoPlayer.loadAndPlay(uri,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络请求电影预告片的Url
     */
    private void getHtml(final int movieId) {
        mPlayBtnView.setVisibility(View.GONE);
        mSuperVideoPlayer.setVisibility(View.VISIBLE);
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return HttpUtil.getMovieVideoUrl(movieId);
            }

            @Override
            public void result(Object result) {
                if (result == null) {
                    tvErrorMsg.setVisibility(View.VISIBLE);
                } else {
                    tvErrorMsg.setVisibility(View.GONE);
                    videoUrl = result.toString();
                    playVideo();
                }
            }
        });
    }

    /**
     * 播放器的回调函数
     */
    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        /**
         * 播放器关闭按钮回调
         */
        @Override
        public void onCloseVideo() {
            resetPageToPortrait();
        }

        /**
         * 播放器横竖屏切换回调
         */
        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mSuperVideoPlayer.setPageType(MediaController.PageType.EXPAND);
            }
        }

        /**
         * 播放完成回调
         */
        @Override
        public void onPlayFinish() {
            mPlayBtnView.setVisibility(View.VISIBLE);
        }
    };

    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == mSuperVideoPlayer) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = context.getResources().getDisplayMetrics().widthPixels;
            float width = context.getResources().getDisplayMetrics().heightPixels;
            mSuperVideoPlayer.getLayoutParams().height = (int) width;
            mSuperVideoPlayer.getLayoutParams().width = (int) height;
            mSuperVideoPlayer.showOrHideController();//隐藏控制器
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = context.getResources().getDisplayMetrics().widthPixels;
            float height = DisplayUtil.dip2px(this, 200.f);
            mSuperVideoPlayer.getLayoutParams().height = (int) height;
            mSuperVideoPlayer.getLayoutParams().width = (int) width;
        }
    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mSuperVideoPlayer.setPageType(MediaController.PageType.SHRINK);
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mPlayBtnView){
            playVideo();
        }
    }

    @Override
    public void onBackPressed() {
        resetPageToPortrait();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
            case SCROLL_STATE_FLING:
                //Log.i("Main","用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                Glide.with(getApplicationContext()).pauseRequests();
                //刷新
                break;
            case SCROLL_STATE_IDLE:
               // Log.i("Main", "视图已经停止滑动");
                Glide.with(getApplicationContext()).resumeRequests();
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
               // Log.i("Main","手指没有离开屏幕，视图正在滑动");
                Glide.with(getApplicationContext()).resumeRequests();
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem >=2){//显示ListView头部悬浮的位置
            layCommentTotal.setVisibility(View.VISIBLE);
        } else {
            layCommentTotal.setVisibility(View.GONE);
        }
    }

    /**
     * 获取Listview滚动的距离
     * @param mListView
     * @return
     */
    private int listViewScorllY(AbsListView mListView){
        View view = mListView.getChildAt(0);
        if (view== null) {
            return 0;
        }
        int firstVisiblePosition = mListView.getFirstVisiblePosition()-1;
        int top = view.getTop();
        return -top + firstVisiblePosition * view.getHeight() ;
    }

    @Override
    public void clickImage(String avatarUrl) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra("imgUrls", new String[]{avatarUrl});
        intent.putExtra("currentIndex", 0);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        savePlayTime = mSuperVideoPlayer.getCurrentPosition();
        mSuperVideoPlayer.pausePlay(false);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(savePlayTime != 0){
            mSuperVideoPlayer.startPlayVideo(savePlayTime);
        }
    }
}
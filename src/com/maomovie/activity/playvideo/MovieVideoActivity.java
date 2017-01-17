package com.maomovie.activity.playvideo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.maomovie.R;
import com.maomovie.activity.BaseActivity;
import com.maomovie.adapter.CommentAdapter;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.util.DisplayUtil;
import com.maomovie.util.HttpUtil;
import com.maomovie.view.MediaController;
import com.maomovie.view.SuperVideoPlayer;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by yanpeng on 2016/12/19.
 * 电影短片
 */
public class MovieVideoActivity extends BaseActivity implements View.OnClickListener{
    private Context context;
    private ThreadHelper threadHelper = new ThreadHelper(new Handler());
    private TextView tvErrorMsg;
    private String videoUrl;//视频播放地址
    private ImageView ivPhoto;  //电影海报
    private TextView tvNm,tvRt,tvSc;//电影名称、上映时间、分数的文本显示器

    private SuperVideoPlayer mSuperVideoPlayer;
    private View mPlayBtnView;

    private PullToRefreshListView pullToRefresh;
    private CommentAdapter listAdaper;
    private JSONArray commentJsonArray;//观众评论数据；
    private String movieName;   //电影名称

    @Override
    public void onCreate() {
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
        ivPhoto = (ImageView) findViewById(R.id.moviePhoto);
        tvNm = (TextView) findViewById(R.id.title);
        tvRt = (TextView) findViewById(R.id.tvRt);
        tvSc = (TextView) findViewById(R.id.tvSc);
        int movieId = getIntent().getIntExtra("movieId", 0);            //电影id
        movieName = getIntent().getStringExtra("movieName");     //电影名称
        tvRt.setText(getIntent().getStringExtra("rt"));
        tvSc.setText(String.valueOf(getIntent().getIntExtra("sc", 0)));
        tvNm.setText(movieName);
        Bitmap movieBitmap = getIntent().getParcelableExtra("img");
        ivPhoto.setImageBitmap(movieBitmap);
        String commentData = getIntent().getStringExtra("commentData");
        try {
            if(commentData != null){
                commentJsonArray = new JSONArray(commentData);
            }else {
                commentJsonArray = new JSONArray();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
        pullToRefresh = (PullToRefreshListView) findViewById(R.id.pullToRefresh);
        listAdaper = new CommentAdapter(context, commentJsonArray);
        pullToRefresh.setAdapter(listAdaper);
        if(movieId == 0){//如果没有接收到影片id
            tvErrorMsg.setVisibility(View.VISIBLE);
        } else {//网络请求电影的预告片资源
            getHtml(movieId);
        }
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
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return HttpUtil.getMovieVideoUrl(movieId);
            }

            @Override
            public void result(Object result) {
                if(result == null){
                    tvErrorMsg.setVisibility(View.VISIBLE);
                } else {
                    listAdaper.notifyDataSetChanged();
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
            finish();
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
}
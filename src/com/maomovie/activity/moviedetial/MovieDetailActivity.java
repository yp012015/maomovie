package com.maomovie.activity.moviedetial;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.maomovie.R;
import com.maomovie.activity.GalleryActivity;
import com.maomovie.activity.login.LoginActivity;
import com.maomovie.activity.playvideo.MovieVideoActivity;
import com.maomovie.entity.TodayMovieEntity;
import com.maomovie.service.LoadingDialog;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.sqlite.service.TodayMovieService;
import com.maomovie.util.DisplayUtil;
import com.maomovie.util.HttpUtil;
import com.maomovie.util.ToastUtil;
import com.maomovie.view.ObservableScrollView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yanpeng on 2017/1/10.
 * 业务功能：展示电影详情
 */
public class MovieDetailActivity extends Activity implements View.OnClickListener,ObservableScrollView.ScrollViewListener{
    private Context context;
    private ObservableScrollView scrollView;//自定义的滚动控件
    private LinearLayout layGradient;//渐变色区域的父控件
    private ImageView imageView ;   //电影海报
    private RelativeLayout titleBar;//顶部导航栏父控件
    private ImageButton btnPlay;//播放按钮
    private int videoHeight;//电影详情上半部分的高度
    private ImageButton btnBack;
    private TextView tvTitle;//顶部导航栏标题
    private TextView tvName_CN;                 //中文名称
    private TextView tvName_EN;                 //英文名称
    private TextView tvScore_Viewer;            //观众评分
    private TextView tvScore_Professional;      //专业评分
    private TextView tvMovieType;               //电影类型
    private TextView tvMoviePlace;              //电影产地
    private TextView tvMovieDate;               //上映时间
    private TextView tvDescription;             //电影介绍
    private TextView tvMovieDir;                //导演
    private TextView tvMovieStar;               //演员
    private TextView[] tvCommentators;          //评论者
    private TextView[] tvComments;              //评论语
    private ImageView[] ivPhotos;               //评论者头像
    private RatingBar[] ratingBars;             //电影评分
    private TodayMovieEntity entity;
    private Dialog loadingDialog;
    private ThreadHelper threadHelper = new ThreadHelper(new Handler());
    private JSONObject jsonResult;          //电影描述及评分的Json
    private JSONArray commentJsonArray;
    private Bitmap bitmap;                  //电影海报
    private String[] avatarUrls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //只对api19以上版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        context = this;
        getLocalData();
    }

    /**
     * 设置沉侵式状态栏
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

    /**初始化控件*/
    private void initView() {
        setContentView(R.layout.activity_movie_detail);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        titleBar = (RelativeLayout) findViewById(R.id.titleBar);
        scrollView = (ObservableScrollView) findViewById(R.id.myScrollView);
        layGradient = (LinearLayout) findViewById(R.id.layGradient);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvName_CN = (TextView)findViewById(R.id.tvMovieName_CN);
        tvName_EN = (TextView)findViewById(R.id.tvMovieName_EN);
        tvScore_Viewer = (TextView)findViewById(R.id.tvScore_viewer);
        tvScore_Professional = (TextView)findViewById(R.id.tvScore_Professional);
        tvMovieType = (TextView)findViewById(R.id.tvMovieType);
        tvMoviePlace = (TextView)findViewById(R.id.tvMoviePlace);
        tvMovieDate = (TextView)findViewById(R.id.tvMovieDate);
        tvDescription = (TextView)findViewById(R.id.tvMovieDescription);
        tvMovieDir = (TextView) findViewById(R.id.tvMovieDir);
        tvMovieStar = (TextView) findViewById(R.id.tvMovieStar);
        tvCommentators = new TextView[5];
        tvComments = new TextView[5];
        ivPhotos = new ImageView[5];
        ratingBars = new RatingBar[5];
        tvCommentators[0] = (TextView) findViewById(R.id.tvCommentator1);
        tvCommentators[1] = (TextView) findViewById(R.id.tvCommentator2);
        tvCommentators[2] = (TextView) findViewById(R.id.tvCommentator3);
        tvCommentators[3] = (TextView) findViewById(R.id.tvCommentator4);
        tvCommentators[4] = (TextView) findViewById(R.id.tvCommentator5);
        tvComments[0] = (TextView) findViewById(R.id.tvComment1);
        tvComments[1] = (TextView) findViewById(R.id.tvComment2);
        tvComments[2] = (TextView) findViewById(R.id.tvComment3);
        tvComments[3] = (TextView) findViewById(R.id.tvComment4);
        tvComments[4] = (TextView) findViewById(R.id.tvComment5);
        ivPhotos[0] = (ImageView) findViewById(R.id.ivPhoto1);
        ivPhotos[1] = (ImageView) findViewById(R.id.ivPhoto2);
        ivPhotos[2] = (ImageView) findViewById(R.id.ivPhoto3);
        ivPhotos[3] = (ImageView) findViewById(R.id.ivPhoto4);
        ivPhotos[4] = (ImageView) findViewById(R.id.ivPhoto5);
        ratingBars[0] = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBars[1] = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBars[2] = (RatingBar) findViewById(R.id.ratingBar3);
        ratingBars[3] = (RatingBar) findViewById(R.id.ratingBar4);
        ratingBars[4] = (RatingBar) findViewById(R.id.ratingBar5);

        imageView = (ImageView) findViewById(R.id.imageView1);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);

        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = layGradient.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layGradient.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                videoHeight = layGradient.getHeight() - titleBar.getHeight();
                scrollView.setScrollViewListener(MovieDetailActivity.this);
            }
        });
        //如果android系统为4.4以下，不具备沉侵式状态栏功能
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //设置顶部导航栏的高度
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context,50));
            titleBar.setLayoutParams(params1);
            //设置返回按钮的布局参数
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    DisplayUtil.dip2px(context,50), ViewGroup.LayoutParams.MATCH_PARENT);
            params2.setMargins(0, 0, 0, 0);
            btnBack.setLayoutParams(params2);
            //设置灰色渐变区域的margin
            layGradient.setPadding(DisplayUtil.dip2px(context,10),DisplayUtil.dip2px(context,50),0,DisplayUtil.dip2px(context,15));
        }

        setView();
    }

    /**
     * 根据电影id从数据库查询电影详情
     */
    private void getLocalData(){
        //获取电影id
        final int id = getIntent().getIntExtra("movieId", 0);
        if(id == 0){
            return;
        }
        bitmap = getIntent().getParcelableExtra("img");
        loadingDialog = LoadingDialog.createLoadingDialog(context);
        loadingDialog.show();
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return new TodayMovieService().queryById(id, context);
            }

            @Override
            public void result(Object result) {
                if (result == null || result instanceof Exception) {
                    loadingDialog.dismiss();
                    ToastUtil.show(context, "电影详情加载失败");
                } else {//电影id是唯一的，所以只能查询到一个实体，直接取出第一个数据即可
                    List<TodayMovieEntity> dataList = (List<TodayMovieEntity>) result;
                    entity = dataList.get(0);
                    getNetData();
                }
            }
        });
    }

    /**
     * 从网络查出电影详情(包含评论)
     */
    private void getNetData() {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                try {
                    Object result = HttpUtil.getMovieDetailById(entity.getId());
                    if (result == null || result instanceof Exception) {
                        return null;
                    }
                    /**去除字符串中的回车、制表符、空格、换行
                     注：
                     \n 回车(\u000a)
                     \t 水平制表符(\u0009)
                     \s 空格(\u0008)
                     \r 换行(\u000d)*/
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                    Matcher m = p.matcher(result.toString());
                    result = m.replaceAll("");
                    jsonResult = new JSONObject(result.toString());
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void result(Object result) {
                loadingDialog.dismiss();
                initView();
            }
        });
    }

    /**为控件赋值*/
    private void setView() {
        tvTitle.setText(entity.getNm());
        tvName_CN.setText(entity.getNm());
        tvName_EN.setText(entity.getScm());
        int wish = entity.getPreSale();
        String score = wish==1 ? "预售" : String.valueOf(entity.getSc());
        tvScore_Viewer.setText(score);
        tvScore_Professional.setText(String.valueOf(entity.getSnum()));
        tvMovieType.setText(entity.getCat());
        String src = entity.getSrc().equals("") ? "中国大陆" : entity.getSrc();
        src += "/ " + entity.getDur() + "分钟";
        tvMoviePlace.setText(src);
        tvMovieDate.setText(entity.getRt());
        imageView.setImageBitmap(bitmap);
        tvMovieDir.setText(entity.getDir());
        tvMovieStar.setText(entity.getStar());
        String description = "获取电影介绍失败";
        if (jsonResult != null) {//设置电影介绍及观众评论
            try {
                description = jsonResult.optJSONObject("data").optJSONObject("MovieDetailModel").optString("dra");
                description = description.replace("<p>", "").replace("</p>", "");
                JSONObject commentJsonObj = jsonResult.optJSONObject("data").optJSONObject("CommentResponseModel");
                TextView tvCommentTotal = (TextView) findViewById(R.id.tvCommentTotal);//评论总条数
                tvCommentTotal.setText("查看全部" + commentJsonObj.optInt("total") + "条评论");
                commentJsonArray = commentJsonObj.optJSONArray("cmts");
                avatarUrls = new String[commentJsonArray.length()];
                for(int i=0; i<commentJsonArray.length(); i++){
                    commentJsonObj = commentJsonArray.optJSONObject(i);
                    tvCommentators[i].setText(commentJsonObj.optString("nickName"));
                    tvComments[i].setText(commentJsonObj.optString("content"));
                    ratingBars[i].setRating((float) commentJsonObj.optDouble("score"));
                    avatarUrls[i] = commentJsonObj.optString("avatarurl");
                }
                setAvatar(avatarUrls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tvDescription.setText(description);
    }

    /**
     * 设置评论者的头像，由于头像是网络资源，需要在子线程中获取drawable对象
     * @param urlStrs
     */
    private void setAvatar(final String[] urlStrs){
        for(int i=0; i<ivPhotos.length; i++){
            ivPhotos[i].setOnClickListener(this);
            if(urlStrs[i].contains("avatar") && urlStrs[i].endsWith("__40465654__9539763.png"))
                continue;
            Glide.with(context).load(urlStrs[i]).thumbnail(0.1f)
                    .placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(ivPhotos[i]);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnBack){
            finish();
        } else if (v == btnPlay){
            Intent intent = new Intent(context, MovieVideoActivity.class);
            intent.putExtra("movieId",entity.getId());
            intent.putExtra("movieName",entity.getNm());
            intent.putExtra("sc",entity.getSc());
            intent.putExtra("rt",entity.getRt());
            intent.putExtra("img",bitmap);
//            if (commentJsonArray != null) {
//                intent.putExtra("commentData", commentJsonArray.toString());
//            }
            startActivity(intent);
        } else if(v.getId() == R.id.btnWish){//想看
            ToastUtil.show(context,"共有" + entity.getWish() + "想看这部电影！");
        } else if(v.getId() == R.id.btnScore){
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        } else {
            int currentIndex = 1;
            switch (v.getId()){
                case R.id.ivPhoto1:
                    currentIndex = 0;
                    break;
                case R.id.ivPhoto2:
                    currentIndex = 1;
                    break;
                case R.id.ivPhoto3:
                    currentIndex = 2;
                    break;
                case R.id.ivPhoto4:
                    currentIndex = 3;
                    break;
                case R.id.ivPhoto5:
                    currentIndex = 4;
                    break;
            }
            Intent intent = new Intent(context, GalleryActivity.class);
            intent.putExtra("imgUrls", avatarUrls);
            intent.putExtra("currentIndex",currentIndex);
            startActivity(intent);
        }
    }

    /**
     * 监听滚动状态，实现顶部导航栏的颜色渐变效果
     */
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y < videoHeight/2) {
            titleBar.setBackgroundColor(Color.argb(0, 212, 62, 55));//AGB由相关工具获得，或者美工提供
            tvTitle.setTextColor(Color.argb(0, 255, 255, 255));
        } else if (y >= videoHeight/2 && y <= videoHeight) {
            float scale = (float) (y-videoHeight/2) / (videoHeight/2);
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            titleBar.setBackgroundColor(Color.argb((int) alpha, 212, 62, 55));
            tvTitle.setTextColor(Color.argb((int) alpha, 255, 255, 255));
        } else{
            titleBar.setBackgroundColor(Color.rgb(212, 62, 55));
            tvTitle.setTextColor(Color.rgb(255, 255, 255));
        }
    }
}
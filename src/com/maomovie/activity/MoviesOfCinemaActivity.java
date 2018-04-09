package com.maomovie.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.maomovie.R;
import com.maomovie.activity.route.ShowMapActivity;
import com.maomovie.adapter.HorizontalListViewAdapter;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.entity.CurrentMoviesEntity;
import com.maomovie.presenter.cinemamovie.CinemaMovieBizInterface;
import com.maomovie.presenter.cinemamovie.CinemaMovieViewInterface;
import com.maomovie.presenter.cinemamovie.CinemaMoviesBizImp;
import com.maomovie.view.HorizontalListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YanP on 2016/8/10.
 * 影院上映影片信息界面
 */
public class MoviesOfCinemaActivity extends BaseActivity implements CinemaMovieViewInterface, View.OnClickListener {
    private ImageButton btnBack;     //返回键
    private TextView tvTitle;   //标题
    private TextView tvCinameName,tvCinemaAddr;//影院名称、地址
    private LinearLayout layShowMap;//地图按钮
    private TextView tvTrafficRoutes;//交通线路
    private TextView tvMsg;
    private TextView[] tvDates = new TextView[2];
    private TextView tvMovieName;//电影名称
    private TextView tvScore;//电影评分
    private TextView tvDuration;//时长
    private LinearLayout laySecond;
    private ScrollView scrollView;
    private CurrentMoviesEntity movieEntity;
    private CinemaEntity cinemaEntity;
    private Context context;
    private HorizontalListView hListView;
    private HorizontalListViewAdapter hListViewAdapter;
    private LinearLayout layMovieTime;
    private View olderSelectView = null;
    private CinemaMovieBizInterface bizInterface;
    private List<CurrentMoviesEntity.CurrentMovieBean> movieList = new LinkedList<>();
    private LayoutInflater mInflater;

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_moviesofcinema);
        mInflater = LayoutInflater.from(this);
        bizInterface = new CinemaMoviesBizImp(this);
        //接收上一界面传递过来的数据实体
        movieEntity = (CurrentMoviesEntity) getIntent().getSerializableExtra("CurrentMoviesEntity");
        cinemaEntity = (CinemaEntity) getIntent().getSerializableExtra("CinemaEntity");
        findView();//初始化控件
        bizInterface.loadMoviesOfCinema(cinemaEntity.getId(), -1);
        setView();//为控件赋值
    }

    /**
     * 初始化控件
     */
    private void findView() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        layMovieTime = (LinearLayout) findViewById(R.id.layMovieTime);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvCinameName = (TextView) findViewById(R.id.tvCinemaName);
        tvCinemaAddr = (TextView) findViewById(R.id.tvCinemaAddr);
        tvMsg = (TextView) findViewById(R.id.tvMsg);

        tvMovieName = (TextView) findViewById(R.id.tvMovieName);
        tvScore= (TextView) findViewById(R.id.tvScore);
        tvDuration= (TextView) findViewById(R.id.tvDuration);

        tvDates[0] = (TextView) findViewById(R.id.tvFirstDate);
        tvDates[1] = (TextView) findViewById(R.id.tvSecondDate);
        laySecond = (LinearLayout) findViewById(R.id.laySecondDate);

        layShowMap = (LinearLayout) findViewById(R.id.layShowMap);
        layShowMap.setOnClickListener(this);

        //显示公交线路
        tvTrafficRoutes = (TextView) findViewById(R.id.tvCinemaTransfer);

        hListView = (HorizontalListView) findViewById(R.id.horizon_listview);
        hListViewAdapter = new HorizontalListViewAdapter(getApplicationContext(), movieList);
        hListView.setAdapter(hListViewAdapter);
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                // TODO Auto-generated method stub
//              if(olderSelectView == null){
//                  olderSelectView = view;
//              }else{
//                  olderSelectView.setSelected(false);
//                  olderSelectView = null;
//              }
//              olderSelectView = view;
//              view.setSelected(true);
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();
                tvMovieName.setText(movieList.get(position).getNm());
                tvScore.setText(movieList.get(position).getSc() + "分");
            }
        });
    }

    /**
     * 为控件赋值
     */
    private void setView(){
        context = this;
        if(cinemaEntity == null)
            return;
        String name = cinemaEntity.getNm();
        tvTitle.setText(name);
        tvCinameName.setText(name);
        String address = cinemaEntity.getAddr();
        tvCinemaAddr.setText(address.replace("成都市", ""));
        String transferLine = cinemaEntity.getBus();
        tvTrafficRoutes.setText(transferLine);
    }

    /**
     * 显示影院上映的电影信息
     * @param movieBeans
     */
    public void showMoviesOfCinema(List<CurrentMoviesEntity.CurrentMovieBean> movieBeans){
        if(movieList == null ) return;
        tvMsg.setVisibility(View.GONE);
        this.movieList.clear();
        this.movieList.addAll(movieBeans);
        hListViewAdapter.notifyDataSetChanged();
        if (movieBeans.size() > 0) {
            CurrentMoviesEntity.CurrentMovieBean movieBean = movieBeans.get(0);
            tvMovieName.setText(movieBean.getNm());
            tvScore.setText(movieBean.getSc() + "分");
        }
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayDateShow(JSONObject dateObj) {
        Iterator iterator = dateObj.keys();
        int index = 0;
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (index == 1) {
                JSONArray jsonArray = dateObj.optJSONArray(key);
                updateDateInfo(jsonArray);
            }
            tvDates[index++].setText(key);
            if(index>2){//只显示最近两天的场次信息
                break;
            }
        }
        if (index<2){//没有第二天的电影场次信息
            laySecond.setVisibility(View.GONE);
        }
    }

    /**
     * 显示电影场次信息
     * @param dateArray
     */
    public void updateDateInfo(JSONArray dateArray) {
        if (dateArray != null && dateArray.length() > 0) {
            for (int i = 0; i < dateArray.length(); i++) {
                JSONObject obj = dateArray.optJSONObject(i);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); // , 1是可选写的
                RelativeLayout llyout = (RelativeLayout) mInflater.from(context).inflate(R.layout.movie_time_item, null);
                TextView tvStartTime = (TextView) llyout.findViewById(R.id.startTime); // 开始时间
                TextView tvEndTime = (TextView) llyout.findViewById(R.id.endTime); // 结束时间
                TextView tvLanguage = (TextView) llyout.findViewById(R.id.language); //影片语言
                TextView tvHall = (TextView) llyout.findViewById(R.id.hall); // 影厅位置
                TextView tvPiece = (TextView) llyout.findViewById(R.id.tvPiece); // 票价

                tvStartTime.setText(obj.optString("tm"));
                tvEndTime.setText(obj.optString("end"));
                tvLanguage.setText(obj.optString("lang") + obj.optString("tp"));
                tvHall.setText(obj.optString("th"));

                llyout.setLayoutParams(lp);
                layMovieTime.addView(llyout);
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v == btnBack){
            finish();
        } else if (v == layShowMap){
            Intent intent = new Intent(context,ShowMapActivity.class);
            intent.putExtra("CurrentMoviesEntity",movieEntity);
            intent.putExtra("CinemaEntity",cinemaEntity);
            startActivity(intent);
        }
    }

    @Override
    public void tvMsg(String msg) {
        tvMsg.setText(msg);
    }
}